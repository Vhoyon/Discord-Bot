package vendor.terminalcommands;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.modules.Logger;
import vendor.modules.Metrics;

public class CommandUptime extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"uptime"
		};
	}
	
	@Override
	public void action(){

		long milliseconds = Metrics.getUptime();

		Logger.log("The bot has been up for " + milliseconds + " milliseconds!");
		
	}

}
