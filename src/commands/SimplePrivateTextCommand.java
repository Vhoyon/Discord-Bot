package commands;

import utilities.Command;

public class SimplePrivateTextCommand extends Command {
	
	private enum TextType{
		SIMPLE, INFO_LINE, INFO_BLOCK
	}
	
	private TextType textType;
	private String textToSend;
	
	private String[] replacements;
	
	private SimplePrivateTextCommand(String textToSend, TextType textType,
			String... replacements){
		
		this.textToSend = textToSend;
		this.textType = textType;
		this.replacements = replacements;
		
	}
	
	public SimplePrivateTextCommand(String textToSend, boolean isInfoOneLiner,
			String... replacements){
		
		this(textToSend, TextType.INFO_LINE, replacements);
		
		if(!isInfoOneLiner)
			this.textType = TextType.INFO_BLOCK;
		
	}
	
	public SimplePrivateTextCommand(String textToSend, boolean isInfoOneLiner){
		this(textToSend, isInfoOneLiner, (String[])null);
	}
	
	public SimplePrivateTextCommand(String textToSend, String... replacements){
		this(textToSend, TextType.SIMPLE, replacements);
	}
	
	public SimplePrivateTextCommand(String textToSend){
		this(textToSend, (String[])null);
	}
	
	@Override
	public void action(){
		
		switch(textType){
		case SIMPLE:
			sendPrivateMessage(String
					.format(textToSend, (Object[])replacements));
			break;
		case INFO_LINE:
			sendInfoPrivateMessage(format(textToSend,
					(Object[])replacements));
			break;
		case INFO_BLOCK:
			sendInfoPrivateMessage(
					String.format(textToSend, (Object[])replacements), false);
			break;
		}
		
	}
}
