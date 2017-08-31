import java.util.Set;

import ressources.*;
import commands.*;
import commands.GameInteractionCommand.CommandType;
import framework.Buffer;
import framework.Command;
import framework.CommandConfirmed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRouter extends Thread implements Ressources, Commands {
	
	private MessageReceivedEvent event;
	private String messageRecu;
	private Buffer buffer;
	private Command command;
	
	public CommandRouter(MessageReceivedEvent event, String messageRecu,
			Buffer buffer){
		
		this.event = event;
		this.messageRecu = messageRecu;
		this.buffer = buffer;
		
	}
	
	public Command getCommand(){
		return command;
	}
	
	@Override
	public void run(){
		
		messageRecu = messageRecu.substring(PREFIX.length());
		
		String[] message = splitContent(messageRecu);
		
		this.setName(message[0] + event.getGuild().getId());
		
		buffer.setLatestGuildID(event.getGuild().getId());
		
		boolean neededConfirmation = false;
		
		try{
			
			Object needsConfirmation = buffer.get(BUFFER_CONFIRMATION);
			
			CommandConfirmed confirmationObject = (CommandConfirmed)needsConfirmation;
			if(message[0].equals(CONFIRM)){
				confirmationObject.confirmed();
				neededConfirmation = true;
			}
			else if(message[0].equals(CANCEL)){
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
			
			if(isCommandRunning(message[0], event.getGuild().getId()) != null){
				command = new SimpleTextCommand(
						"Cannot run another instance of the command `"
								+ message[0] + "` : it is already running.");
			}
			else
				switch(message[0]){
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
					command = new CommandStop(isCommandRunning(message[1],
							event.getGuild().getId()));
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
									+ buildVCommand(message[0])
									+ " *- please make an idea in the __ideas__ text channel!*",
							false);
					break;
				}
			
			command.setCommandName(message[0]);
			command.setContent(message[1]);
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
