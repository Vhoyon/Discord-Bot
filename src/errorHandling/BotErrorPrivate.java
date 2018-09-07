package errorHandling;

import utilities.BotCommand;
import vendor.abstracts.AbstractBotError;

/**
 * Vhoyon's custom implementation of the AbstractBotError class to format error
 * messages like we want to send to a user's private channel. We implemented it
 * to send a private info message for added squiggly lines.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see vendor.abstracts.AbstractBotError
 */
public class BotErrorPrivate extends AbstractBotError {
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, boolean) AbstractBotError(AbstractBotCommand, String,
	 *      boolean)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String) AbstractBotError(AbstractBotCommand commandInError, String
	 *      errorMessage)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, String, boolean) AbstractBotError(AbstractBotCommand
	 *      commandInError, String errorMessage, String errorEmoji, boolean
	 *      isErrorOneLiner)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, String) AbstractBotError(AbstractBotCommand commandInError,
	 *      String errorMessage, String errorEmoji)
	 */
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
			String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String, boolean)
	 *      AbstractBotError(String errorMessage, boolean isErrorOneLiner)
	 */
	public BotErrorPrivate(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String)
	 *      AbstractBotError(String errorMessage)
	 */
	public BotErrorPrivate(String errorMessage){
		super(errorMessage);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String, String,
	 *      boolean) AbstractBotError(String errorMessage, String errorEmoji,
	 *      boolean isErrorOneLiner)
	 */
	public BotErrorPrivate(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String
	 *      errorMessage, String errorEmoji) AbstractBotError(String
	 *      errorMessage, String errorEmoji)
	 */
	public BotErrorPrivate(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoPrivateMessage(messageToSend, isErrorOneLiner());
	}
	
}
