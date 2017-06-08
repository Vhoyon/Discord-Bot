package Commands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildManager;

/**
 * 
 * @author Stephano
 *<br><br>
 *
 *Classe qui permet au bot de se connecter au serveur local ou se trouve l'utilisateur
 *
 *<b>NE FONCTIONNE PAS</b>
 */

public class JoinVoiceChannel {

	public JoinVoiceChannel(MessageReceivedEvent event) {
		VoiceChannel vc = null;
		GuildManager gm = null;
		for (VoiceChannel channel : event.getGuild().getVoiceChannels()) {
			vc = channel;
			for (Member usr : vc.getMembers()) {
				if (usr.getEffectiveName().equals(event.getAuthor().getName())) {
					gm = new GuildManager(event.getGuild());
					ChannelManager cm = vc.getManager();
					break;
				}
			}

		}
	}

}
