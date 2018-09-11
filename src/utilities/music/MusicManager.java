package utilities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import utilities.BotCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles all the {@link MusicPlayer} players that this bot has opened so far
 * and implements the logic to load tracks from these players. This Manager only
 * deals with one player per server (Guild).
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */
public class MusicManager {
	
	private static MusicManager musicManager;
	
	private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
	private final Map<String, MusicPlayer> players = new HashMap<>();
	
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
		
		if(musicManager == null){
			musicManager = new MusicManager();
		}
		
		return musicManager;
		
	}
	
	/**
	 * Determines if the MusicManager has a player for the Guild given in the
	 * parameters.
	 * 
	 * @param guild
	 *            The {@link net.dv8tion.jda.core.entities.Guild Guild} that
	 *            links a player to a server.
	 * @return {@code true} if there is a player for the guild provided,
	 *         {@code false} otherwise.
	 * @since v0.4.0
	 */
	public synchronized boolean hasPlayer(Guild guild){
		return players.containsKey(guild.getId());
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
		
		if(!hasPlayer(command.getGuild()))
			players.put(command.getGuild().getId(),
					new MusicPlayer(manager.createPlayer(), command));
		
		return players.get(command.getGuild().getId());
		
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
		
		if(this.hasPlayer(command.getGuild())){
			
			MusicPlayer player = this.getPlayer(command);
			
			player.getAudioPlayer().destroy();
			
			if(player.isConnectedToVoiceChannel()){
				player.closeConnection();
			}
			
			players.remove(command.getGuildId());
			
			command.disconnect();
			
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
		
		MusicPlayer player = getPlayer(command);
		
		command.getGuild().getAudioManager()
				.setSendingHandler(player.getAudioHandler());
		
		manager.loadItemOrdered(player, source, new AudioLoadResultHandler(){
			
			@Override
			public void trackLoaded(AudioTrack track){
				if(onSuccessLoadBeforePlay != null)
					onSuccessLoadBeforePlay.run();
				
				command.sendMessage(command.lang("MusicManagerTrackLoaded",
						command.code(track.getInfo().title)));
				
				player.playTrack(track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist){
				
				if(onSuccessLoadBeforePlay != null)
					onSuccessLoadBeforePlay.run();
				
				StringBuilder builder = new StringBuilder();
				
				builder.append(
						command.lang("MusicManagerPlaylistLoaded",
								command.code(playlist.getName()))).append("\n");
				
				for(int i = 0; i < playlist.getTracks().size(); i++){
					AudioTrack track = playlist.getTracks().get(i);
					
					builder.append("\n").append(
							command.lang("MusicManagerPlaylistAddedTrackInfo",
									(i + 1),
									command.code(track.getInfo().title)));
					
					player.playTrack(track);
				}
				
				command.sendMessage(builder.toString());
				
			}
			
			@Override
			public void noMatches(){
				command.sendMessage(command.lang("MusicManagerNoMatch",
						command.code(source)));
			}
			
			@Override
			public void loadFailed(FriendlyException exception){
				command.sendMessage(command.lang("MusicManagerLoadFailed",
						command.code(exception.getMessage())));
			}
			
		});
		
	}
	
}
