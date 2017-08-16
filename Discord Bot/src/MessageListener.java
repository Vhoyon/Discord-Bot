import commands.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.entities.TextChannel;

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
	
	public String[] messageSplit(String command){
		
		String[] messages = command.split(" ", 2);
		return messages;
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		AudioCommands audio = new AudioCommands(event);
		
		String messageRecu = event.getMessage().getContent();
		
		TextChannel textChannel = event.getTextChannel();
		
		VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(
				event);
		
		String[] message = messageSplit(messageRecu);
		
		if(message[0].contains("!")){
			
			switch(message[0].substring(1)){
			case "hello":
				textChannel.sendMessage("hello " + event.getAuthor().getName())
						.complete();
				break;
			case "help":
				Help help = new Help(event);
				break;
			case "connect":
				voiceChannels.JoinVoiceChannel();
				break;
			case "disconnect":
				voiceChannels.LeaveVoiceChannel();
				break;
			case "play":
				audio.play();
				break;
			case "clear":
				Clear clear = new Clear(event);
				break;
			case "spam":
				Spam spam = new Spam(event);
				break;
			case "game":
				Game game = new Game(event, message[1]);
				break;
			case "test":
				textChannel.sendMessage("test hello " + event.getAuthor().getName())
				.complete();
			default:
				break;
			}
			
		}
		
	}
	
}

//if (messageRecu.equalsIgnoreCase("!hello")) {
//	textChannel.sendMessage("hello " + event.getAuthor().getName()).complete();
//} else if (messageRecu.equalsIgnoreCase("!help")) {
//	Help help = new Help(event);
//} else if (messageRecu.equalsIgnoreCase("!connect")) {
//	voiceChannels.JoinVoiceChannel();
//} else if (messageRecu.equalsIgnoreCase("!disconnect")) {
//	voiceChannels.LeaveVoiceChannel();
//} else if (messageRecu.contains("!play ")) {
//	audio.play();
//} else if (messageRecu.equalsIgnoreCase("!clear")) {
//	Clear clear = new Clear(event);
//} else if (messageRecu.equalsIgnoreCase("!spam")) {
//	Spam spam = new Spam(event);
//}  else if (messageRecu.substring(0, 5).equalsIgnoreCase("!game")) {
//	Game game = new Game(event,messageRecu);
//} 