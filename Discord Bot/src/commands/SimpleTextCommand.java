package commands;

import framework.Command;

public class SimpleTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	private boolean isPrivateMessage;
	
	private SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			TextType textType){
		
		this.isPrivateMessage = isPrivateMessage;
		this.textToSend = textToSend;
		this.textType = textType;
		
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend){
		this(isPrivateMessage, textToSend, TextType.SIMPLE);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			boolean isInfoOneLiner){
		
		this(isPrivateMessage, textToSend, TextType.INFO_LINE);
		
		if(!isInfoOneLiner)
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	public SimpleTextCommand(String textToSend){
		this(false, textToSend);
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner){
		this(false, textToSend, isInfoOneLiner);
	}
	
	@Override
	public void action(){
		
		if(isPrivateMessage)
			switch(textType){
			case SIMPLE:
				sendPrivateMessage(textToSend);
				break;
			case INFO_LINE:
				sendInfoPrivateMessage(textToSend);
				break;
			case INFO_BLOCK:
				sendInfoPrivateMessage(textToSend, false);
				break;
			}
		else
			switch(textType){
			case SIMPLE:
				sendMessage(textToSend);
				break;
			case INFO_LINE:
				sendInfoMessage(textToSend);
				break;
			case INFO_BLOCK:
				sendInfoMessage(textToSend, false);
				break;
			}
		
	}
}
