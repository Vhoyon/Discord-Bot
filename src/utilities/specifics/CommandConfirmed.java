package utilities.specifics;

import utilities.BotCommand;
import utilities.interfaces.Commands;
import vendor.abstracts.AbstractCommandConfirmed;

public abstract class CommandConfirmed extends AbstractCommandConfirmed
		implements Commands {
	
	public CommandConfirmed(){}
	
	public CommandConfirmed(BotCommand inceptionCommand){
		super(inceptionCommand);
	}
	
	@Override
	protected void actionIfConfirmable(){
		sendInfoMessage(
				getConfMessage()
						+ "\n\n"
						+ lang("CommandConfirmedCustomAndConfirmMessage",
								buildVCommand(CONFIRM), buildVCommand(CANCEL)),
				false);
	}
	
	public void cancelled(){
		sendInfoMessage("*" + lang(true, "CommandConfirmedConfCancelled") + "*");
	}
	
}
