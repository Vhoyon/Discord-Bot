package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;

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
	
}
