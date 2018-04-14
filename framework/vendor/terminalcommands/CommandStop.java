package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;

public class CommandStop extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"stop"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
