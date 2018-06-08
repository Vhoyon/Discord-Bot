package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;

public class TerminalCommandRestart extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"restart"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();

			console.onStart();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
