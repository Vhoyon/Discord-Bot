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
		
		sendPrivateMessage(lang("Content"));
		sendInfoMessage(lang("HelpSentMessage"));
		
	}
	
}
