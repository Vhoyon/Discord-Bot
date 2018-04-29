package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;

import errorHandling.BotError;

public class CommandMusicPause extends MusicCommands {
	
	@Override
	public void action(){
		
		if(getGuild() == null)
			return;
		
		if(!isPlaying()){
			new BotError(this, lang("NoMusic"));
		}
		else{
			
			MusicPlayer player = MusicManager.get().getPlayer(this);
			
			if(player.isPaused()){
				new BotError(this, lang("AlreadyPaused"));
			}
			else{
				player.setPause(true);
				
				sendInfoMessage(lang("Success"));
			}
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			MUSIC_PAUSE
		};
	}

	@Override
	public String getCommandDescription() {
		return "Stop the music until you start it back up";
	}
}
