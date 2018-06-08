package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;
import vendor.objects.ParametersHelp;

public class CommandSetting extends BotCommand {
	
	@Override
	public void action(){
		
		try{
			
			String prefixContent = getParameter("prefix").getParameterContent();
			
			setSetting("prefix", prefixContent, (value) -> {
				
				sendMessage("You switched the prefix to `" + value + "`!");
				
			});
			
		}
		catch(NoContentException e){
			
			sendMessage("You dun goofed");
			
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
					"Changes the prefix used for each command. Default is `!!`.",
					"prefix")
		};
	}
	
}
