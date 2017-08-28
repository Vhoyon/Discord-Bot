package commands;

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
		
		sendPrivateMessage("Help is here");
		sendMessage("\\~\\~*Help sent!*\\~\\~");
		
	}
	
}
