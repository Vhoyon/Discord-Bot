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

public class MusicManager {
	
	private static MusicManager musicManager;
	
	private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
	private final Map<String, MusicPlayer> players = new HashMap<>();
	
	private MusicManager(){
		AudioSourceManagers.registerRemoteSources(manager);
		AudioSourceManagers.registerLocalSource(manager);
	}
	
	public static MusicManager get(){
		
		if(musicManager == null){
			musicManager = new MusicManager();
		}
		
		return musicManager;
		
	}
	
	public synchronized boolean hasPlayer(Guild guild){
		return players.containsKey(guild.getId());
	}
	
	public synchronized MusicPlayer getPlayer(BotCommand command){
		
		if(!hasPlayer(command.getGuild()))
			players.put(command.getGuild().getId(),
					new MusicPlayer(manager.createPlayer(), command));
		
		return players.get(command.getGuild().getId());
		
	}
	
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
	
	public void loadTrack(final BotCommand command, final String source){
		this.loadTrack(command, source, null);
	}
	
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
			public void loadFailed(FriendlyException exeption){
				command.sendMessage(command.lang("MusicManagerLoadFailed",
						command.code(exeption.getMessage())));
			}
			
		});
		
	}
	
}
