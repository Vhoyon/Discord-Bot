package utilities.abstracts;

import errorHandling.BotError;
import utilities.Command;

public abstract class MusicCommands extends Command {
	
	public Boolean canSkipAll(boolean skipAll){
		
		if(!isPlaying()){
			new BotError(this, lang("CommandMusicSkipNotPlaying"));
			return null;
		}
		
		if(hasParameter("a", "all")){
			skipAll = true;
		}

		return skipAll;

	}
	
	protected boolean isPlaying(){
		return getGuild().getAudioManager().isConnected()
				|| getGuild().getAudioManager().isAttemptingToConnect();
	}
	
}
