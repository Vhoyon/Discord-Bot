package Commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

/**
 * 
 * @author Stephano
 *
 *         Classe qui permet d'éffacer tout les messages dans le chat ou on
 *         l'invoque
 *
 */
public class Clear {

	public Clear(MessageReceivedEvent event) {
		boolean vide = false;
		MessageHistory hist = event.getTextChannel().getHistory();
		do {
			List<Message> messages = hist.retrievePast(100).complete();
			if (!messages.isEmpty()) {
				event.getTextChannel().deleteMessages(messages).complete();
			} else {
				vide = true;
			}
		} while (!vide);

	}

}
