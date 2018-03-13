package commands;

import utilities.Command;
import errorHandling.BotError;

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
				sendInfoMessage(getStringEz("SavedFromSpam", EMOJI_OK_HAND));
				
			}
			else{
				
				if(commandToStop == null){
					
					new BotError(this, getStringEz("NoCommandToStopMessage",
							getContent()));
					
				}
				else{
					
					if(commandToStop.stopAction())
						sendInfoMessage(getStringEz(
								"CommandFullyStoppedMessage", getContent(),
								EMOJI_OK_HAND));
					else{
						new BotError(this, getStringEz(
								"CommandNotStoppedMessage", getContent()), true);
					}
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage(getStringEz("BotCantStop"));
		}
		
	}
}
