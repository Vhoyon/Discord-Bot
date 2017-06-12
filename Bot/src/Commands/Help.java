package Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * 
 * @author Stephano
 *
 *<br><br>
 *Classe qui envois un message a l'utilisateur qui demande de l'aide avec une liste complète des commandes
 *
 */

public class Help {

	public Help(MessageReceivedEvent event) {
		event.getAuthor().openPrivateChannel();
		if (event.getAuthor().hasPrivateChannel()) {
			event.getAuthor().getPrivateChannel().sendMessage("Help is here").complete();
		}
	}

}
