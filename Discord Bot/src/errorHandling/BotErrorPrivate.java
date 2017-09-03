package errorHandling;

import framework.Command;

public class BotErrorPrivate extends AbstractBotError {
	
	public BotErrorPrivate(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	public BotErrorPrivate(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotErrorPrivate(Command commandInError, String errorMessage,
			String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	public BotErrorPrivate(Command commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	public BotErrorPrivate(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	public BotErrorPrivate(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
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
