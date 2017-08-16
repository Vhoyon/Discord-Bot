package commands;

public class SimpleTextCommand extends Command {
	
	private String textToSend;
	
	public SimpleTextCommand(String textToSend){
		this.textToSend = textToSend;
	}
	
	@Override
	public void action(){
		
		sendMessage(textToSend);
		
	}
	
}
