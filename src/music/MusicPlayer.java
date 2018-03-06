package music;

import net.dv8tion.jda.core.entities.Guild;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicPlayer {
	
	public final static int MAX_VOLUME = 20;
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final Guild guild;
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild){
		this.audioPlayer = audioPlayer;
		this.guild = guild;
		
		// Defaults volume to 75% of our maxed value (20) by default
		this.audioPlayer.setVolume(15);
		
		listener = new AudioListener(this);
		audioPlayer.addListener(listener);
	}
	
	public AudioPlayer getAudioPlayer(){
		return this.audioPlayer;
	}
	
	public AudioListener getListener(){
		return this.listener;
	}
	
	public Guild getGuild(){
		return this.guild;
	}
	
	public AudioHandler getAudioHandler(){
		return new AudioHandler(this.audioPlayer);
	}
	
	public synchronized void playTrack(AudioTrack track){
		this.getListener().queue(track);
	}
	
	public synchronized void togglePause(){
		this.setPause(!this.getAudioPlayer().isPaused());
	}
	
	public synchronized void setPause(boolean isPaused){
		this.getAudioPlayer().setPaused(isPaused);
	}
	
	public synchronized boolean isPaused(){
		return this.getAudioPlayer().isPaused();
	}
	
	public synchronized boolean skipTrack(){
		return this.getListener().nextTrack();
	}
	
	public synchronized int getNumberOfTracks(){
		return this.getListener().getTracks().size();
	}
	
}
