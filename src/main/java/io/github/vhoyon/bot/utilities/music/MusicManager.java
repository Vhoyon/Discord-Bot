package io.github.vhoyon.bot.utilities.music;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Guild;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.interfaces.BufferLevel;
import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.objects.Buffer;

/**
 * Handles all the {@link MusicPlayer} players that this bot has opened so far
 * and implements the logic to load tracks from these players. This Manager only
 * deals with one player per server (Guild).
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */
public class MusicManager implements Utils {
	
	private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
	private final Map<String, MusicPlayer> players = new HashMap<>();
	
	private static Runnable onTracksEmpty;
	
	private MusicManager(){
		AudioSourceManagers.registerRemoteSources(manager);
		AudioSourceManagers.registerLocalSource(manager);
	}
	
	/**
	 * Gets the MusicManager for the whole bot, such has there is only one
	 * instance at a time.
	 *
	 * @version 1.0
	 * @since v0.4.0
	 */
	public static MusicManager get(){
		return Buffer.getSingleton(MusicManager.class, MusicManager::new);
	}
	
	public static void destroy(){
		Buffer.removeSingleton(MusicManager.class);
	}
	
	public static boolean hasPlayersOnTracksEmptyLogic(){
		return MusicManager.onTracksEmpty != null;
	}
	
	public static void setPlayersOnTrackEmptyLogic(Runnable onTracksEmpty){
		MusicManager.onTracksEmpty = onTracksEmpty;
	}
	
	/**
	 * Determines if the MusicManager has a player for the Guild given in the
	 * parameters.
	 * 
	 * @param guild
	 *            The {@link net.dv8tion.jda.api.entities.Guild Guild} that
	 *            links a player to a server.
	 * @return {@code true} if there is a player for the guild provided,
	 *         {@code false} otherwise.
	 * @since v0.4.0
	 */
	public synchronized boolean hasPlayer(Guild guild){
		return players.containsKey(guild.getId());
	}
	
	public synchronized void onPlayerPresent(Guild guild,
			Consumer<MusicPlayer> onPlayerPresent){
		if(hasPlayer(guild))
			onPlayerPresent.accept(getPlayer(guild));
	}
	
	/**
	 * Search for a player given the context of the BotCommand added a
	 * parameter.
	 * 
	 * @param command
	 *            The command to get the context from.
	 * @return The {@link MusicPlayer} that is associated with the context of
	 *         the command (mostly the VoiceChannel) and if there is none,
	 *         creates a new MusicPlayer, store this new player and return it.
	 * @since v0.4.0
	 */
	public synchronized MusicPlayer getPlayer(BotCommand command){
		return this.getPlayer(command.getGuild(),
				command.setting("empty_drop_delay", BufferLevel.GUILD));
	}
	
	/**
	 * Search for a player given the context of the Guild added a
	 * parameter.
	 * 
	 * @param guild
	 *            The Guild to get the context from.
	 * @return The {@link MusicPlayer} that is associated with the context of
	 *         the Guild (mostly the VoiceChannel) and if there is none,
	 *         creates a new MusicPlayer, store this new player and return it.
	 * @since v0.13.0
	 */
	public synchronized MusicPlayer getPlayer(Guild guild){
		return this.getPlayer(guild, 0);
	}
	
	public synchronized MusicPlayer getPlayer(Guild guild,
			int defaultEmptyDropDelay){
		
		if(!hasPlayer(guild)){
			
			MusicPlayer newPLayer = new MusicPlayer(manager.createPlayer(),
					guild);
			
			newPLayer.setEmptyDropDelay(defaultEmptyDropDelay);
			newPLayer.setOnTracksEmpty(onTracksEmpty);
			
			players.put(guild.getId(), newPLayer);
			
		}
		
		return players.get(guild.getId());
		
	}
	
