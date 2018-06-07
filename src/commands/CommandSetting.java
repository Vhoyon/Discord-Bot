package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;

public class CommandSetting extends BotCommand {
	
	@Override
	public void action(){
		
		try{
			
			String prefixContent = getParameter("prefix").getParameterContent();
			
			setSetting("prefix", prefixContent);
			
		}
		catch(NoContentException e){
			
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
