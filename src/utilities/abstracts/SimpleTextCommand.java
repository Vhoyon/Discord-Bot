package utilities.abstracts;

import utilities.interfaces.Commands;
import utilities.interfaces.Resources;
import vendor.abstracts.AbstractTextCommand;

public abstract class SimpleTextCommand extends AbstractTextCommand implements
		Commands, Resources {
	
	@Override
	protected void sendMessageMethod(String textToSend, TextType textType){
		
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
