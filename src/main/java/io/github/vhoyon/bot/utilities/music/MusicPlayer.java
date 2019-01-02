package io.github.vhoyon.bot.utilities.music;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.utilities.TimerManager;

/**
 * Handles the playback of tracks added to the {@link AudioListener} object.
 *
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */
public class MusicPlayer {
	
	/**
	 * Represents the max comfortable value for listening in Discord.
	 * 
	 * @since v0.4.0
	 */
	public final static int MAX_VOLUME = 20;
	
	public final static int DEFAULT_VOLUME = 60;
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final Guild guild;
	
	private String lastTrackURI;
	private boolean shouldLoopOne;
	private boolean shouldLoopAll;
	
	private int emptyDropDelay;
	
	/**
	 * Creates a MusicPlayer that sets its initial volume to the setting "
	 * {@code volume}" from the BotCommand supplied in the {@code command}
	 * parameter.
	 * 
	 * @param audioPlayer
	 *            The AudioPlayer to play music out of.
	 * @param command
	 *            The command to get the context for handling different tasks
	 *            such as connections management.
	 * @since v0.4.0
	 */
	public MusicPlayer(AudioPlayer audioPlayer, BotCommand command){
		this(audioPlayer, command.getGuild(), command.setting("volume"));
	}
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild){
		this(audioPlayer, guild, null);
	}
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild,
			Runnable onTracksEmpty){
		this(audioPlayer, guild, DEFAULT_VOLUME, onTracksEmpty);
	}
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild, int defaultVolume){
		this(audioPlayer, guild, defaultVolume, null);
	}
	
	public MusicPlayer(AudioPlayer audioPlayer, Guild guild, int defaultVolume,
			Runnable onTracksEmpty){
		this.audioPlayer = audioPlayer;
		this.guild = guild;
		
		this.setVolume(defaultVolume);
		
		this.listener = new AudioListener(this, onTracksEmpty);
		this.audioPlayer.addListener(this.listener);
	}
	
	/**
	 * Gets LavaPlayer's AudioPlayer object of this player.
	 *
	 * @return The {@link com.sedmelluq.discord.lavaplayer.player.AudioPlayer
	 *         AudioPlayer} object linked to this custom player.
	 * @since v0.4.0
	 */
	public AudioPlayer getAudioPlayer(){
		return this.audioPlayer;
	}
	
	/**
	 * Gets the AudioListener object of this player.
	 *
	 * @return The {@link AudioListener} object linked to this custom player.
	 * @since v0.4.0
	 */
	public AudioListener getListener(){
		return this.listener;
	}
	
	/**
	 * Gets the Guild of this command.
	 *
	 * @return This command's {@link net.dv8tion.jda.core.entities.Guild Guild}
	 *         (using {@link BotCommand#getGuild()}).
	 * @since v0.4.0
	 */
	public Guild getGuild(){
		return this.guild;
	}
	
	public String getLatestTrackURI(){
		return this.lastTrackURI;
	}
	
	public void setLatestTrackURI(String uri){
		this.lastTrackURI = uri;
	}
	
	public boolean isLooping(){
		return this.isLoopingOne() || this.isLoopingAll();
	}
	
	public boolean isLoopingOne(){
		return this.shouldLoopOne;
	}
	
	public void setLoopingOne(){
		this.shouldLoopOne = true;
		this.shouldLoopAll = false;
	}
	
	public boolean isLoopingAll(){
		return this.shouldLoopAll;
	}
	
	public void setLoopingAll(){
		this.shouldLoopOne = false;
		this.shouldLoopAll = true;
	}
	
	public void stopLooping(){
		this.shouldLoopOne = false;
		this.shouldLoopAll = false;
	}
	
	public int getEmptyDropDelay(){
		return this.emptyDropDelay;
	}
	
	public void setEmptyDropDelay(int emptyDropDelay){
		this.emptyDropDelay = emptyDropDelay;
	}
	
	public void setOnTracksEmpty(Runnable onTracksEmpty){
		this.getListener().setOnTracksEmpty(onTracksEmpty);
	}
	
	/**
	 * Gets the AudioHandler of this player.
	 *
	 * @return A new {@link AudioHandler} associated with this player.
	 * @since v0.4.0
	 */
	public AudioHandler getAudioHandler(){
		return new AudioHandler(this.getAudioPlayer());
	}
	
	/**
	 * Plays the track from the parameter {@code track}. This will generally be
	 * called automatically.
	 *
	 * @param track
	 *            An {@link com.sedmelluq.discord.lavaplayer.track.AudioTrack}
	 *            to play from.
	 * @since v0.4.0
	 */
	public synchronized void playTrack(AudioTrack track){
		this.getListener().queue(track);
		TimerManager.stopTimer("noMusicDelay");
	}
	
	/**
	 * Toggles the pause state of this player (will pause if playing and play if
	 * paused).
	 *
	 * @since v0.4.0
	 */
	public synchronized void togglePause(){
		this.setPause(!this.isPaused());
	}
	
	/**
	 * Sets the pause status of this player that will pause the player's tracks
	 * if the param {@code isPaused} is {@code true} and play the tracks it if
	 * the param is {@code false}.
	 *
	 * @param isPaused
	 *            {@code true} to make the player pause the music, {@code false}
	 *            to make the player play the music.
	 * @since v0.4.0
	 */
	public synchronized void setPause(boolean isPaused){
		this.getAudioPlayer().setPaused(isPaused);
	}
	
	/**
	 * Gets the status of the player to know if the tracks are paused or not.
	 *
	 * @return {@code true} if this player's tracks playback is paused,
	 *         {@code false} otherwise.
	 * @since v0.4.0
	 */
	public synchronized boolean isPaused(){
		return this.getAudioPlayer().isPaused();
	}
	
	/**
	 * Allows to skip the current track and go to the next in the list
	 * immediately.
	 *
	 * @return {@code true} if the skip was a success, {@code false} otherwise
	 *         or if their is no other tracks in the list.
	 * @since v0.4.0
	 */
	public synchronized boolean skipTrack(){
		return this.getListener().nextTrack();
	}
	
	/**
	 * Gets the amount of tracks remaining in the playlist of this player.
	 *
	 * @return An {@code int} that tells how many remaining tracks there are in
	 *         this player.
	 * @since v0.4.0
	 */
	public synchronized int getNumberOfTracks(){
		return this.getListener().getTracks().size();
	}
	
	/**
	 * Get the connected VoiceChannel of this player.
	 *
	 * @return The {@link net.dv8tion.jda.core.entities.VoiceChannel} that is
	 *         being sent music to.
	 * @since v0.4.0
	 */
	public VoiceChannel getConnectedChannel(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	/**
	 * Gets the status of wheter the player (bot) is connected to a VoiceChannel
	 *
	 * @return {@code true} if the player (bot) is connected to a
	 *         {@link net.dv8tion.jda.core.entities.VoiceChannel}, {@code false}
	 *         otherwise.
	 * @since v0.4.0
	 */
	public boolean isConnectedToVoiceChannel(){
		return this.getConnectedChannel() != null;
	}
	
	/**
	 * Closes the connection of this player to the VoiceChannel - meaning in
	 * other words that the bot will leave the VoiceChannel.
	 * 
	 * @since v0.4.0
	 */
	public void closeConnection(){
		this.getGuild().getAudioManager().closeAudioConnection();
	}
	
	/**
	 * Sets the volume of this player. <br>
	 * There is a formula applied to it where the volume is transformed to
	 * behave like a percentage : therefore, the volume is divided by <i><u>100
	 * / {@value #MAX_VOLUME}</u></i> ({@value #MAX_VOLUME} being taken from the
	 * static variable {@link MusicPlayer#MAX_VOLUME}).
	 *
	 * @param volume
	 *            Integer that represents the volume in percentage of
	 *            comfortable listening value.
	 * @since v0.4.0
	 */
	public void setVolume(int volume){
		this.getAudioPlayer().setVolume(
				(int)Math.ceil((double)volume
						/ ((double)100 / (double)MAX_VOLUME)));
	}
	
}
