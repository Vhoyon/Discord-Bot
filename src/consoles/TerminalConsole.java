package consoles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

public abstract class TerminalConsole implements Console, Loggable {
	
	private BufferedReader reader;
	
	public TerminalConsole(){
		reader = null;
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
			
			reader = new BufferedReader(new InputStreamReader(System.in));
			
			onInitialized();
			
			boolean canContinue = true;
			
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
	public void log(String logText, String logType, boolean hasAppendedDate){
		System.out.println(logText);
		
//		logToChannel(logText, logType);
	}
	
	/**
	 * @deprecated
	 * @param logText
	 * @param logType
	 */
	@SuppressWarnings("unused")
	private void logToChannel(String logText, String logType){
		
		if("ERROR".equals(logType))
			System.err.println(logText);
		else
			System.out.println(logText);
		
	}
	
	private boolean handleInput(String input){
		
		if(input == null)
			return true;
		
		if(input.length() == 0){
			Logger.log("The input cannot be empty!", LogType.ERROR);
			
			return true;
		}
		
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
	
	@Override
	public String getInput(String message){
		
		System.out.println();
		System.out.print(message + " ");
		
		try{
			return reader.readLine();
		}
		catch(IOException e){
			Logger.log(e);
			
			return null;
		}
		
	}
	
}
