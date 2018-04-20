package commands;

import utilities.Command;
import utilities.specifics.CommandConfirmed;
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
				
				new CommandConfirmed(this){
					@Override
					public String getConfMessage(){
						return "Please confirm that you want to stop the "
								+ buildVCommand(commandToStop.getCommandName())
								+ " command.";
					}
					
					@Override
					public void confirmed(){
						stopCommandLogic(commandToStop);
					}
				};
				
			}
			
		}
		else{
			
			Command commandToStop = CommandsThreadManager.getCommandRunning(
					getContent(), getId(), getRouter());
			
			if(commandToStop == null){
				new BotError(this, lang("NoCommandToStopMessage", getContent()));
			}
			else{
				stopCommandLogic(commandToStop);
			}
			
		}
		
	}
	
	private void stopCommandLogic(Command commandToStop){
		
		if(commandToStop.stopAction())
			sendInfoMessage(lang("CommandFullyStoppedMessage",
					commandToStop.getCommandName(), EMOJI_OK_HAND));
		else{
			new BotError(this, lang("CommandNotStoppedMessage",
					commandToStop.getCommandName()), true);
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			STOP
		};
	}

	@Override
	public String getCommandDescription() {
		return "This command stops the specified command";
	}
}
