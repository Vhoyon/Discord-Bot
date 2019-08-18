package io.github.vhoyon.bot.errorHandling;

import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.abstracts.AbstractBotError;
import io.github.vhoyon.vramework.interfaces.Emoji;

/**
 * Vhoyon's custom implementation of the AbstractBotError class to format error
 * messages like we want to send to a user's private channel. We implemented it
 * to send a private info message for added squiggly lines.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError
 */
public class BotErrorPrivate extends AbstractBotError {
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, boolean) AbstractBotError(AbstractBotCommand, String,
	 *      boolean)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String) AbstractBotError(AbstractBotCommand commandInError, String
	 *      errorMessage)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, Emoji, boolean) AbstractBotError(AbstractBotCommand
	 *      commandInError, String errorMessage, Emoji errorEmoji, boolean
	 *      isErrorOneLiner)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   Emoji errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, Emoji) AbstractBotError(AbstractBotCommand commandInError,
	 *      String errorMessage, Emoji errorEmoji)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
			Emoji errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(String,
	 *      boolean)
	 *      AbstractBotError(String errorMessage, boolean isErrorOneLiner)
	 */
	public BotErrorPrivate(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(String)
	 *      AbstractBotError(String errorMessage)
	 */
	public BotErrorPrivate(String errorMessage){
		super(errorMessage);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(String,
	 *      Emoji, boolean) AbstractBotError(String errorMessage, Emoji
	 *      errorEmoji,
	 *      boolean isErrorOneLiner)
	 */
	public BotErrorPrivate(String errorMessage, Emoji errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotError#AbstractBotError(String
	 *      errorMessage, Emoji errorEmoji) AbstractBotError(String
	 *      errorMessage, Emoji errorEmoji)
	 */
	public BotErrorPrivate(String errorMessage, Emoji errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoPrivateMessage(messageToSend, isErrorOneLiner());
	}
	
}
