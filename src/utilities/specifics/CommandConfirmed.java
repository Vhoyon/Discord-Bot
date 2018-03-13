package utilities.specifics;

import utilities.Command;

public abstract class CommandConfirmed extends Command {
	
	public CommandConfirmed(){}
	
	public CommandConfirmed(Command inceptionCommand){
		super(inceptionCommand);
		
		action();
	}
	
	public abstract String getConfMessage();
	
	@Override
	public void action(){
		
		try{
			getBuffer().get(BUFFER_CONFIRMATION);
		}
		catch(NullPointerException e){
			
			sendInfoMessage(
					getConfMessage()
							+ "\n\n"
							+ getString(
									"CommandConfirmedCustomAndConfirmMessage",
									buildVCommand(CONFIRM),
									buildVCommand(CANCEL)), false);
			
			getBuffer().push(this, BUFFER_CONFIRMATION);
			
		}
		
	}
	
	public abstract void confirmed();
	
	public void cancelled(){
		sendInfoMessage("*" + getString("CommandConfirmedConfCancelled") + "*");
	}
	
}
