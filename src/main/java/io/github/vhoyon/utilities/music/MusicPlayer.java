package io.github.vhoyon.utilities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import io.github.vhoyon.utilities.BotCommand;

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
	
	private final AudioPlayer audioPlayer;
	private final AudioListener listener;
	private final BotCommand command;
	
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
		this.audioPlayer = audioPlayer;
		this.command = command;
		
		int defaultVolume = command.setting("volume");
		
		this.setVolume(defaultVolume);
		
		this.listener = new AudioListener(this);
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
	 *         (using {@link utilities.BotCommand#getGuild()
	 *         BotCommand.getGuild()}).
	 * @since v0.4.0
	 */
	public Guild getGuild(){
		return this.getCommand().getGuild();
	}
	
	/**
	 * Gets the command associated with this player
	 *
	 * @return The {@link utilities.BotCommand BotCommand} associated with this
	 *         player.
	 * @since v0.4.0
	 */
	public BotCommand getCommand(){
		return this.command;
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
	 *            An {@link com.sedmelluq.discord.lavaplayer.track.AudioTrack
	 *            AudioTrack} to play from.
	 * @since v0.4.0
	 */
	public synchronized void playTrack(AudioTrack track){
		this.getListener().queue(track);
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
	 * @return The {@link net.dv8tion.jda.core.entities.VoiceChannel
	 *         VoiceChannel} that is being sent music to.
	 * @since v0.4.0
	 */
	public VoiceChannel getConnectedChannel(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	/**
	 * Gets the status of wheter the player (bot) is connected to a VoiceChannel
	 *
	 * @return {@code true} if the player (bot) is connected to a
	 *         {@link net.dv8tion.jda.core.entities.VoiceChannel VoiceChannel},
	 *         {@code false} otherwise.
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
	 * static variable {@code MusicPlayer.MAX_VOLUME}).
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
