package commands;

import net.dv8tion.jda.core.entities.PrivateChannel;

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
		
		PrivateChannel channel = getEvent().getAuthor().openPrivateChannel()
				.complete();
		if(getEvent().getAuthor().hasPrivateChannel()){
			channel.sendMessage("Help is here").complete();
		}
		
	}
	
}
