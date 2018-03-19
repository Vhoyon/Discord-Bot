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
				
				getMemory(BUFFER_SPAM);
				remember(false, BUFFER_SPAM);
				sendInfoMessage(lang("SavedFromSpam", EMOJI_OK_HAND));
				
			}
			else{
				
				if(commandToStop == null){
					
					new BotError(this, lang("NoCommandToStopMessage",
							getContent()));
					
				}
				else{
					
					if(commandToStop.stopAction())
						sendInfoMessage(lang("CommandFullyStoppedMessage",
								getContent(), EMOJI_OK_HAND));
					else{
						new BotError(this, lang("CommandNotStoppedMessage",
								getContent()), true);
					}
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage(lang("BotCantStop"));
		}
		
	}
}
