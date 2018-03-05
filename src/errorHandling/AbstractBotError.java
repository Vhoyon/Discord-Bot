package errorHandling;

import ressources.Utils;
import framework.Command;

public abstract class AbstractBotError extends Command {
	
	private String errorMessage;
	private String emoji;
	protected boolean isErrorOneLiner;
	
	private Object[] replacements;
	
	public AbstractBotError(Command commandInError, String errorMessage){
		this(commandInError, errorMessage, (Object[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			Object[] replacements){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner){
		this(commandInError, errorMessage, isErrorOneLiner, (Object[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			boolean isErrorOneLiner, Object[] replacements){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, isErrorOneLiner,
				replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji){
		this(commandInError, errorMessage, errorEmoji, (Object[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, Object[] replacements){
		this(commandInError, errorMessage, errorEmoji, true, replacements);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner){
		this(commandInError, errorMessage, errorEmoji, isErrorOneLiner,
				(Object[])null);
	}
	
	public AbstractBotError(Command commandInError, String errorMessage,
			String errorEmoji, boolean isErrorOneLiner, Object[] replacements){
		
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
	
	public AbstractBotError(String errorMessage, Object[] replacements){
		this(errorMessage, EMOJI_RED_CROSS, replacements);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner){
		this(errorMessage, isErrorOneLiner, (Object[])null);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner,
			Object[] replacements){
		this(errorMessage, EMOJI_RED_CROSS, isErrorOneLiner, replacements);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji){
		this(errorMessage, errorEmoji, (Object[])null);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			Object[] replacements){
		this(errorMessage, errorEmoji, true, replacements);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		this(errorMessage, errorEmoji, isErrorOneLiner, (Object[])null);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner, Object[] replacements){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		this.replacements = replacements;
		
	}
	
	@Override
	public void action(){
		
		String messageToSend = emoji + " ** "
				+ Utils.format(errorMessage, replacements) + " **";
		
		sendErrorMessage(messageToSend);
		
	}
	
	protected abstract void sendErrorMessage(String messageToSend);
	
}
