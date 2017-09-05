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
			
			sendInfoMessage(getConfMessage() + "\n\n"
					+ getString("CommandConfirmedCustomAndConfirmMessage"),
					false, new Object[]
					{
						buildVCommand(CONFIRM), buildVCommand(CANCEL)
					});
			
			getBuffer().push(this, BUFFER_CONFIRMATION);
			
		}
		
	}
	
	public abstract void confirmed();
	
	public void cancelled(){
		sendInfoMessage("*" + getString("CommandConfirmedConfCancelled") + "*");
	}
	
}
