package io.github.vhoyon.bot.utilities;

import io.github.vhoyon.bot.app.CommandRouter;
import io.github.vhoyon.bot.utilities.interfaces.Commands;
import io.github.vhoyon.bot.utilities.interfaces.Resources;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;

/**
 * Vhoyon's custom implementation of the
 * {@link io.github.vhoyon.vramework.abstracts.AbstractBotCommand
 * AbstractBotCommand} to format
 * parameters how we want them and add few utilities such as Settings handling.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class BotCommand extends AbstractBotCommand implements
		Commands, Resources {
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotCommand#AbstractBotCommand()
	 *      AbstractBotCommand()
	 */
	public BotCommand(){
		super();
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotCommand#AbstractBotCommand(AbstractBotCommand)
	 *      AbstractBotCommand(AbstractBotCommand commandToCopy)
	 */
	public BotCommand(BotCommand botCommandToCopy){
		super(botCommandToCopy);
	}
	
	@Override
	public CommandRouter getRouter(){
		return (CommandRouter)super.getRouter();
	}
	
	@Override
	public String formatParameter(String parameterToFormat){
		return buildVParameter(parameterToFormat);
	}
	
}
