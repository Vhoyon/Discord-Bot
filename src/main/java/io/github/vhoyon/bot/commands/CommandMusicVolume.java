package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.utilities.abstracts.MusicCommands;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.ved.jsanitizers.IntegerSanitizer;

/**
 * Command to change the general volume of the MusicPlayer so that everyone's
 * volume is affected. Note that this doesn't mean the bot's volume will be
 * changed in each users' preferences, but rather than the output of the audio
 * stream will be reduced to whatever the amount is given to this command will
 * be.
 * <p>
 * There is an hardcoded limit bounds to <b>0</b> through <b>100</b> to simulate
 * a percentage given. Therefore, any number given to the content of this
 * command will be checked and appropriate error message will be sent in the
 * case where the content is not valid for this command.
 * </p>
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandMusicVolume extends MusicCommands {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		try{
			
			int volume = IntegerSanitizer.sanitizeValue(content, 0, 100);
			
			MusicManager.get().getPlayer(this).setVolume(volume);
			
			sendMessage(lang("ChangedSuccess", volume));
			
		}
		catch(NumberFormatException e){
			new BotError(this, lang("NumberNotANumber"));
		}
		catch(IllegalArgumentException e){
			new BotError(this, lang("NumberNotBetweenRange", 0, 100));
		}
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_VOLUME;
	}
	
	@Override
	public String getCommandDescription(){
		return "Change the volume of the music that the bot plays";
	}
	
}
