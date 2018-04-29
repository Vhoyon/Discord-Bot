package utilities.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class AudioListener extends AudioEventAdapter {
	
	private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
	private final MusicPlayer player;
	
	public AudioListener(MusicPlayer player){
		this.player = player;
	}
	
	public BlockingQueue<AudioTrack> getTracks(){
		return tracks;
	}
	
	public int getTrackSize(){
		return tracks.size();
	}
	
	public boolean nextTrack(){
		
		if(tracks.isEmpty()){
			
			if(player.getGuild().getAudioManager().getConnectedChannel() != null){
				player.getGuild().getAudioManager().closeAudioConnection();
			}
			return false;
			
		}
		
		return player.getAudioPlayer().startTrack(tracks.poll(), false);
		
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track,
			AudioTrackEndReason endReason){

		if(this.player.getCommand().hasMemory("MUSIC_LOOP") && (boolean) this.player.getCommand().getMemory("MUSIC_LOOP")){
			player.playTrack(track.makeClone());

		}else if(endReason.mayStartNext)
			nextTrack();
		
	}
	
	public void queue(AudioTrack track){
		
		if(!player.getAudioPlayer().startTrack(track, true))
			tracks.offer(track);
		
	}
	
	public void purgeQueue() {
        tracks.clear();
    }
	
}