	/**
	 * While this method name seems to imply that it's emptying a player, it
	 * actually deletes it and remove it from the players list, effectively
	 * seeming like an emptying action from the user's viewpoint.
	 * 
	 * @param command
	 *            The command to get the context from.
	 * @since v0.4.0
	 */
	public synchronized void emptyPlayer(BotCommand command){
		this.emptyPlayer(command, true);
	}
	
	public synchronized void emptyPlayer(BotCommand command, boolean disconnect){
		
		if(this.hasPlayer(command.getGuild())){
			
			MusicPlayer player = this.getPlayer(command);
			
			player.getAudioPlayer().destroy();
			
			if(disconnect && player.isConnectedToVoiceChannel()){
				player.closeConnection();
			}
			
			players.remove(command.getGuildId());
			
		}
		
	}
	
	public synchronized void emptyPlayer(Guild guild){
		emptyPlayer(guild, true);
	}
	
	public synchronized void emptyPlayer(Guild guild, boolean disconnect){
		
		if(this.hasPlayer(guild)){
			
			MusicPlayer player = this.getPlayer(guild);
			
			player.getAudioPlayer().destroy();
			
			if(disconnect && player.isConnectedToVoiceChannel()){
				player.closeConnection();
			}
			
			players.remove(guild.getId());
			
		}
		
	}
	
	/**
	 * Loads a track in the player associated with the context of the
	 * {@code command} parameter and tries to play it immediately - putting it
	 * in a playlist if there is already music playing.
	 * <p>
	 * There is four outcome to this loading mechanism :
	 * <ol>
	 * <li>The source points to a valid source that is a single track and loads
	 * it correctly, either playing the track immediately or putting it in the
	 * player's playlist;</li>
	 * <li>The source points to a valid source that is a playlist of tracks and
	 * loads it correctly, putting all the tracks in the player's playlist and
	 * plays the first item if nothing is curretly playing;</li>
	 * <li>The source points to a source that had no match, therefore, a message
	 * is sent saying that there is no match for the source the user has given;</li>
	 * <li>The manager failed to load the source, so even if it might be valid,
	 * we couldn't fetch it, therefore a message is sent to the user saying that
	 * a problem occurred.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param command
	 *            The command to get the context from.
	 * @param source
	 *            The source of the track there is to be created. If it's a
	 *            link, it will try to find the source codec automatically if it
	 *            can, and if it's not a link, it will search for YouTube if the
	 *            environment key is available.
	 * @since v0.4.0
	 * @see #loadTrack(BotCommand, String, Runnable)
	 */
	public void loadTrack(final BotCommand command, final String source){
		this.loadTrack(command, source, null);
	}
	
	public void loadTrack(final Guild guild, final String source){
		this.loadTrack(guild, source, null, null, null, null, null);
	}
	
