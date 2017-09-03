package errorHandling;

import framework.Command;

public abstract class AbstractBotError extends Command {
	
	private String errorMessage;
	private String emoji;
	protected boolean isErrorOneLiner;
	
	public AbstractBotError(Command commandInError, String errorMessage){
		this(commandInError, errorMessage, EMOJI_RED_CROSS);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, isErrorOneLiner);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji){
		this(commandInError, errorMessage, errorEmoji, true);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		
		super(commandInError);
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
		action();
		
	}
	
	public AbstractBotError(String errorMessage){
		this(errorMessage, EMOJI_RED_CROSS);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner){
		this(errorMessage, EMOJI_RED_CROSS, isErrorOneLiner);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji){
		this(errorMessage, errorEmoji, true);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
	}
	
	@Override
	public void action(){
		
		String messageToSend = emoji + " ** " + errorMessage + " **";
		
		sendErrorMessage(messageToSend);
		
	}
	
	protected abstract void sendErrorMessage(String messageToSend);
	
}
