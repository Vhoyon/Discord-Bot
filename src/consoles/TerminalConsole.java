package consoles;

import java.io.IOException;

import vendor.abstracts.AbstractTerminalConsole;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

public abstract class TerminalConsole extends AbstractTerminalConsole {
	
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
			
			boolean canContinue;
			
			do{
				
				String input = getInput(">");
				
				canContinue = handleInput(input);
				
			}while(canContinue);
			
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
		Logger.log("Thanks for using the bot!", LogType.INFO, false);
	}
	
}
