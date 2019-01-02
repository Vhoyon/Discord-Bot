package io.github.vhoyon.bot.commands;

import io.github.ved.jsanitizers.IntegerSanitizer;
import io.github.ved.jsanitizers.exceptions.BadFormatException;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.MusicCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;

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
public class CommandMusicVolume extends MusicCommand {
	
	@Override
	public void musicAction(){
		
		String content = getContent();
		
		int min = 0;
		int max = 100;
		
		try{
			
			int volume = IntegerSanitizer.sanitizeValue(content, min, max);
			
			MusicManager.get().getPlayer(this).setVolume(volume);
			
			sendMessage(lang("ChangedSuccess", volume));
			
		}
		catch(BadFormatException e){
			
			switch(e.getErrorCode()){
			case IntegerSanitizer.FORMAT_NOT_A_NUMBER:
				new BotError(this, lang("NumberNotANumber"));
				break;
			default:
				new BotError(this, lang("NumberNotBetweenRange", min, max));
				break;
			}
			
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
