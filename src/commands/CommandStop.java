package commands;

import utilities.Command;
import utilities.specifics.CommandsThreadManager;
import errorHandling.BotError;

public class CommandStop extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			
			try{
				
				getMemory(BUFFER_SPAM);
				
				remember(false, BUFFER_SPAM);
				
				sendInfoMessage(lang("SavedFromSpam", EMOJI_OK_HAND));
				
			}
			catch(NullPointerException e){
				sendMessage(lang("BotCantStop"));
			}
			
		}
		else{
			
			Command commandToStop = CommandsThreadManager.getCommandRunning(
					getContent(), getGuildId(), getRouter());
			
			if(commandToStop == null){
				
				new BotError(this, lang("NoCommandToStopMessage", getContent()));
				
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
}
