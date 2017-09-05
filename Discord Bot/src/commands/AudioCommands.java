package commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

import net.dv8tion.jda.core.audio.AudioConnection;
import net.dv8tion.jda.core.audio.AudioPacket;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import framework.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.impl.AudioManagerImpl;

/**
 * 
 * @author Stephano
 *
 *         Classe qui contiens tout les methodes qui font la getion de l'audio
 *
 *         <b>NE FONCTIONNE PAS</b>
 */
public class AudioCommands extends Command {

	@Override
	public void action() {
		
//		AudioInputStream audio = new AudioInputStream(getContent(), "mp3" );
	}

	

}
