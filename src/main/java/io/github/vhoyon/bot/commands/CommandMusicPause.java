package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.MusicCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.bot.utilities.music.MusicPlayer;

/**
 * Pauses the music only if the bot is already playing some music in a
 * VoiceChannel of this guild. It uses the {@link MusicManager} to deal with the
 * logic of pausing a track.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandMusicPause extends MusicCommand {
	
	@Override
	public void musicAction(){
		
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
	public String getCall(){
		return MUSIC_PAUSE;
	}
	
	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
	
}
