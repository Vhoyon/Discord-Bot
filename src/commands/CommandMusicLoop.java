package commands;

import errorHandling.BotError;
import utilities.abstracts.MusicCommands;
import vendor.objects.ParametersHelp;

public class CommandMusicLoop extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isPlaying()){
			new BotError(this, lang("CommandMusicLoopNotPlaying"));
		}
		else{
			
			if(this.hasMemory("MUSIC_LOOP")
					&& (boolean)this.getMemory("MUSIC_LOOP")){
				
				forget("MUSIC_LOOP");
				sendMessage("The loop has been stopped");
				
			}
			else if(this.hasMemory("LOOP_ONCE")
					&& (boolean)this.getMemory("LOOP_ONCE")){
				
				forget("LOOP_ONCE");
				sendMessage("The loop has been stopped");
				
			}
			else{
				
				if(hasParameter("o", "one")){
					
					remember(true, "LOOP_ONCE");
					sendMessage("A Loop on the current song has been started.");
					
				}
				else{
					
					remember(true, "MUSIC_LOOP");
					sendMessage("A loop on all the songs of the playlist has been started.");
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			MUSIC_LOOP
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "Skip the song that is currently playing";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"This makes the looping of a single song instead of the whole track playlist.",
					false, "o", "one")
		};
	}
	
}
