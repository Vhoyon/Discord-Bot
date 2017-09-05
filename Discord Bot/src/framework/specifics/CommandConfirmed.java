package framework.specifics;

import framework.Command;
import ressources.Ressources;

public abstract class CommandConfirmed extends Command implements Ressources {
	
	public abstract String getConfMessage();
	
	@Override
	public void action(){
		
		try{
			getBuffer().get(BUFFER_CONFIRMATION);
		}
		catch(NullPointerException e){
			
			sendInfoMessage(
					getConfMessage()
							+ "\n\nEnter "
							+ buildVCommand(CONFIRM)
							+ " to confirm. Entering "
							+ buildVCommand(CANCEL)
							+ " will cancel the confirmation.\nAny other command will cancel the confirmation and execute the inputted command.",
					false);
			
			getBuffer().push(this, BUFFER_CONFIRMATION);
			
		}
		
	}
	
	public abstract void confirmed();
	
	public void cancelled(){
		sendInfoMessage("*Confirmation cancelled*");
	}
	
}
