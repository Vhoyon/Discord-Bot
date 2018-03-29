package commands;

import utilities.specifics.AbstractTextCommand;

public abstract class SimplePrivateTextCommand extends AbstractTextCommand {
	
	@Override
	protected void sendMessageMethod(String textToSend, TextType textType){
		
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
		
	}
	
}
