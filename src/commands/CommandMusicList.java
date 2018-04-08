package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import errorHandling.BotError;

public class CommandMusicList extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isPlaying()){
			new BotError(this, lang("NoList", buildVCommand(MUSIC_PLAY
					+ " [music]")));
		}
		else{
			
			StringBuilder sb = new StringBuilder();
			
			AudioTrack currentTrack = MusicManager.get().getPlayer(getGuild())
					.getAudioPlayer().getPlayingTrack();
			
			sb.append(lang("CurrentTrack", currentTrack.getInfo().title));
			
			if(MusicManager.get().getPlayer(getGuild()).getListener()
					.getTrackSize() != 0){
				
				sb.append("\n\n").append(lang("Header")).append("\n\n");
				
				int i = 1;
				
				for(AudioTrack track : MusicManager.get().getPlayer(getGuild())
						.getListener().getTracks()){
					
					sb.append(lang("TrackInfo", i++, track.getInfo().title))
							.append("\n");
					
				}
				
			}
			
			sendMessage(sb.toString());
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			MUSIC_LIST
		};
	}
}
