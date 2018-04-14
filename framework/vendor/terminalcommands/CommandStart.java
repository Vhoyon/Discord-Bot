package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;

public class CommandStart extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"start"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStart();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
