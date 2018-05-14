package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

import errorHandling.BotError;

public class CommandMusicDisconnect extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isPlaying()){
			new BotError(this, lang("NotConnected"));
			return;
		}
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(lang("Success"));
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_DISCONNECT;
	}

	@Override
	public String getCommandDescription() {
		return "Disconnect the bot from the voice channel";
	}
}
