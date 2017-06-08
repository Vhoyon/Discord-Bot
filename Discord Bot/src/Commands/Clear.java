package Commands;

import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * 
 * @author Stephano
 *
 *Classe qui permet d'éffacer tout les messages dans le chat ou on l'invoque
 *
 *<b>NE FONCTIONNE PAS</b>
 */
public class Clear {

	public Clear(MessageReceivedEvent event) {
		List<VoiceChannel> voiceChanel = event.getAuthor().getJDA().getVoiceChannels();
		for (VoiceChannel voiceChannel : voiceChanel) {
			List<Member> members = voiceChannel.getMembers();
			for (Member member : members) {
			}
		}
		// textChannel.deleteMessages((Collection<Message>)
		// event.getTextChannel().getHistory().getRetrievedHistory().subList(0,
		// textChannel.getHistory().size() + 1)).complete();
	}

}
