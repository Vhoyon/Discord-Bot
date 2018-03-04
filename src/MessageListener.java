import ressources.*;
import framework.Buffer;
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
			
			new CommandRouter(event, messageRecu, buffer).start();
			
		}
		
	}
	
}
