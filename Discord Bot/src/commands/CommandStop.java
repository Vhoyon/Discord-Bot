package commands;

import errorHandling.BotError;
import framework.Command;

public class CommandStop extends Command {
	
	private Command commandToStop;
	
	public CommandStop(Command commandToStop){
		this.commandToStop = commandToStop;
	}
	
	@Override
	public void action(){
		
		try{
			
			if(getContent() == null){
				
				getBuffer().get(BUFFER_SPAM);
				getBuffer().push(false, BUFFER_SPAM);
				sendInfoMessage(getStringEz("SavedFromSpam"), EMOJI_OK_HAND);
				
			}
			else{
				
				if(commandToStop == null){
					
					new BotError(this, getStringEz("NoCommandToStopMessage"),
							new String[]
							{
								getContent()
							});
					
				}
				else{
					
					if(commandToStop.stopAction())
						sendInfoMessage(
								getStringEz("CommandFullyStoppedMessage"),
								getContent(), EMOJI_OK_HAND);
					else{
						sendInfoMessage(
								getStringEz("CommandNotStoppedMessage"),
								getContent());
					}
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage(getStringEz("BotCantStop"));
		}
		
	}
	
}
