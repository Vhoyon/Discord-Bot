package errorHandling;

import framework.Command;

public abstract class AbstractBotError extends Command {
	
	private String errorMessage;
	private String emoji;
	protected boolean isErrorOneLiner;
	
	private String[] replacements;
	
	public AbstractBotError(Command commandInError, String errorMessage){
		this(commandInError, errorMessage, (String[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String... replacements){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		this(commandInError, errorMessage, isErrorOneLiner, (String[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner, String... replacements){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, isErrorOneLiner,
				replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji){
		this(commandInError, errorMessage, errorEmoji, (String[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, String... replacements){
		this(commandInError, errorMessage, errorEmoji, true, replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		this(commandInError, errorMessage, errorEmoji, isErrorOneLiner,
				(String[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner, String... replacements){
		
		super(commandInError);
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		this.replacements = replacements;
		
		action();
		
	}
	
	public AbstractBotError(String errorMessage){
		this(errorMessage, (String[])null);
	}
	
	public AbstractBotError(String errorMessage, String... replacements){
		this(errorMessage, EMOJI_RED_CROSS, replacements);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner){
		this(errorMessage, isErrorOneLiner, (String[])null);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner,
			String... replacements){
		this(errorMessage, EMOJI_RED_CROSS, isErrorOneLiner, replacements);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji){
		this(errorMessage, errorEmoji, (String[])null);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			String... replacements){
		this(errorMessage, errorEmoji, true, replacements);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		this(errorMessage, errorEmoji, isErrorOneLiner, (String[])null);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner, String... replacements){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		this.replacements = replacements;
		
	}
	
	@Override
	public void action(){
		
		String messageToSend = emoji + " ** "
				+ String.format(errorMessage, (Object[])replacements) + " **";
		
		sendErrorMessage(messageToSend);
		
	}
	
	protected abstract void sendErrorMessage(String messageToSend);
	
}