	/**
	 * Loads a track in the player associated with the context of the
	 * {@code command} parameter and tries to play it immediately - putting it
	 * in a playlist if there is already music playing.
	 * <p>
	 * There is four outcome to this loading mechanism :
	 * <ol>
	 * <li>The source points to a valid source that is a single track and loads
	 * it correctly, either playing the track immediately or putting it in the
	 * player's playlist;</li>
	 * <li>The source points to a valid source that is a playlist of tracks and
	 * loads it correctly, putting all the tracks in the player's playlist and
	 * plays the first item if nothing is curretly playing;</li>
	 * <li>The source points to a source that had no match, therefore, a message
	 * is sent saying that there is no match for the source the user has given;</li>
	 * <li>The manager failed to load the source, so even if it might be valid,
	 * we couldn't fetch it, therefore a message is sent to the user saying that
	 * a problem occurred.</li>
	 * </ol>
	 * </p>
	 * This method also tries to run the arbitrary code given in the Runnable
	 * {@code onSuccessLoadBeforePlay} parameter, which is ran before sending
	 * the message that it was a success <i>and</i> before playing the track(s).
	 * This code will not run if the loading of the source isn't a success, such
	 * as there is no match or if the loading failed.
	 * 
	 * @param command
	 *            The command to get the context from.
	 * @param source
	 *            The source of the track there is to be created. If it's a
	 *            link, it will try to find the source codec automatically if it
	 *            can, and if it's not a link, it will search for YouTube if the
	 *            environment key is available.
	 * @param onSuccessLoadBeforePlay
	 *            Arbitrary code that runs before sending the success message
	 *            and trying to play the loaded track. Can be {@code null} (or
	 *            use {@link #loadTrack(BotCommand, String)}) to not run
	 *            anything before playing.
	 * @since v0.4.0
	 * @see #loadTrack(BotCommand, String)
	 */
	public void loadTrack(final BotCommand command, final String source,
			Runnable onSuccessLoadBeforePlay){
		
		this.loadTrack(
				command.getGuild(),
				source,
				command.setting("empty_drop_delay"),
				onSuccessLoadBeforePlay,
				(track) -> {
					command.sendMessage(command.lang("MusicManagerTrackLoaded",
							command.code(track.getInfo().title)));
				},
				(playlist) -> {
					
					StringBuilder builder = new StringBuilder();
					
					builder.append(
							command.lang("MusicManagerPlaylistLoaded",
									command.code(playlist.getName()))).append(
							"\n");
					
					String playlistLine = command
							.lang("MusicManagerPlaylistAddedTrackInfo");
					
					playlist.getTracks()
							.forEach(
									(track) -> builder.append("\n").append(
											format(playlistLine, track
													.getPosition(),
													command.code(track
															.getInfo().title))));
					
					command.sendMessage(builder.toString());
					
				}, () -> {
					command.sendMessage(command.lang("MusicManagerNoMatch",
							command.code(source)));
				}, (exception) -> {
					command.sendMessage(command.lang("MusicManagerLoadFailed",
							command.code(exception.getMessage())));
				});
		
	}
	
	public void loadTrack(final Guild guild, final String source,
			Runnable onSuccessLoadBeforePlay,
			Consumer<AudioTrack> onTrackLoaded,
			Consumer<AudioPlaylist> onPlaylistLoaded, Runnable onNoMatch,
			Consumer<FriendlyException> onLoadFailed){
		this.loadTrack(guild, source, 0, onSuccessLoadBeforePlay,
				onTrackLoaded, onPlaylistLoaded, onNoMatch, onLoadFailed);
	}
	
	public void loadTrack(final Guild guild, final String source,
			int defaultEmptyDropDelay, Runnable onSuccessLoadBeforePlay,
			Consumer<AudioTrack> onTrackLoaded,
			Consumer<AudioPlaylist> onPlaylistLoaded, Runnable onNoMatch,
			Consumer<FriendlyException> onLoadFailed){
		
		MusicPlayer player = getPlayer(guild, defaultEmptyDropDelay);
		
		guild.getAudioManager().setSendingHandler(player.getAudioHandler());
		
		manager.loadItemOrdered(player, source, new AudioLoadResultHandler(){
			
			@Override
			public void trackLoaded(AudioTrack track){
				
				if(onSuccessLoadBeforePlay != null)
					onSuccessLoadBeforePlay.run();
				
				player.playTrack(track);
				
				if(onTrackLoaded != null)
					onTrackLoaded.accept(track);
				
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist){
				
				if(onSuccessLoadBeforePlay != null)
					onSuccessLoadBeforePlay.run();
				
				playlist.getTracks().forEach(player::playTrack);
				
				if(onPlaylistLoaded != null)
					onPlaylistLoaded.accept(playlist);
				
			}
			
			@Override
			public void noMatches(){
				if(onNoMatch != null)
					onNoMatch.run();
			}
			
			@Override
			public void loadFailed(FriendlyException exception){
				if(onLoadFailed != null)
					onLoadFailed.accept(exception);
			}
			
		});
		
	}
	
}
