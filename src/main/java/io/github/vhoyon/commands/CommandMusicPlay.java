package io.github.vhoyon.commands;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import io.github.vhoyon.errorHandling.BotError;
import io.github.vhoyon.utilities.abstracts.MusicCommands;
import io.github.vhoyon.utilities.music.MusicManager;
import io.github.vhoyon.utilities.music.MusicPlayer;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Logger.LogType;
import io.github.vhoyon.vramework.objects.ParametersHelp;
import io.github.vhoyon.vramework.utilities.sanitizers.EnumSanitizer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Command to play a track into the VoiceChannel of the user that used this
 * command if he is connected to a VoiceChannel.
 * <p>
 * Many utilities are packed into this command :
 * <ul>
 * <li>Putting a link in the content of it allows the MusicManager to search
 * through that link for a possible audio source and play it immediately or
 * append the track to the MusicPlayer's playlist if there is already music
 * playing;</li>
 * <li>Putting a track's title to search through YouTube and play the first
 * result found in the same way expressed above;</li>
 * <li>Having a paused track and inputting this command without any content
 * tries to play back the paused track;</li>
 * <li>Play a random playlist using the {@code -r} (<i>or {@code --random}</i>)
 * flag.</li>
 * </ul>
 * </p>
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandMusicPlay extends MusicCommands {
	
	/**
	 * Class that holds the values of a track to be played, tracking its name
	 * and its source to be able to send messages like we want.
	 * 
	 * @author V-ed (Guillaume Marcoux)
	 * @since v0.10.0
	 */
	protected class SourceTrack {
		protected String name;
		protected String source;
		
		public SourceTrack(String name, String source){
			this.name = name;
			this.source = source;
		}
	}
	
	@Override
	public void action(){
		
		if(!isConnectedToVoiceChannelMember()){
			new BotError(this, lang("NotConnected"), true);
		}
		else{
			
			if(hasParameter("r")){
				
				SourceTrack playlistFound = getRandomTrack();
				
				sendInfoMessage(lang("Rand", code(playlistFound.name)));
				
				MusicManager.get().loadTrack(this, playlistFound.source,
						this::connectIfNotPlaying);
				
			}
			else{
				
				if(hasParameter("l")){
					callCommand(MUSIC_REPLAY);
				}
				else{
					
					if(!hasContent()
							&& !MusicManager.get().hasPlayer(getGuild())){
						new BotError(this, lang("NoContent"));
					}
					else{
						
						if(hasContent()){
							
							try{
								
								String source;
								
								if(isUrl(getContent())){
									
									sendInfoMessage("Getting data from "
											+ ital(code(getContent())) + "...",
											true);
									
									source = getContent();
									
								}
								else{
									
									sendInfoMessage("Searching Youtube for "
											+ ital(code(getContent())) + "!",
											true);
									
									source = getSourceFromYoutube(getContent());
									
								}
								
								MusicManager.get().loadTrack(this, source,
										this::connectIfNotPlaying);
								
							}
							catch(IOException e){
								sendMessage(lang("SongByStringFail"));
							}
							catch(IllegalStateException e){
								Logger.log(
										"Please setup your environment variable \"YOUTUBE_TOKEN\" to give users the ability to search using raw text!",
										LogType.WARNING);
								sendMessage("The owner of this bot did not setup his tokens correctly, please try again using a link!");
							}
							
						}
						else{
							
							MusicPlayer player = MusicManager.get().getPlayer(
									this);
							
							if(player.isPaused()){
								player.setPause(false);
								
								sendMessage(lang("Resuming"));
							}
							else{
								new BotError(this, lang("NoContent"));
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Gets a random track/playlist from a predetermined list of sources or from
	 * the environment if the latter is configured so.
	 * 
	 * @return A {@link SourceTrack} that holds the name and the source of the
	 *         track/playlist to play.
	 */
	protected SourceTrack getRandomTrack(){
		
		ArrayList<SourceTrack> playlistList = new ArrayList<>();
		
		boolean shouldAddInlineSources;
		
		try{
			
			if(hasEnv("PLAYLISTS_PLAY_RANDOM")){
				ArrayList<String> envPlaylists = EnumSanitizer
						.formatEnvironment("PLAYLISTS_PLAY_RANDOM");
				
				// Define format to get the data we want
				// This is done using the particularity of replacing regex capturing groups
				String dataRegex = "^(\\S.*)\\s*\\{(\\S.*)\\}$";
				for(String envPlaylist : envPlaylists){
					String envPlaylistName = envPlaylist.replaceAll(dataRegex,
							"$1"); // return first capturing group
					String envPlaylistSource = envPlaylist.replaceAll(
							dataRegex, "$2"); // return second capturing group
					
					playlistList.add(new SourceTrack(envPlaylistName,
							envPlaylistSource));
				}
			}
			
			shouldAddInlineSources = !hasEnv("PLAYLISTS_PLAY_RANDOM")
					|| !env("PLAYLISTS_PLAY_OVERWRITE", true);
			
		}
		catch(BadFormatException e){
			
			String logMessage;
			
			if(e.getErrorCode() == 1){
				logMessage = "The values added in the PLAYLISTS_PLAY_RANDOM env variable are not formatted correctly, make sure you follow the pattern correctly!";
			}
			else{
				logMessage = e.getMessage();
			}
			
			Logger.log(logMessage, LogType.ERROR);
			
			shouldAddInlineSources = true;
			
		}
		
		if(shouldAddInlineSources){
			
			String sources[][] =
			{
				{
					"V-ed's playlist from SoundCloud",
					"https://soundcloud.com/v_ed/sets/musiiic"
				},
				{
					"NoCopyrightsSounds Copyright free songs from YouTube",
					"https://www.youtube.com/watch?v=2jwj9wVx3mg&list=PLRBp0Fe2GpgnIh0AiYKh7o7HnYAej-5ph"
				},
				{
					"NoCopyrightsSounds electronic from YouTube",
					"https://www.youtube.com/watch?v=tua4SVV-GSE&list=PLRBp0Fe2GpgnZOm5rCopMAOYhZCPoUyO5"
				}
			};
			
			for(String[] source : sources){
				playlistList.add(new SourceTrack(source[0], source[1]));
			}
		}
		
		return playlistList.get(new Random().nextInt(playlistList.size()));
		
	}
	
	/**
	 * Finds the source of a video by searching YouTube using the query's text.
	 * This would be like if a user searched in the search bar of YouTube.
	 * 
	 * @param query
	 *            The text query to search with.
	 * @return The URL in a String form of the first video found from YouTube
	 *         using the {@code query} parameter.
	 * @throws IOException
	 *             If any errors happened while searching for the video online.
	 * @throws IllegalStateException
	 *             If the environment variable {@code YOUTUBE_TOKEN} is not set
	 * @since v0.9.0
	 */
	private String getSourceFromYoutube(String query) throws IOException,
			IllegalStateException{
		
		if(!hasEnv("YOUTUBE_TOKEN")){
			throw new IllegalStateException("youtube");
		}
		
		YouTube youtube = new YouTube.Builder(new NetHttpTransport(),
				new JacksonFactory(), new HttpRequestInitializer(){
					public void initialize(HttpRequest request)
							throws IOException{}
				}).setApplicationName("Discord Bot").build();
		
		YouTube.Search.List search = youtube.search().list("snippet");
		
		search.setMaxResults((long)1);
		search.setQ(getContent());
		search.setKey(env("YOUTUBE_TOKEN"));
		
		SearchListResponse response = search.execute();
		
		String id = response.getItems().get(0).getId().getVideoId();
		
		return "https://www.youtube.com/watch?v=" + id;
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_PLAY;
	}
	
	@Override
	public String getCommandDescription(){
		return "Start a song by giving a youtube link or restart a paused song by not giving a link";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(lang("ReplayDescription"), false, "l", "latest"),
			new ParametersHelp(lang("AnythingDescription"), false, "r",
					"random")
		};
	}
	
	/**
	 * Determines if the given parameter {@code string} is a URL or not.
	 * 
	 * @param string
	 *            The string to test.
	 * @return {@code true} if the string is formed as an URL, {@code false}
	 *         otherwise.
	 * @since v0.9.0
	 */
	public boolean isUrl(String string){
		try{
			new URL(string);
			return true;
		}
		catch(MalformedURLException e){
			return false;
		}
	}
	
}
