package commands;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import errorHandling.BotError;
import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;
import vendor.objects.ParametersHelp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class CommandMusicPlay extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isConnectedToVoiceChannelMember()){
			new BotError(this, lang("NotConnected"), true);
		}
		else{
			
			if(hasParameter("r")){
				
				String sources[][] =
				{
					{
						"V-ed's playlist from soundcloud",
						"https://soundcloud.com/v_ed/sets/musiiic"
					},
					{
						"No Copyrights sounds Copyright free songs from youtube",
						"https://www.youtube.com/watch?v=2jwj9wVx3mg&list=PLRBp0Fe2GpgnIh0AiYKh7o7HnYAej-5ph"
					},
					{
						"No Copyrights sounds electronic from youtube",
						"https://www.youtube.com/watch?v=tua4SVV-GSE&list=PLRBp0Fe2GpgnZOm5rCopMAOYhZCPoUyO5"
					}
				
				};
				
				int rand = new Random().nextInt(sources.length);
				
				sendInfoMessage(lang("Rand", sources[rand][0]), true);
				
				MusicManager.get().loadTrack(this, sources[rand][1],
						(player) -> connectIfNotPlaying());
				
			}
			else{
				
				if(hasParameter("l")){
					callCommand(MUSIC_REPLAY);
				}
				else{
					
					if(getContent() == null
							&& !MusicManager.get().hasPlayer(getGuild())){
						new BotError(this, lang("NoContent"));
					}
					else{
						
						if(getContent() != null){
							
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
										(player) -> connectIfNotPlaying());
								
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
