package app;

import utilities.interfaces.Resources;
import vendor.abstracts.AbstractCommandRouter;
import vendor.abstracts.AbstractMessageListener;
import vendor.abstracts.CommandsLinker;
import vendor.objects.Buffer;
import vendor.objects.CommandsRepository;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * This class implements the logic used by the {@link vendor.abstracts.AbstractMessageListener AbstractMessageListener} class to create the right {@link vendor.abstracts.CommandsLinker CommandsLinker} (our {@link BotCommandsLinker}) and create the appropriate {@link vendor.abstracts.AbstractCommandRouter AbstractCommandRouter} (our {@link CommandRouter}).
 * 
 * @version 1.0
 * @since 0.1.0
 * @author Stephano
 * @see vendor.abstracts.AbstractMessageListener
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
