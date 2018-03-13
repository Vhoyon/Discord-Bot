package commands;

import utilities.Command;

public class SimpleTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	
	private String[] replacements;
	
	private SimpleTextCommand(String textToSend, TextType textType,
			String... replacements){
		
		this.textToSend = textToSend;
		this.textType = textType;
		this.replacements = replacements;
		
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner,
			String... replacements){
		
		this(textToSend, TextType.INFO_LINE, replacements);
		
		if(!isInfoOneLiner)
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	public SimpleTextCommand(String textToSend, boolean isInfoOneLiner){
		this(textToSend, isInfoOneLiner, (String[])null);
	}
	
	public SimpleTextCommand(String textToSend, String... replacements){
		this(textToSend, TextType.SIMPLE, replacements);
	}
	
	public SimpleTextCommand(String textToSend){
		this(textToSend, (String[])null);
	}
	
	@Override
	public void action(){
		
		switch(textType){
		case SIMPLE:
			sendMessage(String.format(textToSend, (Object[])replacements));
			break;
		case INFO_LINE:
			sendInfoMessage(String.format(textToSend, (Object[])replacements));
			break;
		case INFO_BLOCK:
			sendInfoMessage(String.format(textToSend, (Object[])replacements),
					false);
			break;
		}
		
	}
}
