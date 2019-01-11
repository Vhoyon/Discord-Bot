package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.MusicCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;

/**
 * Command that disconnects the bot from the connected VoiceChannel and empties
 * the music playlist.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandMusicDisconnect extends MusicCommand {
	
	@Override
	public void musicAction(){
		
		if(!isPlaying()){
			new BotError(this, lang("NotConnected"));
			return;
		}
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(lang("Success"));
		
	}
	
	@Override
	public String getCall(){
		return MUSIC_DISCONNECT;
	}
	
	@Override
	public String getCommandDescription(){
		return "Disconnect the bot from the voice channel";
	}
	
}
