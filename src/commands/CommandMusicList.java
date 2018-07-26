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
			
			AudioTrack currentTrack = MusicManager.get().getPlayer(this)
					.getAudioPlayer().getPlayingTrack();
			
			sb.append(lang("CurrentTrack", code(currentTrack.getInfo().title)));
			
			if(MusicManager.get().getPlayer(this).getListener().getTrackSize() != 0){
				
				sb.append("\n\n").append(lang("Header")).append("\n\n");
				
				int i = 1;
				
				for(AudioTrack track : MusicManager.get().getPlayer(this)
						.getListener().getTracks()){
					
					sb.append(
							lang("TrackInfo", code(i++),
									code(track.getInfo().title))).append("\n");
					
				}
				
			}
			
			sendMessage(sb.toString());
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_LIST;
	}
	
	@Override
	public String getCommandDescription(){
		return "Display a list of all the music that you have in the music list";
		
	}
}
