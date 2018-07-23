package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

public class CommandMusicReplay extends MusicCommands {
	@Override
	public void action(){
		
		if(getGuild() == null)
			return;
		
		connectIfNotPlaying();
		
		String trackSource = null;
		
		if(hasMemory("LATEST_SONG")){
			trackSource = (String)getMemory("LATEST_SONG");
		}else{
			sendMessage(lang("NoPreviousSong"));
		}
		
		MusicManager.get().loadTrack(this, trackSource);
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_REPLAY;
	}
}
