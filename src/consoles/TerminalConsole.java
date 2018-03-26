package consoles;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;

public abstract class TerminalConsole implements Console {
	
	private class Terminal implements Loggable {
		
		@Override
		public void log(String logText){
			
			System.out.println(logText + "\n");
			
		}
		
	}
	
	public TerminalConsole(){
		
		Logger.setOutputs(new Terminal());
		
		Logger.log("Welcome to the Discord Bot terminal console!", false);
		
		// TODO : Handle inputting commands from the temrinal using a BufferedReader
		// This todo is actually quite important has it defines the thread behavior
		// and will keep the bot alive as long as the terminal is open.
		
	}
	
}
