package consoles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

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
		
		BufferedReader br = null;
		
		try{
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
			while(true){
				
				System.out.print("> ");
				
				String input = br.readLine();
				
				handleInput(input);
				
			}
			
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
	
	private void handleInput(String input){
		
		if(input == null || input.length() == 0)
			return;
		
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
			
			System.exit(0);
			
			break;
		default:
			
			Logger.log("No command with the name \"" + input + "\"!",
					LogType.WARNING, false);
			
			break;
		}
		
	}
	
}
