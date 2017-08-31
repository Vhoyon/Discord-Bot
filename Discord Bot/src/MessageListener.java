import ressources.*;
import framework.Buffer;
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
					
					new CommandRouter(event, messageRecu, buffer).start();
					
				}
				else if(messageRecu.equals(PREFIX)){
					
					event.getTextChannel()
							.sendMessage(
									"... you wanted to call upon me or...?")
							.complete();
					
				}
				
			}
			
		}
		
	}
	
}
