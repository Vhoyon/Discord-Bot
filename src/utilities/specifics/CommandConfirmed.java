package utilities.specifics;

import utilities.BotCommand;
import utilities.interfaces.Commands;
import utilities.interfaces.Resources;
import vendor.abstracts.AbstractBotCommand;
import vendor.abstracts.AbstractCommandConfirmed;

/**
 * Vhoyon's implementation of the {@link AbstractCommandConfirmed} to generalize how cancellations and pre-confirmations are handled.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class CommandConfirmed extends AbstractCommandConfirmed
		implements Commands, Resources {

	/**
	 * Creates a bare CommandConfirmed to allow for greater flexibility in determining when to ask for confirmation.
	 * 
	 * <p>
	 *     This usage does not get the context from the current request, so you need to add it in some way (<i>e.g. : using {@link #putStateFromCommand(AbstractBotCommand)}</i>), and calling {@link #action()} too.
	 * </p>
	 * 
	 * @since v0.4.0
	 */
	public CommandConfirmed(){
		super();
	}

	/**
	 * Creates a CommandConfirmed for the command passed in parameter. This will immediately trigger the action of the confirmation and wait for a call on this object's {@link #confirmed()} before actually doing the action of the command passed in parameter.
	 * <p>
	 *     In the case were a call on this object's {@link #cancelled()} is made, a message in the context of the command passed in the parameter will be sent that the confirmation was cancelled.
	 * </p>
	 * 
	 * @param commandToConfirm The command to require a confirmation before its execution
	 * 
	 * @since v0.4.0
	 */
	public CommandConfirmed(BotCommand commandToConfirm){
		super(commandToConfirm);
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
	
	@Override
	public void cancelled(){
		sendInfoMessage(ital(lang(true, "CommandConfirmedConfCancelled")));
	}
	
}
