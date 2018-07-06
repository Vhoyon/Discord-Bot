package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;
import vendor.objects.ParametersHelp;

import java.util.function.Consumer;

public class CommandSetting extends BotCommand {
	
	@Override
	public void action(){
		
		tryAndChangeSetting("prefix", "prefix", (value) -> {
			sendMessage("You switched the prefix to `" + value + "`!");
		}, (parameterName) -> {
			sendMessage("The prefix setting requires at least one character!");
		});
		
		tryAndChangeSetting("nickname", "nickname", (value) -> {
			getGuildManager().setNickname(getSelfMember(), value.toString());
			
			sendMessage("The nickname of the bot is now set to `" + value + "`!");
		}, (parameterName) -> {
			sendMessage("The nickname of the bot cannot be empty!");
		});
		
	}
	
	public void tryAndChangeSetting(String settingName, String parameterName,
			Consumer<Object> onSuccess, Consumer<String> onNoContent){
		
		if(hasParameter(parameterName)){
			
			try{
				
				String parameterContent = getParameter(parameterName).getParameterContent();
				
				setSetting(settingName, parameterContent, onSuccess);
				
			}
			catch(NoContentException e){
				onNoContent.accept(parameterName);
			}
			
		}
		
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
					"Changes the prefix used for each command. Default is `" + getSettings().getField("prefix").getDefaultValue() + "`.",
					"prefix"),
			new ParametersHelp(
					"Changes the bot's nickname. His default name is `" + getSettings().getField("nickname").getDefaultValue() + "`.",
					"nickname"),
		};
	}
	
}
