import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ressources.*;
import commands.*;
import commands.GameInteractionCommand.CommandType;
import framework.Buffer;
import framework.Command;
import framework.CommandConfirmed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRouter extends Thread implements Ressources, Commands {
	
	private class Request {
		
		private class Parameter {
			
			public final static String PREFIX = "--";
			
			private String parameter;
			private String parameterContent;
			
			public Parameter(String parameter){
				
				this.parameter = parameter;
				
			}
			
			public String getParameter(){
				return parameter.substring(PREFIX.length());
			}
			
			public String getParameterContent(){
				return parameterContent;
			}
			
			public void setParameterContent(String parameterContent){
				this.parameterContent = parameterContent;
			}
			
		}
		
		private String command;
		private String content;
		private ArrayList<Parameter> parameters;
		
		public Request(String receivedMessage){
			
			String[] messageSplit = splitCommandAndContent(receivedMessage);
			
			setCommand(messageSplit[0]);
			setContent(messageSplit[1]);
			
			if(content != null){
				
				// Test if content contains parameters.
				// The params must be right after command for it to trigger.
				//				if(content.matches("(" + Parameter.PREFIX + ".+)+")){
				
				parameters = new ArrayList<>();
				
				// Splits the content : Search for all spaces, except thoses
				// in double quotes and put all what's found in the
				// possibleParams ArrayList.
				// Necessary since .split() removes the wanted Strings.
				ArrayList<String> possibleParams = new ArrayList<>();
				Matcher matcher = Pattern.compile(
						"[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(content);
				while(matcher.find()){
					possibleParams.add(matcher.group());
				}
				
				boolean canRoll = true;
				
				for(int i = 0; i < possibleParams.size() && canRoll; i++){
					
					String stringToTest = possibleParams.get(i);
					
					// If string is structured as a parameter, create it.
					if(stringToTest.matches(Parameter.PREFIX + "[^\\s]+")){
						
						Parameter newParam = new Parameter(
								possibleParams.get(i));
						
						try{
							
							String possibleParamContent = possibleParams
									.get(i + 1);
							
							// If the following String isn't another param, set
							// said String as the content for the current param.
							if(!possibleParamContent.matches(Parameter.PREFIX
									+ "[^\\s]+")){
								
								newParam.setParameterContent(possibleParamContent
										.replaceAll("\"", ""));
								
								i++;
								
							}
							
						}
						catch(IndexOutOfBoundsException e){
							canRoll = false;
						}
						
						parameters.add(newParam);
						
						// TODO Remove the parameters and their content from the content of the whole command
//						getContent().substring(
//								getContent().indexOf(newParam.getParameter()),
//								getContent().indexOf(
//										newParam.getParameterContent())
//										+ newParam.getParameterContent()
//												.length());
						
					}
					
				}
				
				new String();
				
				//				}
				
			}
			
		}
		
		public String getCommand(){
			return command;
		}
		
		public void setCommand(String command){
			this.command = command.substring(PREFIX.length());
		}
		
		public String getContent(){
			return content;
		}
		
		public void setContent(String content){
			this.content = content;
		}
		
		private String[] splitCommandAndContent(String command){
			
			// Remove leading / trailing spaces (leading spaces are removed anyway)
			String[] splitted = command.trim().replaceAll("( )+", " ")
					.split(" ", 2);
			
			if(splitted.length == 1){
				// TODO : Find better way you lazy basterd.
				String actualCommand = splitted[0];
				splitted = new String[2];
				splitted[0] = actualCommand;
			}
			
			splitted[0] = splitted[0].toLowerCase();
			
			return splitted;
			
		}
		
	}
	
	private MessageReceivedEvent event;
	private Request request;
	private Buffer buffer;
	private Command command;
	
	public CommandRouter(MessageReceivedEvent event, String messageRecu,
			Buffer buffer){
		
		this.event = event;
		this.request = new Request(messageRecu);
		this.buffer = buffer;
		
	}
	
	public Command getCommand(){
		return command;
	}
	
	@Override
	public void run(){
		
		//		String[] message = splitContent(messageRecu);
		
		this.setName(request.getCommand() + event.getGuild().getId());
		
		buffer.setLatestGuildID(event.getGuild().getId());
		
		boolean neededConfirmation = false;
		
		try{
			
			Object needsConfirmation = buffer.get(BUFFER_CONFIRMATION);
			
			CommandConfirmed confirmationObject = (CommandConfirmed)needsConfirmation;
			if(request.getCommand().equals(CONFIRM)){
				confirmationObject.confirmed();
				neededConfirmation = true;
			}
			else if(request.getCommand().equals(CANCEL)){
				confirmationObject.cancelled();
				neededConfirmation = true;
			}
			else{
				confirmationObject.cancelled();
			}
			
			buffer.remove(BUFFER_CONFIRMATION);
			
		}
		catch(NullPointerException e){}
		
		if(!neededConfirmation){
			
			if(isCommandRunning(request.getCommand(), event.getGuild().getId()) != null){
				command = new SimpleTextCommand(
						"Cannot run another instance of the command `"
								+ request.getCommand()
								+ "` : it is already running.");
			}
			else
				switch(request.getCommand()){
				case HELLO:
					command = new SimpleTextCommand("hello "
							+ event.getAuthor().getName());
					break;
				case HELP:
					command = new CommandHelp();
					break;
				case CONNECT:
					command = new Command(){
						public void action(){
							connect();
						}
					};
					break;
				case DISCONNECT:
					command = new Command(){
						public void action(){
							disconnect();
						}
					};
					break;
				//			case "play":
				//				audio.play();
				//				break;
				case CLEAR:
					command = new CommandClear();
					break;
				case SPAM:
					command = new CommandSpam();
					break;
				case TERMINATE:
					command = new SimpleTextCommand(
							"**Y** *O*__***~~U~~***__ __*C* **A**N__ **NO*__t_S*To** ~~P*T*~~ __he*B**O**T*");
					break;
				case STOP:
					command = new CommandStop(isCommandRunning(
							request.getContent(), event.getGuild().getId()));
					break;
				case GAME:
					command = new GameInteractionCommand(CommandType.INITIAL);
					break;
				case GAME_ADD:
					command = new GameInteractionCommand(CommandType.ADD);
					break;
				case GAME_REMOVE:
					command = new GameInteractionCommand(CommandType.REMOVE);
					break;
				case GAME_ROLL:
				case GAME_ROLL_ALT:
					command = new GameInteractionCommand(CommandType.ROLL);
					break;
				case GAME_LIST:
					command = new GameInteractionCommand(CommandType.LIST);
					break;
				case TEST:
					command = new CommandConfirmed(
							"Are you sure you want to do X?"){
						@Override
						public void confirmed(){
							sendMessage("hi");
						}
					};
					break;
				default:
					command = new SimpleTextCommand(
							"*No actions created for the command* "
									+ buildVCommand(request.getCommand())
									+ " *- please make an idea in the __ideas__ text channel!*",
							false);
					break;
				}
			
			command.setCommandName(request.getCommand());
			command.setContent(request.getContent());
			command.setContext(event);
			command.setBuffer(buffer);
			command.setGuildID(event.getGuild().getId());
			
			command.action();
			
		}
		
	}
	
	public String[] splitContent(String command){
		
		// Remove leading / trailing spaces (leading spaces are removed anyway)
		command = command.trim();
		
		command = command.replaceAll("( )+", " ");
		
		String[] splitted = command.split(" ", 2);
		
		if(splitted.length == 1){
			// TODO : Find better way you lazy basterd.
			String actualCommand = splitted[0];
			splitted = new String[2];
			splitted[0] = actualCommand;
		}
		
		splitted[0] = splitted[0].toLowerCase();
		
		return splitted;
		
	}
	
	public Command isCommandRunning(String commandName, String guildID){
		
		Command commandFound = null;
		
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		
		for(Thread thread : threadArray){
			
			if(thread instanceof CommandRouter
					&& thread.getName().equals(commandName + guildID)){
				
				commandFound = ((CommandRouter)thread).getCommand();
				break;
				
			}
			
		}
		
		return commandFound;
		
	}
	
}