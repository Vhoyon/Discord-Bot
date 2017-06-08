package Commands;

import java.util.ArrayList;

import net.dv8tion.jda.core.audio.AudioPacket;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * 
 * @author Stephano
 *
 *Classe qui contiens tout les methodes qui font la getion de l'audio
 *
 *<b>NE FONCTIONNE PAS</b>
 */
public class AudioCommands {
	private MessageReceivedEvent events;
	public AudioCommands(MessageReceivedEvent event) {
		events = event;
	}

}
