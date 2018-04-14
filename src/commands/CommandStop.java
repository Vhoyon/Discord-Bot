package commands;

import utilities.Command;
import utilities.specifics.CommandsThreadManager;
import errorHandling.BotError;

public class CommandStop extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			
			Command commandToStop = CommandsThreadManager
					.getLatestRunningCommandExcept(this, getId());
			
			if(commandToStop == null){
				sendMessage(lang("BotCantStop"));
			}
			else{
				
				boolean stopSuccess = commandToStop.stopAction();
				
				if(stopSuccess){
					
					sendInfoMessage("Command "
							+ buildVCommand(commandToStop.getCommandName())
							+ " successfully stopped!");
					
				}
				else{
					
					new BotError(this, "The command "
							+ buildVCommand(commandToStop.getCommandName())
							+ " did not successfully stopped!");
					
				}
				
			}
			
		}
		else{
			
			Command commandToStop = CommandsThreadManager.getCommandRunning(
					getContent(), getId(), getRouter());
			
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
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			STOP
		};
	}
	
}
