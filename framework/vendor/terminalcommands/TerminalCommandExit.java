package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;

public class TerminalCommandExit extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"exit"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();

			console.onExit();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}

	@Override
	public boolean doesStopTerminal() {
		return true;
	}

}
