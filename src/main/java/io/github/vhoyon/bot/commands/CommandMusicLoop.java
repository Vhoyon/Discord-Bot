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
				sendMessage("The loop has been stopped");
				
			}
			else{
				
				if(hasOption("o", "one")){
					
					player.setLoopingOne();
					sendMessage("A Loop on the current song has been started.");
					
				}
				else{
					
					player.setLoopingAll();
					sendMessage("A loop on all the songs of the playlist has been started.");
					
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
		return "Skip the song that is currently playing";
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(
					"This makes the looping of a single song instead of the whole track playlist.",
					false, "o", "one")
		};
	}
	
}
