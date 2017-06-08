package Commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildManager;

/**
 * 
 * @author Stephano
 *<br><br>
 *
 *Classe qui permet au bot de se connecter et de se deconnecter au serveur local ou se trouve l'utilisateur
 *
 *
 */

public class VoiceChannelInteraction {
	
	public MessageReceivedEvent event;

	public VoiceChannelInteraction(MessageReceivedEvent event) {
		this.event = event;
	}

	public void JoinVoiceChannel() {
		VoiceChannel vc = null;
		GuildManager gm = null;
		for (VoiceChannel channel : event.getGuild().getVoiceChannels()) {
			vc = channel;
			for (Member usr : vc.getMembers()) {
				if (usr.getEffectiveName().equals(event.getAuthor().getName())) {
					gm = new GuildManager(event.getGuild());
					ChannelManager cm = vc.getManager();
					AudioManager man = event.getGuild().getAudioManager();
					man.openAudioConnection(vc);
					break;
				}
			}

		}
	}

	public void LeaveVoiceChannel() {
		for (VoiceChannel channel : event.getGuild().getVoiceChannels()) {
			
			for (Member usr : channel.getMembers()) {
				if (usr.getEffectiveName().equalsIgnoreCase("bot")) {
					AudioManager man = event.getGuild().getAudioManager();
					man.closeAudioConnection();
					event.getTextChannel().sendMessage("The bot has disconnected").complete();
					break;
				}else{
					event.getTextChannel().sendMessage("The bot can not be disconnected if it is not in a voice channel.").complete();
				}
			}

		}
	}

}
