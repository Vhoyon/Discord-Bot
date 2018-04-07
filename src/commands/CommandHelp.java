package commands;

import utilities.Command;

/**
 * Classe qui envois un message a l'utilisateur qui demande de l'aide avec une
 * liste complète des commandes.
 * 
 * @author Stephano
 */

public class CommandHelp extends Command {
	
	@Override
	public void action(){

		String test = getRouter().getCommandsRepo().getFullHelpString();
		
		sendPrivateMessage(test);
		sendInfoMessage(lang("HelpSentMessage"));
		
	}
	
}
