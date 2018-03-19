package utilities.specifics;

import utilities.Command;

public abstract class CommandConfirmed extends Command {
	
	private Command inceptionCommand = null;
	
	public CommandConfirmed(){}
	
	public CommandConfirmed(Command inceptionCommand){
		super(inceptionCommand);
		
		this.inceptionCommand = inceptionCommand;
		
		action();
	}
	
	public abstract String getConfMessage();
	
	@Override
	public void action(){
		
		if(!hasMemory(BUFFER_CONFIRMATION)){
			//			getMemory(BUFFER_CONFIRMATION);
			//		}
			//		else{
			
			sendInfoMessage(
					getConfMessage()
							+ "\n\n"
							+ lang(true,
									"CommandConfirmedCustomAndConfirmMessage",
									buildVCommand(CONFIRM),
									buildVCommand(CANCEL)), false);
			
			getBuffer().push(this, BUFFER_CONFIRMATION);
			
		}
		
	}
	
	public abstract void confirmed();
	
	public void cancelled(){
		sendInfoMessage("*" + lang(true, "CommandConfirmedConfCancelled") + "*");
	}
	
	@Override
	public String lang(String shortKey){
		if(inceptionCommand != null){
			return inceptionCommand.lang(shortKey);
		}
		return super.lang(shortKey);
	}
	
	@Override
	public String lang(String shortKey, Object... replacements){
		if(inceptionCommand != null){
			return inceptionCommand.lang(shortKey, replacements);
		}
		return super.lang(shortKey, replacements);
	}
	
}
