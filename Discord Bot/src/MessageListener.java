import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Commands.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * 
 * @author Stephano
 *
 *         <br>
 * 		<br>
 * 		Cette classe extend <b>ListenerAdapter</b> recoit les commandes de
 *         l'utilisateur et appele les classes necessaires
 */
public class MessageListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		AudioCommands audio = new AudioCommands(event);
		Message msg = event.getMessage();
		String messageRecu = event.getMessage().getContent();
		TextChannel textChannel = event.getTextChannel();
		VoiceChannelInteraction voiceChannels = new VoiceChannelInteraction(event);
		if (messageRecu.contains("!")) {
			if (messageRecu.equalsIgnoreCase("!hello")) {
				textChannel.sendMessage("hello " + event.getAuthor().getName()).complete();
			} else if (messageRecu.equalsIgnoreCase("!help")) {
				Help help = new Help(event);
			} else if (messageRecu.equalsIgnoreCase("!connect")) {
				voiceChannels.JoinVoiceChannel();
			} else if (messageRecu.equalsIgnoreCase("!disconnect")) {
				voiceChannels.LeaveVoiceChannel();
			} else if (messageRecu.contains("!play ")) {
				audio.play();
			} else if (messageRecu.equalsIgnoreCase("!clear")) {
				Clear clear = new Clear(event);

			}
		}
	}
}