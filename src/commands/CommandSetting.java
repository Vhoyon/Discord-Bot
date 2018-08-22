package commands;

import errorHandling.BotError;
import utilities.BotCommand;
import utilities.music.MusicManager;
import vendor.objects.ParametersHelp;
import vendor.utilities.settings.SettingField;

import java.util.function.Consumer;

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
	
	private boolean shouldSwitchToDefault;
	
	@Override
	public void action(){
		
		this.shouldSwitchToDefault = hasParameter("d");
		
		tryAndChangeSetting("prefix", "prefix", (value) -> {
			sendMessage("You switched the prefix to " + code(value) + "!");
		});
		
		tryAndChangeSetting(
				"param_prefix",
				"param_prefix",
				(value) -> {
					sendMessage("You switched the parameters prefix to "
							+ code(value)
							+ " ("
							+ ital("and of course "
									+ code(value.toString() + value.toString()))
							+ ")!");
				});
		
		tryAndChangeSetting("nickname", "nickname", (value) -> {
			setSelfNickname(value.toString());
			
			sendMessage("The nickname of the bot is now set to " + code(value)
					+ "!");
		});
		
		tryAndChangeSetting(
				"confirm_stop",
				"confirm_stop",
				(value) -> {
					boolean isConfirming = (boolean)value;
					
					if(isConfirming){
						sendMessage("Stopping the most recent running command will now ask for a confirmation.");
					}
					else{
						sendMessage("Stopping the most recent running command will not ask for a confirmation anymore.");
					}
				});
		
		tryAndChangeSetting("volume", "volume", (value) -> {
			
			if(MusicManager.get().hasPlayer(this.getGuild())){
				MusicManager.get().getPlayer(this).setVolume((int)value);
			}
			
			sendMessage("The default volume will now be " + code(value) + "!");
			
		});
		
	}
	
	/**
	 * Method to prevent code duplicatas that executes the logic to change a
	 * setting while still validating values and handling error messages sent on
	 * validation fail. <br>
	 * This method also handles the logic for displaying the default/current
	 * value of a setting and setting back a setting to its default.
	 * 
	 * @param settingName
	 *            The name of the setting to apply the logic of upon.
	 * @param parameterName
	 *            The name of the parameter to link with the request (this is
	 *            used to know what content will be taken from the request to
	 *            apply on the setting found using the name given by the
	 *            parameter {@code settingName}.
	 * @param onSuccess
	 *            Arbitrary code to run when changing the setting was a success.
	 * @since v0.7.0
	 */
	public void tryAndChangeSetting(String settingName, String parameterName,
			Consumer<Object> onSuccess){
		
		onParameterPresent(
				parameterName,
				param -> {
					
					String parameterContent = param.getContent();
					
					if(parameterContent == null){
						
						SettingField<Object> settingField = getSettings()
								.getField(settingName);
						
						if(this.shouldSwitchToDefault){
							
							settingField.setToDefaultValue(onSuccess);
							
							sendMessage("The setting "
									+ code(settingName)
									+ " has been set back to its default ("
									+ ital(code(settingField.getDefaultValue()))
									+ ")!");
							
						}
						else{
							
							Object defaultSettingValue = settingField
									.getDefaultValue();
							Object currentSettingValue = settingField
									.getValue();
							
							sendMessage("The default value for the setting "
									+ code(settingName) + " is : "
									+ ital(code(defaultSettingValue))
									+ ". Current value : "
									+ code(currentSettingValue) + ".");
							
						}
						
					}
					else{
						
						try{
							setSetting(settingName, parameterContent, onSuccess);
						}
						catch(IllegalArgumentException e){
							new BotError(this, e.getMessage());
						}
						
					}
					
				});
		
	}
	
	@Override
	public Object getCalls(){
		return new String[]
		{
			"setting", "settings"
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "This command changes settings for the bot. Use the parameters below to change what you want to change!";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"Changes the prefix used for each command. Default is "
							+ code(getSettings().getField("prefix")
									.getDefaultValue()) + ".", "prefix"),
			new ParametersHelp(
					"Changes the parameters prefix used for each command. Default is "
							+ code(getSettings().getField("param_prefix")
									.getDefaultValue()) + ".", "param_prefix"),
			new ParametersHelp(
					"Changes the bot's nickname. His default name is "
							+ code(getSettings().getField("nickname")
									.getDefaultValue()) + ".", "nickname"),
			new ParametersHelp(
					"Determine the behavior of stopping the most recent running command. "
							+ code("true")
							+ " to ask for a confirmation, "
							+ code("false")
							+ " to stop the most recent command without confirming. Default is set to "
							+ code(getSettings().getField("nickname")
									.getDefaultValue()) + ".", "confirm_stop"),
			new ParametersHelp(
					"Changes the bot's default volume when playing some music. The default value is "
							+ code(getSettings().getField("volume")
									.getDefaultValue()) + ".", "volume"),
			new ParametersHelp(
					"Switch to allow for putting back the default value for each settings as parameters quickly.",
					false, "d", "default")
		};
	}
	
}
