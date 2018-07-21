package commands;

import errorHandling.BotError;
import utilities.BotCommand;
import utilities.specifics.CommandConfirmed;
import vendor.utilities.CommandsThreadManager;

public class CommandStop extends BotCommand {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			
			BotCommand commandToStop = (BotCommand)CommandsThreadManager
					.getLatestRunningCommandExcept(this, getKey());
			
			if(commandToStop == null){
				sendMessage(lang("BotCantStop"));
			}
			else{
				
				boolean isConfirming = setting("confirm_stop");
				
				if(!isConfirming){
					stopCommandLogic(commandToStop);
				}
				else{
					new CommandConfirmed(this){
						@Override
						public String getConfMessage(){
							return "Please confirm that you want to stop the "
									+ buildVCommand(commandToStop
											.getCommandName()) + " command.";
						}
						
						@Override
						public void confirmed(){
							stopCommandLogic(commandToStop);
						}
					};
				}
				
			}
			
		}
		else{
			
			BotCommand commandToStop = (BotCommand)CommandsThreadManager
					.getCommandRunning(getContent(), getEventDigger(),
							getRouter());
			
			if(commandToStop == null){
				new BotError(this, lang("NoCommandToStopMessage",
						code(getContent())));
			}
			else{
				stopCommandLogic(commandToStop);
			}
			
		}
		
	}
	
	private void stopCommandLogic(BotCommand commandToStop){

		if(!commandToStop.kill())
			sendInfoMessage(lang("CommandFullyStoppedMessage",
					code(commandToStop.getCommandName()), EMOJI_OK_HAND));
		else{
			new BotError(this, lang("CommandNotStoppedMessage",
					code(commandToStop.getCommandName())), true);
		}
		
	}
	
	@Override
	public Object getCalls(){
		return STOP;
	}
	
	@Override
	public String getCommandDescription(){
		return "This command stops the specified command";
	}
}
