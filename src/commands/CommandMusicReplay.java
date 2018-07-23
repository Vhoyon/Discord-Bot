package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

public class CommandMusicReplay extends MusicCommands {
	@Override
	public void action(){
		
		if(getGuild() == null)
			return;
		
		connectIfNotPlaying();
		
		String trackSource = (String)getMemory("LATEST_SONG");
		
		MusicManager.get().loadTrack(this, trackSource);
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_REPLAY;
	}
}
