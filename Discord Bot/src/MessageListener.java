import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;

import ressources.Ressources;
import commands.*;
import commands.CommandGameInteraction.CommandGameType;
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
public class MessageListener extends ListenerAdapter {
	
	private ArrayList<Object> buffer = new ArrayList<>();
	
	public MessageListener(){
		buffer.add(false);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		String messageRecu = event.getMessage().getContent();
		
		if(messageRecu.matches(Ressources.PREFIX + ".+")){
			
			messageRecu = messageRecu.substring(Ressources.PREFIX.length());
			
			//			AudioCommands audio = new AudioCommands(event);
			
			//			TextChannel textChannel = event.getTextChannel();
			
			//			VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(
			//					event);
			
			Command command;
			
			String[] message = splitContent(messageRecu);
			
			switch(message[0]){
			case "hello":
				command = new SimpleTextCommand("hello "
						+ event.getAuthor().getName());
				break;
			//			case "help":
			//				Help help = new Help(event);
			//				break;
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
			//			case "spam":
			//				Spam spam = new Spam(event);
			//				break;
			case "game":
				command = new CommandGameInteraction(CommandGameType.INITIAL);
				break;
			case "game_add":
				command = new CommandGameInteraction(CommandGameType.ADD);
				break;
			case "game_remove":
				command = new CommandGameInteraction(CommandGameType.REMOVE);
				break;
			case "game_roll":
			case "roll":
				command = new CommandGameInteraction(CommandGameType.ROLL);
				break;
			case "test":
				command = new SimpleTextCommand("test hello "
						+ event.getAuthor().getName());
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
	
	public String[] splitContent(String command){
		
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
