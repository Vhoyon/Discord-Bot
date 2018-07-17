package utilities.music;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import utilities.BotCommand;
import vendor.exceptions.BadParameterException;
import vendor.modules.Environment;
import vendor.modules.Logger;

public class MusicPlayer {
	
	public final static int MAX_VOLUME = 20;
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final BotCommand command;
	
	public MusicPlayer(AudioPlayer audioPlayer, BotCommand command){
		this.audioPlayer = audioPlayer;
		this.command = command;
		
		int defaultVolume = command.setting("volume");
		
		try{
			this.setVolume(defaultVolume);
		}
		catch(BadParameterException e){
			Logger.log(e + " Your environment variable is not correctly set...");
		}
		
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
	
	public void setVolume(int volume) throws BadParameterException{
		
		if(volume < 0 || volume > 100){
			throw new BadParameterException("Volume must be between 0 and 100.");
		}
		else{
			
			this.getAudioPlayer().setVolume(volume / (100 / MAX_VOLUME));
			
		}
		
	}
	
}
