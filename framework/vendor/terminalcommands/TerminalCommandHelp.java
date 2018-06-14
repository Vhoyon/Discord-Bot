package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;

public class TerminalCommandHelp extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"help"
		};
	}
	
	@Override
	public void action(){

		String fullHelpString = console.getCommandsRepo().getFullHelpString("Available commands :");

		Logger.log(fullHelpString, false);

	}
	
}
