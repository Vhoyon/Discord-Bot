package commands;

import utilities.BotCommand;
import utilities.specifics.CommandConfirmed;
import utilities.specifics.CommandsThreadManager;
import errorHandling.BotError;

public class CommandStop extends BotCommand {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			
			BotCommand commandToStop = CommandsThreadManager
					.getLatestRunningCommandExcept(this, getKey());
			
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
			
			BotCommand commandToStop = CommandsThreadManager.getCommandRunning(
					getContent(), getKey(), getRouter());
			
			if(commandToStop == null){
				new BotError(this, lang("NoCommandToStopMessage", getContent()));
			}
			else{
				stopCommandLogic(commandToStop);
			}
			
		}
		
	}
	
	private void stopCommandLogic(BotCommand commandToStop){
		
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
