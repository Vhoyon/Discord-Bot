package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

public class CommandMusicReplay extends MusicCommands {
	@Override
	public void action(){
		
		if(!hasMemory("LATEST_SONG")){
			sendMessage(lang("NoPreviousSong"));
		}
		else{
			
			String trackSource = null;
			
			trackSource = (String)getMemory("LATEST_SONG");
			
			MusicManager.get().loadTrack(this, trackSource, (player) -> connectIfNotPlaying());
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_REPLAY;
	}
}
