package commands;

import framework.Command;

/**
 * 
 * @author Stephano
 *
 * <br>
 * <br>
 *         Classe qui envois un message a l'utilisateur qui demande de l'aide
 *         avec une liste complète des commandes
 *
 */

public class CommandHelp extends Command {
	
	@Override
	public void action(){
		
		sendPrivateMessage(getStringEz("Content"));
		sendInfoMessage(getStringEz("HelpSentMessage"));
		
	}
	
}
