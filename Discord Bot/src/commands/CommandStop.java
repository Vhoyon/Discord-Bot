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
					
					new BotError(this, "No command with the name `"
							+ getContent()
							+ "` was found, no action was taken.");
					
				}
				else{
					
					if(commandToStop.stopAction())
						sendInfoMessage("The `"
								+ getContent()
								+ "` command was successfully stopped. :ok_hand:");
					else{
						sendInfoMessage("The `"
								+ getContent()
								+ "` command refused to stop. Ooo, scary. (It probably just wasn't implemented...)");
					}
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage("I CAN'T STOP");
		}
		
	}
	
}
