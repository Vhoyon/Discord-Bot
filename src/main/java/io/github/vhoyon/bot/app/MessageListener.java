package io.github.vhoyon.bot.app;

import io.github.vhoyon.bot.utilities.interfaces.Resources;
import io.github.vhoyon.vramework.abstracts.AbstractCommandRouter;
import io.github.vhoyon.vramework.abstracts.AbstractMessageListener;
import io.github.vhoyon.vramework.abstracts.CommandsLinker;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.objects.CommandsRepository;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * This class implements the logic used by the
 * {@link io.github.vhoyon.vramework.abstracts.AbstractMessageListener AbstractMessageListener}
 * class to create the right {@link io.github.vhoyon.vramework.abstracts.CommandsLinker
 * CommandsLinker} (our {@link BotCommandsLinker}) and create the appropriate
 * {@link io.github.vhoyon.vramework.abstracts.AbstractCommandRouter AbstractCommandRouter} (our
 * {@link CommandRouter}).
 * 
 * @version 1.0
 * @since 0.1.0
 * @author Stephano
 * @see io.github.vhoyon.vramework.abstracts.AbstractMessageListener
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
