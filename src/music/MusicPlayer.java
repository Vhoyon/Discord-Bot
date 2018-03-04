package music;

import net.dv8tion.jda.core.entities.Guild;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicPlayer {
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final Guild guild;
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild){
		this.audioPlayer = audioPlayer;
		this.guild = guild;
		
		listener = new AudioListener(this);
		audioPlayer.addListener(listener);
	}
	
	public AudioPlayer getAudioPlayer(){
		return this.audioPlayer;
	}
	
	public Guild getGuild(){
		return this.guild;
	}
	
	public AudioListener getListener(){
		return this.listener;
	}
	
	public AudioHandler getAudioHandler(){
		return new AudioHandler(this.audioPlayer);
	}
	
	public synchronized void playTrack(AudioTrack track){
		this.getListener().queue(track);
	}
	
	public synchronized void skipTrack(){
		this.getListener().nextTrack();
	}
	
}
