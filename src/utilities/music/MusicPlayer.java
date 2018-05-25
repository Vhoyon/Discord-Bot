package utilities.music;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import vendor.exceptions.BadParameterException;
import vendor.modules.Environment;
import vendor.modules.Logger;

public class MusicPlayer {
	
	public final static int MAX_VOLUME = 20;
	public final static int DEFAULT_VOLUME= 75;

	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final Guild guild;
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild){
		this.audioPlayer = audioPlayer;
		this.guild = guild;

//		 Defaults volume to 75% of our maxed value (20) by default
		int defaultVolume  = Environment.getVar("DEFAULT_VOLUME",75);

		try {
			this.setVolume(defaultVolume);
		} catch (BadParameterException e) {
			Logger.log(e);
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
        	setVolume(DEFAULT_VOLUME);
            throw new BadParameterException("Volume must be between 0 and 100.");
        }
        else{

            this.getAudioPlayer().setVolume(volume / (100 / MAX_VOLUME));

        }
    }
	
}
