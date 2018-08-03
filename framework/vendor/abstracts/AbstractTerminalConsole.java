package vendor.abstracts;

import vendor.interfaces.Console;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;
import vendor.objects.CommandsRepository;
import vendor.objects.TerminalCommandsLinker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractTerminalConsole implements Console, Loggable {
	
	protected BufferedReader reader;
	
	private CommandsRepository commandsRepo;
	
	private String inputPrefix;
	
	private AtomicBoolean isWaitingForInput;
	private AtomicBoolean isLogging;
	private Thread loggingThread;
	
	private String latestInputMessage;
	
	public AbstractTerminalConsole(){
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		this.commandsRepo = new CommandsRepository(new TerminalCommandsLinker());
		
		this.setInputPrefix(">");
		this.isWaitingForInput = new AtomicBoolean(false);
		this.isLogging = new AtomicBoolean(false);
		this.latestInputMessage = "";
	}
	
	public CommandsRepository getCommandsRepo(){
		return commandsRepo;
	}
	
	public void setInputPrefix(String inputPrefix){
		this.inputPrefix = inputPrefix;
	}
	
	public String getInputPrefix(){
		return this.inputPrefix;
	}
	
	public boolean isWaitingForInput(){
		return this.isWaitingForInput.get();
	}
	
	public boolean isLogging(){
		return this.isLogging.get();
	}
	
	protected String getLatestInputMessage(){
		return this.latestInputMessage;
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		
		if(this.isWaitingForInput() && !this.isLogging())
			this.sendLog("---\n");
		
		this.isLogging.set(true);
		
		this.sendLog(logText);
		
		if(!this.isWaitingForInput()){
			this.isLogging.set(false);
		}
		else{
			
			if(loggingThread != null)
				loggingThread.interrupt();
			
			loggingThread = new Thread(() -> {
				
				try{
					Thread.sleep(250);
					
					printGetInputMessage(getLatestInputMessage());
					
					isLogging.set(false);
				}
				catch(InterruptedException e){}
				
			});
			
			loggingThread.start();
			
		}
		
		// logToChannel(logText, logType);
	}
	
	protected void sendLog(String log){
		this.sendLog(log, true);
	}
	
	protected void sendLog(String log, boolean appendNewLine){
		if(appendNewLine)
			System.out.println(log);
		else
			System.out.print(log);
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
		
		this.isWaitingForInput.set(false);
		
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
	
	protected void printGetInputMessage(){
		this.printGetInputMessage(this.getInputPrefix());
	}
	
	protected void printGetInputMessage(String message){
		sendLog("\n" + message + " ", false);
	}
	
	public String getInput(){
		return this.getInput(this.getInputPrefix());
	}
	
	@Override
	public String getInput(String message){
		
		printGetInputMessage(message);
		this.latestInputMessage = message;
		
		if(loggingThread != null)
			loggingThread = null;
		
		this.isWaitingForInput.set(true);
		
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
		
		String choiceSeparator = " / ";
		
		StringBuilder choiceBuilder = new StringBuilder();
		
		choiceBuilder.append(" (");
		
		for(String possibility : choices){
			choiceBuilder.append(possibility).append(choiceSeparator);
		}
		
		choiceBuilder.delete(choiceBuilder.length() - choiceSeparator.length(),
				choiceBuilder.length());
		
		choiceBuilder.append(")");
		
		boolean isChoiceValid = false;
		
		String formattedInput = null;
		
		do{
			
			String input = getInput(question + choiceBuilder.toString()).trim();
			
			System.out.println();
			
			if(input.length() == 0){
				Logger.log("The choice cannot be empty!", LogType.ERROR);
			}
			else{
				
				formattedInput = input.substring(0, 1).toLowerCase();
				
				for(int i = 0; i < choices.length && !isChoiceValid; i++){
					if(choices[i].equals(formattedInput))
						isChoiceValid = true;
				}
				
			}
			
		}while(!isChoiceValid);
		
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
