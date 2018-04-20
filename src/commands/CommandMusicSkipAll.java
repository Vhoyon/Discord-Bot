package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

public class CommandMusicSkipAll extends MusicCommands {
	
	@Override
	public void action(){
		
		Boolean canSkipAll = canSkipAll(true);
		
		if(canSkipAll != null){
			
			MusicManager.get().emptyPlayer(this);
			
			sendInfoMessage(lang("SkippedAllMusic"));
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			MUSIC_SKIP_ALL1, MUSIC_SKIP_ALL2, MUSIC_SKIP_ALL3
		};
	}

	@Override
	public String getCommandDescription() {
		return "Skip all the songs in a playlist";
	}
}
