package errorHandling;

import utilities.BotCommand;
import vendor.abstracts.AbstractBotError;

public class BotError extends AbstractBotError {
	
	public BotError(BotCommand commandInError, String errorMessage,
					boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	public BotError(BotCommand commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	public BotError(BotCommand commandInError, String errorMessage,
					String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(BotCommand commandInError, String errorMessage,
					String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	public BotError(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	public BotError(String errorMessage){
		super(errorMessage);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoMessage(messageToSend, isErrorOneLiner());
	}
	
}
