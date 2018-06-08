package utilities.music;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import utilities.BotCommand;

public class MusicPlayer {
	
	public final static int MAX_VOLUME = 20;
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final BotCommand command;
	
	public MusicPlayer(AudioPlayer audioPlayer, BotCommand command){
		this.audioPlayer = audioPlayer;
		this.command = command;
		
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
		return this.command.getGuild();
	}
	
	public BotCommand getCommand(){
		return this.command;
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
	
	public VoiceChannel getConnectedChannel(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	public boolean isConnectedToVoiceChannel(){
		return this.getConnectedChannel() != null;
	}
	
	public void closeConnection(){
		this.getGuild().getAudioManager().closeAudioConnection();
	}
	
}
