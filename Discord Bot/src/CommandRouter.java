import ressources.*;
import commands.*;
import commands.GameInteractionCommand.CommandType;
import framework.Buffer;
import framework.Command;
import framework.CommandConfirmed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRouter implements Runnable, Ressources, Commands {
	
	private MessageReceivedEvent event;
	private String messageRecu;
	private Buffer buffer;
	
	public CommandRouter(MessageReceivedEvent event, String messageRecu,
			Buffer buffer){
		
		this.event = event;
		this.messageRecu = messageRecu;
		this.buffer = buffer;
		
	}
	
	@Override
	public void run(){
		
		buffer.setLatestGuildID(event.getGuild().getId());
		
		messageRecu = messageRecu.substring(PREFIX.length());
		
		//			AudioCommands audio = new AudioCommands(event);
		
		//			TextChannel textChannel = event.getTextChannel();
		
		//			VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(
		//					event);
		
		Command command;
		
		String[] message = splitContent(messageRecu);
		
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
			
			switch(message[0].toLowerCase()){
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
			case STOP:
				command = new SimpleTextCommand(
						"**T** *h*~~E~~ __*b* **O**t__ **CA*__n__N*oT** ~~B*E*~~ __St*O**P**P*eD__");
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
				command = new CommandConfirmed("Are you sure you want to do X?"){
					@Override
					public void confirmed(){
						sendMessage("hi");
					}
				};
				break;
			default:
				command = new SimpleTextCommand(
						"\\~\\~\n*No actions created for the command* "
								+ buildVCommand(message[0])
								+ " *- please make an idea in the __ideas__ text channel!*\n\\~\\~");
				break;
			}
			
			command.setContent(message[1]);
			command.setContext(event);
			command.setBuffer(buffer);
			command.setGuildID(buffer.getLatestGuildID());
			
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
		
		return splitted;
		
	}
	
}
