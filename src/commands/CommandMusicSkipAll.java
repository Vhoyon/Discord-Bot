package commands;

import errorHandling.BotError;
import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

public class CommandMusicSkipAll extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isPlaying()){
			new BotError(this, lang("CommandMusicSkipNotPlaying"));
		}
		else{
			
			MusicManager.get().emptyPlayer(this);
			
			sendInfoMessage(lang("SkippedAllMusic"));
			
		}
		
	}
	
	@Override
	public Object getCalls(){
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
