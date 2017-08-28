import ressources.Ressources;
import commands.*;
import commands.GameInteractionCommand.CommandType;
import framework.Buffer;
import framework.Command;
import framework.CommandConfirmed;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * 
 * @author Stephano
 *
 * <br>
 * <br>
 *         Cette classe extend <b>ListenerAdapter</b> recoit les commandes de
 *         l'utilisateur et appele les classes necessaires
 */
public class MessageListener extends ListenerAdapter implements Ressources {
	
	private Buffer buffer;
	
	public MessageListener(){
		buffer = new Buffer();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		String messageRecu = event.getMessage().getContent();
		
		// Bots doesn't need attention...
		if(!event.getAuthor().isBot()){
			
			// Only interactions are through a server, no single conversations permitted!
			if(event.isFromType(ChannelType.PRIVATE)){
				
				event.getPrivateChannel()
						.sendMessage(
								"*You must be in a server to interact with me!*")
						.complete();
				
			}
			else if(event.isFromType(ChannelType.TEXT)){
				
				if(messageRecu.matches(PREFIX + ".+")){
					
					buffer.setGuildID(event.getGuild().getId());
					
					messageRecu = messageRecu.substring(PREFIX.length());
					
					//			AudioCommands audio = new AudioCommands(event);
					
					//			TextChannel textChannel = event.getTextChannel();
					
					//			VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(
					//					event);
					
					Command command;
					
					String[] message = splitContent(messageRecu);
					
					boolean hasConfirmed = false;
					
					try{
						
						Object needsConfirmation = buffer
								.get(BUFFER_CONFIRMATION);
						
						CommandConfirmed object = (CommandConfirmed)needsConfirmation;
						if(message[0].equals("confirm")){
							object.confirmed();
							hasConfirmed = true;
						}
						else if(message[0].equals("cancel")){
							object.cancelled();
							hasConfirmed = true;
						}
						else{
							object.cancelled();
						}
						
						buffer.remove(BUFFER_CONFIRMATION);
						
					}
					catch(IndexOutOfBoundsException e){}
					
					if(!hasConfirmed){
						
						switch(message[0]){
						case "hello":
							command = new SimpleTextCommand("hello "
									+ event.getAuthor().getName());
							break;
						case "help":
							command = new CommandHelp();
							break;
						case "connect":
							command = new Command(){
								public void action(){
									connect();
								}
							};
							break;
						case "disconnect":
							command = new Command(){
								public void action(){
									disconnect();
								}
							};
							break;
						//			case "play":
						//				audio.play();
						//				break;
						case "clear":
							command = new CommandClear();
							break;
						case "spam":
							command = new CommandSpam();
							break;
						case "game":
							command = new GameInteractionCommand(
									CommandType.INITIAL);
							break;
						case "game_add":
							command = new GameInteractionCommand(
									CommandType.ADD);
							break;
						case "game_remove":
							command = new GameInteractionCommand(
									CommandType.REMOVE);
							break;
						case "game_roll":
						case "roll":
							command = new GameInteractionCommand(
									CommandType.ROLL);
							break;
						case "game_list":
							command = new GameInteractionCommand(
									CommandType.LIST);
							break;
						case "test":
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
									"\\~\\~\n*No actions created for the command \"**"
											+ Ressources.PREFIX
											+ message[0]
											+ "**\" - please make an idea in the __ideas__ text channel!*\n\\~\\~");
							break;
						}
						
						command.setContent(message[1]);
						command.setContext(event);
						command.setBuffer(buffer);
						
						command.action();
						
					}
					
				}
				
			}
			
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
