package errorHandling;

import utilities.BotCommand;
import vendor.abstracts.AbstractBotError;

/**
 * Vhoyon's custom implementation of the AbstractBotError class to format error
 * messages like we want.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see vendor.abstracts.AbstractBotError
 */
public class BotError extends AbstractBotError {
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, boolean) AbstractBotError(AbstractBotCommand, String,
	 *      boolean)
	 */
	public BotError(BotCommand commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String) AbstractBotError(AbstractBotCommand commandInError, String
	 *      errorMessage)
	 */
	public BotError(BotCommand commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, String, boolean) AbstractBotError(AbstractBotCommand
	 *      commandInError, String errorMessage, String errorEmoji, boolean
	 *      isErrorOneLiner)
	 */
	public BotError(BotCommand commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(AbstractBotCommand,
	 *      String, String) AbstractBotError(AbstractBotCommand commandInError,
	 *      String errorMessage, String errorEmoji)
	 */
	public BotError(BotCommand commandInError, String errorMessage,
			String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String, boolean)
	 *      AbstractBotError(String errorMessage, boolean isErrorOneLiner)
	 */
	public BotError(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String)
	 *      AbstractBotError(String errorMessage)
	 */
	public BotError(String errorMessage){
		super(errorMessage);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String, String,
	 *      boolean) AbstractBotError(String errorMessage, String errorEmoji,
	 *      boolean isErrorOneLiner)
	 */
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotError#AbstractBotError(String
	 *      errorMessage, String errorEmoji) AbstractBotError(String
	 *      errorMessage, String errorEmoji)
	 */
	public BotError(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoMessage(messageToSend, isErrorOneLiner());
	}
	
}
