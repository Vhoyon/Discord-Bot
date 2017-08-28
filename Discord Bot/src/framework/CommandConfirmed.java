package framework;

import ressources.Ressources;

public abstract class CommandConfirmed extends Command implements Ressources {
	
	private String confirmationMessage;
	
	public CommandConfirmed(String confirmationMessage){
		
		this.confirmationMessage = confirmationMessage;
		
	}
	
	@Override
	public void action(){
		
		try{
			getBuffer().get(BUFFER_CONFIRMATION);
		}
		catch(IndexOutOfBoundsException e){
			
			sendMessage(confirmationMessage
					+ "\n\nEnter `"
					+ Ressources.buildCommand("confirm")
					+ "` to confirm. Entering `"
					+ Ressources.buildCommand("cancel")
					+ "` will cancel the confirmation.\nAny other command will cancel the confirmation and execute the inputted command.");
			
			getBuffer().push(this, BUFFER_CONFIRMATION);
			
		}
		
	}
	
	public abstract void confirmed();
	
	public void cancelled(){
		sendMessage("\\~\\~*Confirmation cancelled*\\~\\~");
	}
	
}
