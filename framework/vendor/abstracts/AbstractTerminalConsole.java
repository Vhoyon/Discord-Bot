package vendor.abstracts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;
import vendor.objects.CommandsRepository;
import vendor.objects.TerminalCommandsLinker;

public abstract class AbstractTerminalConsole implements Console, Loggable {
	
	protected BufferedReader reader;
	
	private CommandsRepository commandsRepo;
	
	public AbstractTerminalConsole(){
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		this.commandsRepo = new CommandsRepository(new TerminalCommandsLinker());
	}
	
	public CommandsRepository getCommandsRepo(){
		return commandsRepo;
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		System.out.println(logText);
		
		// logToChannel(logText, logType);
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
	
	protected boolean handleInput(String input){
		
		if(input == null)
			return false;
		
		if(input.length() == 0){
			Logger.log("The input cannot be empty!", LogType.ERROR);
			
			return false;
		}
		
		AbstractTerminalCommand command = (AbstractTerminalCommand)commandsRepo
				.getContainer().initiateLink(input);
		
		command.setConsole(this);
		
		command.action();
		
		return command.doesStopTerminal();
		
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
	
	@Override
	public int getConfirmation(String question, QuestionType questionType){
		
		String[] choices = null;
		
		switch(questionType){
		case YES_NO:
			choices = new String[]
			{
				"y", "n"
			};
			break;
		case YES_NO_CANCEL:
			choices = new String[]
			{
				"y", "n", "c"
			};
			break;
		}
		
		StringBuilder choiceBuilder = new StringBuilder();
		
		choiceBuilder.append(" (");
		
		for(String possibility : choices){
			choiceBuilder.append(possibility).append(" / ");
		}
		
		choiceBuilder
				.delete(choiceBuilder.length() - 3, choiceBuilder.length());
		
		choiceBuilder.append(")");
		
		boolean doesCommandStopsBot = false;
		
		String formattedInput = null;
		
		do{
			
			String input = getInput(question + choiceBuilder.toString()).trim();
			
			System.out.println();
			
			if(input.length() == 0){
				Logger.log("The choice cannot be empty!", LogType.ERROR);
			}
			else{
				
				formattedInput = input.substring(0, 1).toLowerCase();
				
				for(int i = 0; i < choices.length && !doesCommandStopsBot; i++){
					if(choices[i].equals(formattedInput))
						doesCommandStopsBot = true;
				}
				
			}
			
		}while(!doesCommandStopsBot);
		
		switch(formattedInput){
		case "n":
			return NO;
		case "y":
			return YES;
		case "c":
			return CANCEL;
		default:
			return -1;
		}
		
	}
	
	@Override
	public void onStart() throws Exception{}
	
	@Override
	public void onStop() throws Exception{}
	
	@Override
	public void onInitialized(){}
	
	@Override
	public void onExit() throws Exception{}
	
}
