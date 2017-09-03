package errorHandling;

import framework.Command;

public class BotError extends AbstractBotError {
	
	public BotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner, String... replacements){
		super(commandInError, errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		super(commandInError, errorMessage, isErrorOneLiner);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner, String... replacements){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner,
				replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		super(commandInError, errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji, String... replacements){
		super(commandInError, errorMessage, errorEmoji, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String... replacements){
		super(commandInError, errorMessage, replacements);
	}
	
	public BotError(Command commandInError, String errorMessage,
			String errorEmoji){
		super(commandInError, errorMessage, errorEmoji);
	}
	
	public BotError(Command commandInError, String errorMessage){
		super(commandInError, errorMessage);
	}
	
	public BotError(String errorMessage, boolean isErrorOneLiner,
			String... replacements){
		super(errorMessage, isErrorOneLiner, replacements);
	}
	
	public BotError(String errorMessage, boolean isErrorOneLiner){
		super(errorMessage, isErrorOneLiner);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner, String... replacements){
		super(errorMessage, errorEmoji, isErrorOneLiner, replacements);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		super(errorMessage, errorEmoji, isErrorOneLiner);
	}
	
	public BotError(String errorMessage, String errorEmoji,
			String... replacements){
		super(errorMessage, errorEmoji, replacements);
	}
	
	public BotError(String errorMessage, String... replacements){
		super(errorMessage, replacements);
	}
	
	public BotError(String errorMessage, String errorEmoji){
		super(errorMessage, errorEmoji);
	}
	
	public BotError(String errorMessage){
		super(errorMessage);
	}
	
	@Override
	protected void sendErrorMessage(String messageToSend){
		sendInfoMessage(messageToSend, isErrorOneLiner);
	}
	
}
