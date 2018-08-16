package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;

import errorHandling.BotError;

public class CommandMusicVolume extends MusicCommands {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		try{
			
			int volume = Integer.valueOf(content);
			
			if(volume < 0 || volume > 100){
				new BotError(this, lang("NumberNotBetweenRange", 0, 100));
			}
			else{
				
				MusicManager.get().getPlayer(this).setVolume(volume);
				
				sendMessage(lang("ChangedSuccess", volume));
				
			}
			
		}
		catch(NumberFormatException e){
			new BotError(this, lang("NumberNotANumber"));
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
