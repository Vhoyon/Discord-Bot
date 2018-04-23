package errorHandling;

import utilities.BotCommand;
import utilities.abstracts.AbstractBotError;

public class BotErrorPrivate extends AbstractBotError {
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   boolean isErrorOneLiner, Object[] replacements){
		super(commandInError, errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   Object[] replacements){
		super(commandInError, errorMessage, replacements);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   String errorEmoji, boolean isErrorOneLiner, Object[] replacements){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner,
				replacements);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   String errorEmoji, Object[] replacements){
		super(commandInError, errorMessage, errorEmoji, replacements);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage,
						   String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	public BotErrorPrivate(BotCommand commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	public BotErrorPrivate(String errorMessage, boolean isErrorOneLiner,
			Object[] replacements){
		super(errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotErrorPrivate(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	public BotErrorPrivate(String errorMessage, Object[] replacements){
		super(errorMessage, replacements);
	}
	
	public BotErrorPrivate(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner, Object[] replacements){
		super(errorMessage, errorEmoji, isErrorOneLiner, replacements);
	}
	
	public BotErrorPrivate(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotErrorPrivate(String errorMessage, String errorEmoji,
			Object[] replacements){
		super(errorMessage, errorEmoji, replacements);
	}
	
	public BotErrorPrivate(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	public BotErrorPrivate(String errorMessage){
		super(errorMessage);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoPrivateMessage(messageToSend, isErrorOneLiner);
	}
	
}
