package consoles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

public abstract class TerminalConsole implements Console, Loggable {
	
	public TerminalConsole(){}
	
	@Override
	public void initialize(){
		
		Logger.setOutputs(this);
		
		Logger.log("Welcome to the Discord Bot terminal console!", false);
		
		BufferedReader br = null;
		
		try{
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
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
				
				System.out.print("> ");
				
				String input = br.readLine();
				
				canContinue = handleInput(input);
				
				System.out.println();
				
			}while(canContinue);
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(br != null){
				try{
					br.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		
		if("ERROR".equals(logType)){
			System.err.println(logText);
		}
		else{
			System.out.println(logText);
		}
		
	}
	
	private boolean handleInput(String input){
		
		if(input == null || input.length() == 0)
			return true;
		
		switch(input){
		case "start":
			
			try{
				onStart();
			}
			catch(Exception e){
				Logger.log(e);
			}
			
			break;
		
		case "stop":
			
			try{
				onStop();
			}
			catch(Exception e){
				Logger.log(e);
			}
			
			break;
		case "restart":
			
			try{
				onStop();
				
				onStart();
			}
			catch(Exception e){
				Logger.log(e);
			}
			
			break;
		case "exit":
			
			try{
				onStop();
			}
			catch(Exception e){}
			
			Logger.log("Thanks for using the bot!", LogType.INFO);
			
			return false;
			
		default:
			
			Logger.log("No command with the name \"" + input + "\"!",
					LogType.WARNING, false);
			
			break;
		}
		
		return true;
		
	}
	
}
