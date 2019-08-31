package io.github.vhoyon.bot.utilities.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.github.vhoyon.vramework.util.TimerManager;

/**
 * Handles all the tracks inside of a {@link MusicPlayer} and gives utility
 * methods such as giving the playlist's size, queuing new tracks, deleting the
 * queue or even starting the next track.
 * <p>
 * This also handles the logic of what happens when a track ends, on which the
 * looping methods are determined in.
 * </p>
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */
public class AudioListener extends AudioEventAdapter {
	
	private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
	private final MusicPlayer player;
	
	private Runnable onTracksEmpty;
	
	public AudioListener(MusicPlayer player){
		this(player, null);
	}
	
	public AudioListener(MusicPlayer player, Runnable onTracksEmpty){
		this.player = player;
		this.setOnTracksEmpty(onTracksEmpty);
	}
	
	public void setOnTracksEmpty(Runnable onTracksEmpty){
		this.onTracksEmpty = onTracksEmpty;
	}
	
	/**
	 * @return The current tracks as a {@link BlockingQueue} of
	 *         {@link AudioTrack}. The track playing will not be in this list.
	 * @since v0.4.0
	 */
	public BlockingQueue<AudioTrack> getTracks(){
		return tracks;
	}
	
	/**
	 * @return The playlist's size in terms of how many tracks there is
	 *         remaining in this playlist.
	 * @since v0.4.0
	 */
	public int getTrackSize(){
		return tracks.size();
	}
	
	/**
	 * Starts the next track in the playlist immediately if there is remaining
	 * tracks.
	 * 
	 * @return {@code false} if there is no remaining track to be played or if
	 *         the next track couldn't start for some reason, or {@code true}
	 *         otherwise.
	 * @since v0.4.0
	 */
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
		
		this.player.setLatestTrackURI(track.getInfo().uri);
		
		if(this.player.isLoopingOne()){
			player.playTrack(track.makeClone());
		}
		else if(endReason.mayStartNext){
			
			if(this.player.isLoopingAll()){
				tracks.add(track.makeClone());
				nextTrack();
			}
			else{
				
				if(!tracks.isEmpty()){
					nextTrack();
				}
				else{
					
					int emptyDropDelay = this.player.getEmptyDropDelay();
					
					Runnable actionsWhenEmpty = () -> {
						
						if(onTracksEmpty != null)
							onTracksEmpty.run();
						
						MusicManager.get().emptyPlayer(this.player.getGuild());
						
					};
					
					if(emptyDropDelay <= 0){
						actionsWhenEmpty.run();
					}
					else{
						
						TimerManager.schedule("noMusicDelay", emptyDropDelay,
								actionsWhenEmpty);
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * @param track
	 *            {@link AudioTrack} to be queued for a future play. If nothing
	 *            is playing, the track will start playing immediately.
	 * @since v0.4.0
	 */
	public void queue(AudioTrack track){
		
		if(!player.getAudioPlayer().startTrack(track, true))
			tracks.offer(track);
		
	}
	
	/**
	 * Removes all the tracks from this playlist.
	 * 
	 * @since v0.4.0
	 */
	public void purgeQueue(){
		tracks.clear();
	}
	
}
