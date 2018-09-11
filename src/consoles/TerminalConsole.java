package consoles;

import vendor.abstracts.AbstractTerminalConsole;
import vendor.modules.Logger;

import java.io.IOException;

/**
 * Vhoyon's custom implementation of the
 * {@link vendor.abstracts.AbstractTerminalConsole AbstractTerminalConsole} that
 * allows us to get commands to start, stop, exit, etc our bot from the
 * Terminal.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see vendor.abstracts.AbstractTerminalConsole
 */
public abstract class TerminalConsole extends AbstractTerminalConsole {
	
	/**
	 * Constructor that simply calls {@link AbstractTerminalConsole}'s
	 * {@link AbstractTerminalConsole#AbstractTerminalConsole() constructor}.
	 * 
	 * @since v0.4.0
	 */
	public TerminalConsole(){
		super();
	}
	
	@Override
	public void initialize(){
		
		Logger.setOutputs(this);
		
		Logger.log("Welcome to the Discord Bot terminal console!", false);
		
		try{
			
			try{
				
				Thread.sleep(100);
				System.err.println();
				
				Thread.sleep(200);
				
				System.out.print("Initializing");
				Thread.sleep(333);
				
				System.out.print(".");
				Thread.sleep(333);
				
				System.out.print(".");
				Thread.sleep(333);
				
				System.out.println(".\nInitialized!\n");
				Thread.sleep(333);
				
			}
			catch(InterruptedException e){}
			
			onInitialized();
			
			boolean shouldStop;
			
			do{
				
				String input = getInput();
				
				shouldStop = handleInput(input);
				
			}while(!shouldStop);
			
		}
		finally{
			if(reader != null){
				try{
					reader.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void onExit(){
		Logger.log("\nThanks for using the bot!", false);
	}
	
}
