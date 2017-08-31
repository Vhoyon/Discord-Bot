package commands;

import framework.Command;

public class SimpleTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	
	public SimpleTextCommand(String textToSend){
		this.textToSend = textToSend;
		this.textType = TextType.SIMPLE;
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner){
		this.textToSend = textToSend;
		
		if(isInfoOneLiner)
			this.textType = TextType.INFO_LINE;
		else
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	@Override
	public void action(){
		
		switch(textType){
		case SIMPLE: sendMessage(textToSend);
			break;
		case INFO_LINE: sendInfoMessage(textToSend);
			break;
		case INFO_BLOCK: sendInfoMessage(textToSend, false);
			break;
		}
		
	}
	
}
