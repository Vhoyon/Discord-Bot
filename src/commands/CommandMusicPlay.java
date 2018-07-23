package commands;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;

import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;
import vendor.modules.Environment;
import vendor.objects.ParametersHelp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CommandMusicPlay extends MusicCommands {
	
	@Override
	public void action(){
		
		if(getGuild() == null)
			return;
		
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
					
					connectIfNotPlaying();
					
					String id;
					String source;
					
					try{
						
						if(!isUrl(getContent())){
							
							YouTube youtube = new YouTube.Builder(
									new NetHttpTransport(),
									new JacksonFactory(),
									new HttpRequestInitializer(){
										public void initialize(
												HttpRequest request)
												throws IOException{}
									}).setApplicationName("Discord Bot")
									.build();
							
							YouTube.Search.List search = youtube.search().list(
									"snippet");
							
							search.setMaxResults((long)1);
							search.setQ(getContent());
							search.setKey(Environment.getVar("YOUTUBE_TOKEN"));
							
							SearchListResponse response = search.execute();
							
							id = response.getItems().get(0).getId()
									.getVideoId();
							
							source = "https://www.youtube.com/watch?v=" + id;
							
						}
						else{
							source = getContent();
						}
						
						MusicManager.get().loadTrack(this, source);
						
					}
					catch(IOException e){
						
						sendMessage(lang("SongByStringFail"));
						
					}
					
				}
				else{
					
					MusicPlayer player = MusicManager.get().getPlayer(this);
					
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
	
	@Override
	public Object getCalls(){
		return MUSIC_PLAY;
	}
	
	@Override
	public String getCommandDescription(){
		return "Start a song by giving a youtube link or restart a paused song by not giving a link";
		
	}

	@Override
	public ParametersHelp[] getParametersDescriptions() {
		return new ParametersHelp[]{
		
			new ParametersHelp(lang("ReplayDescription"),"l" ,"latest")
			
		};
	}

	public boolean isUrl(String string){
		try{
			URL url = new URL(string);
			return true;
		}
		catch(MalformedURLException e){
			return false;
		}
	}
}
