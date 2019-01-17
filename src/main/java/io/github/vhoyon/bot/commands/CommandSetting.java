package io.github.vhoyon.bot.commands;

import java.util.ArrayList;
import java.util.function.Consumer;

import io.github.ved.jrequester.Option;
import io.github.ved.jrequester.OptionData;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.interfaces.BufferLevel;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.utilities.settings.Setting;
import io.github.vhoyon.vramework.utilities.settings.SettingRepositoryRepository;

/**
 * Command to interact with the settings of this bot. There's quite a few things
 * you can do, here's a small list :
 * <ul>
 * <li>Don't add any content to the available parameters to see their current
 * and default values;</li>
 * <li>Add the flag {@code -d} (<i>or {@code default}</i>) to set back the
 * settings added as parameters (which has <b>no text afterward</b>) to their
 * default state. Any parameters that has content will not be set back to its
 * default;</li>
 * <li>Add content after the parameters that you want to change to set its
 * value. Appropriate error messages will be sent if validations fails.</li>
 * </ul>
 * <p>
 * Run the command {@code !!help setting} to see more about available parameters
 * (in other words, available settings)!
 * </p>
 * 
 * @version 1.0
 * @since v0.7.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandSetting extends BotCommand {
	
	/**
	 * Class that facilitates the logic to change settings based on the code it
	 * gets given in its {@link #onSuccess(Object) onSuccess(T)} method.<br>
	 * It also allows to not execute the {@link #sendMessage(String)} (<i>and
	 * its variants</i>) methods by overriding those methods to execute only
	 * under the right state.
	 * 
	 * @param <T>
	 *            The type of the setting to deal with. This type will be used
	 *            when sending the object to the {@link #onSuccess(Object)
	 *            onSuccess(T)} method.
	 * @version 1.0
	 * @since v0.10.0
	 * @author V-ed (Guillaume Marcoux)
	 */
	protected abstract class SettingChanger<T> extends BotCommand {
		
		public boolean shouldSendMessages;
		
		private String settingName;
		private String parameterName;
		private boolean isPresent;
		
		public SettingChanger(String settingAndParamName){
			this(settingAndParamName, settingAndParamName);
		}
		
		public SettingChanger(String settingName, String parameterName){
			super(CommandSetting.this);
			
			this.shouldSendMessages = true;
			this.settingName = settingName;
			this.parameterName = parameterName;
			this.isPresent = this.hasOption(this.parameterName);
			
			CommandSetting.this.settings.add(this);
		}
		
		@Override
		public void actions(){
			
			@SuppressWarnings("unchecked")
			Consumer<Object> onSuccess = (value) -> {
				try{
					onSuccess((T)value);
				}
				catch(ClassCastException e){
					Logger.log(
							"One of your SettingChanger is not well typed : make sure that your settings configuration and your SettingChanger in CommandSetting are synchronized correctly!",
							Logger.LogType.ERROR);
				}
			};
			
			OptionData param = getOption(this.parameterName);
			
			String parameterContent = param.getContent();
			
			BufferLevel level = isForGuild() ? BufferLevel.GUILD : null;
			
			if(parameterContent == null){
				
				Setting<Object> settingField = getSettings(level).getSetting(
						this.settingName);
				
				if(CommandSetting.this.shouldSwitchToDefault){
					
					this.setSendable(false);
					
					settingField.setToDefaultValue(onSuccess);
					
					if(level == BufferLevel.GUILD && hasTextSettings()){
						
						SettingRepositoryRepository
								.getReposOfGuildTextChannels(getGuild())
								.forEach(
										repo -> repo.getSetting(
												this.settingName)
												.setToDefaultValue());
						
					}
					
					this.setSendable(true);
					
					sendMessage("The setting " + code(settingName)
							+ " has been set back to its default ("
							+ ital(code(settingField.getDefaultValue())) + ")!");
					
				}
				else{
					
					Object defaultSettingValue = settingField.getDefaultValue();
					Object currentSettingValue = settingField.getValue();
					
					sendMessage("The default value for the setting "
							+ code(settingName) + " is : "
							+ ital(code(defaultSettingValue))
							+ ". Current value : " + code(currentSettingValue)
							+ ".");
					
				}
				
			}
			else{
				
				try{
					
					setSetting(settingName, parameterContent, level, onSuccess);
					
					if(level == BufferLevel.GUILD && hasTextSettings()){
						
						SettingRepositoryRepository
								.getReposOfGuildTextChannels(getGuild())
								.forEach(
										repo -> repo.save(settingName,
												parameterContent));
						
					}
					
				}
				catch(BadFormatException e){
					new BotError(this, e.getMessage());
				}
				
			}
			
		}
		
		public abstract void onSuccess(T value);
		
		public boolean isForGuild(){
			return isOnlyForGuild() || hasOption("g");
		}
		
		public boolean isOnlyForGuild(){
			return false;
		}
		
		public boolean hasTextSettings(){
			return !isOnlyForGuild();
		}
		
		public boolean isPresent(){
			return this.isPresent;
		}
		
		public void setSendable(boolean shouldSendMessages){
			this.shouldSendMessages = shouldSendMessages;
		}
		
		@Override
		public String getCall(){
			return null;
		}
		
		@Override
		public String sendMessage(String messageToSend){
			return this.shouldSendMessages ? super.sendMessage(messageToSend)
					: null;
		}
		
		@Override
		public String lang(String key){
			return this.shouldSendMessages ? super.lang(key) : null;
		}
		
		@Override
		public String lang(String key, Object... replacements){
			return this.shouldSendMessages ? super.lang(key, replacements)
					: null;
		}
		
	}
	
	private boolean shouldSwitchToDefault;
	private ArrayList<SettingChanger<?>> settings = new ArrayList<>();
	
	@Override
	public void actions(){
		
		this.shouldSwitchToDefault = hasOption("d");
		
		this.setupSettings();
		
		boolean hasAtLeastOneSetting = settings.stream()
				.filter(SettingChanger::isPresent).peek(SettingChanger::action)
				.count() > 0;
		
		if(!hasAtLeastOneSetting){
			new BotError(
					this,
					"You haven't entered a single setting parameter to change - get to know which ones are available using "
							+ buildVCommand(HELP + " " + "setting") + "!");
		}
		
	}
	
	/**
	 * Method to setup all the available settings, confirming their types and
	 * defining the arbitrary code to run on success.<br>
	 * This setup requires the use of the SettingChanger class that handles all
	 * the storage and when-to-call logic. Simply create a new SettingChanger,
	 * define its {@code onSuccess} method and you'll be good to go.
	 * 
	 * @since v0.10.0
	 */
	protected void setupSettings(){
		
		new SettingChanger<String>("prefix"){
			@Override
			public void onSuccess(String newPrefix){
				sendMessage("You switched the prefix to " + code(newPrefix)
						+ "!");
			}
		};
		
		new SettingChanger<Character>("param_prefix"){
			@Override
			public void onSuccess(Character newParamPrefix){
				sendMessage("You switched the parameters prefix to "
						+ code(newParamPrefix)
						+ " ("
						+ ital("and of course "
								+ code(newParamPrefix + "" + newParamPrefix))
						+ ")!");
			}
		};
		
		new SettingChanger<String>("nickname"){
			@Override
			public void onSuccess(String newNickname){
				setSelfNickname(newNickname);
				
				sendMessage("The nickname of the bot is now set to "
						+ code(newNickname) + "!");
			}
		};
		
		new SettingChanger<Boolean>("confirm_stop"){
			@Override
			public void onSuccess(Boolean isConfirming){
				if(isConfirming){
					sendMessage("Stopping the most recent running command will now ask for a confirmation.");
				}
				else{
					sendMessage("Stopping the most recent running command will not ask for a confirmation anymore.");
				}
			}
		};
		
		new SettingChanger<Integer>("volume"){
			@Override
			public void onSuccess(Integer volume){
				MusicManager.get().onPlayerPresent(this.getGuild(),
						(player) -> player.setVolume(volume));
				
				sendMessage("The default volume will now be " + code(volume)
						+ "!");
			}
			
			@Override
			public boolean isOnlyForGuild(){
				return true;
			}
		};
		
		new SettingChanger<Integer>("empty_drop_delay"){
			@Override
			public void onSuccess(Integer delay){
				MusicManager.get().onPlayerPresent(this.getGuild(),
						(player) -> player.setEmptyDropDelay(delay));
				
				sendMessage("The default disconnect delay for the bot when the music player is empty is now "
						+ code(delay) + "!");
			}
			
			@Override
			public boolean isOnlyForGuild(){
				return true;
			}
		};
		
		new SettingChanger<Integer>("alone_drop_delay"){
			@Override
			public void onSuccess(Integer delay){
				sendMessage("The default disconnect delay for the bot when he gets alone is now "
						+ code(delay) + "!");
			}
			
			@Override
			public boolean isOnlyForGuild(){
				return true;
			}
		};
		
	}
	
	@Override
	public String getCall(){
		return "setting";
	}
	
	@Override
	public String[] getAliases(){
		return new String[]
		{
			"settings"
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "This command changes settings for the bot. Use the parameters below to change what you want to change!";
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(
					"Changes the prefix used for each command. Default is "
							+ code(getSettings().getSetting("prefix")
									.getDefaultValue()) + ".", "prefix"),
			new Option(
					"Changes the parameters prefix used for each command. Default is "
							+ code(getSettings().getSetting("param_prefix")
									.getDefaultValue()) + ".", "param_prefix"),
			new Option("Changes the bot's nickname. His default name is "
					+ code(getSettings().getSetting("nickname")
							.getDefaultValue()) + ".", "nickname"),
			new Option(
					"Determine the behavior of stopping the most recent running command. "
							+ code("true")
							+ " to ask for a confirmation, "
							+ code("false")
							+ " to stop the most recent command without confirming. Default is set to "
							+ code(getSettings().getSetting("nickname")
									.getDefaultValue()) + ".", "confirm_stop"),
			new Option(
					"Changes the bot's default volume when playing some music. The default value is "
							+ code(getSettings().getSetting("volume")
									.getDefaultValue()) + ".", "volume"),
			new Option(
					"Changes the bot's default disconnect time when the music player is empty. The default is "
							+ code(getSettings().getSetting("empty_drop_delay")
									.getDefaultValue()) + "ms.",
					"empty_drop_delay"),
			new Option(
					"Changes the bot's default disconnect time when the bot is not with humans anymore. The default is "
							+ code(getSettings().getSetting("alone_drop_delay")
									.getDefaultValue()) + "ms.",
					"alone_drop_delay"),
			new Option(
					"Switch to allow for putting back the default value for each settings as parameters quickly.",
					false, "d", "default"),
			new Option(
					"Switch to allow for setting the values to the Guild level instead of the TextChannel.",
					false, "g", "guild"),
		};
	}
	
}
