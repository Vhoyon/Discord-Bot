package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.exceptions.JDANotSetException;
import vendor.modules.Logger;
import vendor.modules.Metrics;

public class CommandHelp extends AbstractTerminalCommand {
	
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
