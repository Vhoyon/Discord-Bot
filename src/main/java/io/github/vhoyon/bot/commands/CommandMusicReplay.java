package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.utilities.abstracts.MusicCommands;
import io.github.vhoyon.bot.utilities.music.MusicManager;

/**
 * Command that replays the latest track that was in the playlist for this
 * command's context.
 * 
 * @version 1.0
 * @since v0.9.0
 * @author Stephano Mehawej
 */
public class CommandMusicReplay extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!hasMemory("LATEST_SONG")){
			sendMessage(lang("NoPreviousSong"));
		}
		else{
			
			String trackSource = (String)getMemory("LATEST_SONG");
			
			MusicManager.get().loadTrack(this, trackSource,
					this::connectIfNotPlaying);
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_REPLAY;
	}
	
	@Override
	public String getCommandDescription(){
		return super.getCommandDescription(); // TODO : Write description for CommandMusicReplay
	}
	
}
