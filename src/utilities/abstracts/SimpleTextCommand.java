package utilities.abstracts;

public abstract class SimpleTextCommand extends AbstractTextCommand {
	
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
