package app;

import utilities.interfaces.Resources;
import vendor.abstracts.AbstractCommandRouter;
import vendor.abstracts.AbstractMessageListener;
import vendor.abstracts.CommandsLinker;
import vendor.objects.Buffer;
import vendor.objects.CommandsRepository;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Cette classe extend <b>ListenerAdapter</b> recoit les commandes de
 * l'utilisateur et appele les classes necessaires
 * 
 * @author Stephano
 */
public class MessageListener extends AbstractMessageListener implements
		Resources {
	
	@Override
	protected CommandsLinker createCommandLinker(){
		return new BotCommandsLinker();
	}
	
	@Override
	protected AbstractCommandRouter createRouter(MessageReceivedEvent event,
			String receivedMessage, Buffer buffer,
			CommandsRepository commandsRepo){
		return new CommandRouter(event, receivedMessage, buffer, commandsRepo);
	}
	
}
