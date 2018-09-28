package io.github.vhoyon.commands;

import io.github.vhoyon.utilities.abstracts.MusicCommands;
import io.github.vhoyon.utilities.music.MusicManager;
import io.github.vhoyon.utilities.music.MusicPlayer;
import io.github.vhoyon.errorHandling.BotError;

/**
 * Pauses the music only if the bot is already playing some music in a
 * VoiceChannel of this guild. It uses the {@link MusicManager} to deal with the
 * logic of pausing a track.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
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
	public Object getCalls(){
		return MUSIC_PAUSE;
	}
	
	@Override
	public String getCommandDescription(){
		return "Stop the music until you start it back up";
	}
	
}
