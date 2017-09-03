package commands;

import framework.Command;

public class SimpleTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	private boolean isPrivateMessage;
	
	private Object[] replacements;
	
	private SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			TextType textType, Object... replacements){
		
		this.isPrivateMessage = isPrivateMessage;
		this.textToSend = textToSend;
		this.textType = textType;
		this.replacements = replacements;
		
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend){
		this(isPrivateMessage, textToSend, (Object[])null);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			Object... replacements){
		this(isPrivateMessage, textToSend, TextType.SIMPLE, replacements);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			boolean isInfoOneLiner){
		this(isPrivateMessage, textToSend, isInfoOneLiner, (Object[])null);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			boolean isInfoOneLiner, Object... replacements){
		
		this(isPrivateMessage, textToSend, TextType.INFO_LINE, replacements);
		
		if(!isInfoOneLiner)
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	public SimpleTextCommand(String textToSend){
		this(textToSend, (Object[])null);
	}
	
	public SimpleTextCommand(String textToSend, Object... replacements){
		this(false, textToSend, replacements);
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner){
		this(textToSend, isInfoOneLiner, (Object[])null);
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner,
			Object... replacements){
		this(false, textToSend, isInfoOneLiner, replacements);
	}
	
	@Override
	public void action(){
		
		if(isPrivateMessage)
			switch(textType){
			case SIMPLE:
				sendPrivateMessage(textToSend, replacements);
				break;
			case INFO_LINE:
				sendInfoPrivateMessage(textToSend, replacements);
				break;
			case INFO_BLOCK:
				sendInfoPrivateMessage(textToSend, false, replacements);
				break;
			}
		else
			switch(textType){
			case SIMPLE:
				sendMessage(textToSend, replacements);
				break;
			case INFO_LINE:
				sendInfoMessage(textToSend, replacements);
				break;
			case INFO_BLOCK:
				sendInfoMessage(textToSend, false, replacements);
				break;
			}
		
	}
}
