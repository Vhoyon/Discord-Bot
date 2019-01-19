package io.github.vhoyon.bot.commands;

import io.github.ved.jrequester.Option;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.MusicCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.bot.utilities.music.MusicPlayer;

/**
 * Command that tells the associated MusicPlayer to loop songs. Two loop modes
 * are available :
 * <ol>
 * <li>PLAYLIST (<i><b>default</b></i>) : loops the playlist after the last
 * track was hit;</li>
 * <li>SINGLE : loops a single track over and over (until stopped, of course).</li>
 * </ol>
 * 
 * @version 1.0
 * @since v0.6.0
 * @author Stephano Mehawej
 */
public class CommandMusicLoop extends MusicCommand {
	
	@Override
	public void musicAction(){
		
		if(!isPlaying()){
			new BotError(this, lang("CommandMusicLoopNotPlaying"));
		}
		else{
			
			MusicPlayer player = MusicManager.get().getPlayer(this);
			
			if(player.isLooping()){
				
				player.stopLooping();
				sendMessage(lang("LoopStopped"));
				
			}
			else{
				
				if(hasOption("o", "one")){
					
					player.setLoopingOne();
					sendMessage(lang("SingleLoop"));
					
				}
				else{
					
					player.setLoopingAll();
					sendMessage(lang("AllLoop"));
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public String getCall(){
		return MUSIC_LOOP;
	}
	
	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(lang("OptionOne"), false, "o", "one")
		};
	}
	
}
