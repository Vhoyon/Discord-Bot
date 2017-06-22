package Commands;

import net.dv8tion.jda.core.entities.PrivateChannel;
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
		PrivateChannel channel =  event.getAuthor().openPrivateChannel().complete();
		if (event.getAuthor().hasPrivateChannel()) {
			channel.sendMessage("Help is here").complete();
		}
	}

}
