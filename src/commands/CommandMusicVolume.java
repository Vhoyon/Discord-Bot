package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import vendor.exceptions.BadParameterException;

import errorHandling.BotError;

public class CommandMusicVolume extends MusicCommands {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		try{
			
			int volume = Integer.valueOf(content);
			
			MusicManager.get().getPlayer(this).setVolume(volume);
			
			sendMessage(lang("ChangedSuccess", volume));
			
		}
		catch(NumberFormatException e){
			new BotError(this, lang("NumberNotANumber"));
		}
		catch(BadParameterException e){
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
