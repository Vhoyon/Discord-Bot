import ressources.Ressources;
import commands.*;
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
	
	public String[] splitContent(String command){
		
		return command.split(" ", 2);
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		String messageRecu = event.getMessage().getContent();
		
		if(messageRecu.matches(Ressources.PREFIX + ".*")){
			
			messageRecu = messageRecu.substring(Ressources.PREFIX.length());
			
			//			AudioCommands audio = new AudioCommands(event);
			
			//			TextChannel textChannel = event.getTextChannel();
			
			//			VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(
			//					event);
			
			Command command;
			
			String[] message = splitContent(messageRecu);
			
			final int COMMAND = 0;
			final int CONTENT = 1;
			
			switch(message[COMMAND]){
			case "hello":
				command = new SimpleTextCommand("hello "
						+ event.getAuthor().getName());
				break;
			//			case "help":
			//				Help help = new Help(event);
			//				break;
			case "connect":
				command = new Connect();
				break;
			case "disconnect":
				command = new Disconnect();
				break;
			//			case "play":
			//				audio.play();
			//				break;
			//			case "clear":
			//				Clear clear = new Clear(event);
			//				break;
			//			case "spam":
			//				Spam spam = new Spam(event);
			//				break;
			case "game":
				command = new Game();
				break;
			case "test":
				command = new SimpleTextCommand("test hello "
						+ event.getAuthor().getName());
			default:
				command = new SimpleTextCommand(
						"~~ No actions created for the command \""
								+ Ressources.PREFIX
								+ message[COMMAND]
								+ "\" - please make an idea in the idea text channel! ~~");
				break;
			}
			
			command.setContent(message[CONTENT]);
			command.setContext(event);
			
			command.action();
			
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