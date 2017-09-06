package commands;

import framework.Command;

public class SimpleTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	private boolean isPrivateMessage;
	
	private String[] replacements;
	
	private SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			TextType textType, String... replacements){
		
		this.isPrivateMessage = isPrivateMessage;
		this.textToSend = textToSend;
		this.textType = textType;
		this.replacements = replacements;
		
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend){
		this(isPrivateMessage, textToSend, (String[])null);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			String... replacements){
		this(isPrivateMessage, textToSend, TextType.SIMPLE, replacements);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			boolean isInfoOneLiner){
		this(isPrivateMessage, textToSend, isInfoOneLiner, (String[])null);
	}
	
	public SimpleTextCommand(boolean isPrivateMessage, String textToSend,
			boolean isInfoOneLiner, String... replacements){
		
		this(isPrivateMessage, textToSend, TextType.INFO_LINE, replacements);
		
		if(!isInfoOneLiner)
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	public SimpleTextCommand(String textToSend){
		this(textToSend, (String[])null);
	}
	
	public SimpleTextCommand(String textToSend, String... replacements){
		this(false, textToSend, replacements);
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner){
		this(textToSend, isInfoOneLiner, (String[])null);
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner,
			String... replacements){
		this(false, textToSend, isInfoOneLiner, replacements);
	}
	
	@Override
	public void action(){
		
		if(isPrivateMessage)
			switch(textType){
			case SIMPLE:
				sendPrivateMessage(textToSend, (Object[])replacements);
				break;
			case INFO_LINE:
				sendInfoPrivateMessage(textToSend, (Object[])replacements);
				break;
			case INFO_BLOCK:
				sendInfoPrivateMessage(textToSend, false,
						(Object[])replacements);
				break;
			}
		else
			switch(textType){
			case SIMPLE:
				sendMessage(textToSend, (Object[])replacements);
				break;
			case INFO_LINE:
				sendInfoMessage(textToSend, (Object[])replacements);
				break;
			case INFO_BLOCK:
				sendInfoMessage(textToSend, false, (Object[])replacements);
				break;
			}
		
	}
}
