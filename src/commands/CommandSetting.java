package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;
import vendor.objects.ParametersHelp;

public class CommandSetting extends BotCommand {
	
	@Override
	public void action(){
		
		tryAndChangeSetting("prefix", "prefix", (value) -> {
			sendMessage("You switched the prefix to `" + value + "`!");
		}, (parameterName) -> {
			sendMessage("You dun goofed");
		});
		
	}
	
	public void tryAndChangeSetting(String settingName, String parameterName,
			Consumer<Object> onSuccess, Consumer<String> onNoContent){
		
		try{
			
			String parameterContent = getParameter(parameterName).getParameterContent();
			
			setSetting(settingName, parameterContent, onSuccess);
			
		}
		catch(NoContentException e){
			onNoContent.accept(parameterName);
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
					"prefix")
		};
	}
	
}
