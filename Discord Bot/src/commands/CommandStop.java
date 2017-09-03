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
				sendInfoMessage("You have been saved from the spam "
						+ EMOJI_OK_HAND);
				
			}
			else{
				
				if(commandToStop == null){
					
					new BotError(
							this,
							"No command with the name `%s` was found, no action was taken.",
							getContent());
					
				}
				else{
					
					if(commandToStop.stopAction())
						sendInfoMessage(
								"The `%s` command was successfully stopped. :ok_hand:",
								getContent());
					else{
						sendInfoMessage(
								"The `%s` command refused to stop. Ooo, scary. (It probably just wasn't implemented...)",
								getContent());
					}
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage("I CAN'T STOP");
		}
		
	}
	
}
