package utilities.abstracts;

import errorHandling.BotError;
import net.dv8tion.jda.core.entities.VoiceChannel;
import utilities.BotCommand;

public abstract class MusicCommands extends BotCommand {

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
	
	public void connectIfNotPlaying(){
		if (!isPlaying()) {

			VoiceChannel voiceChannel = getGuild().getMember(getUser())
				.getVoiceState().getChannel();

			if (voiceChannel == null) {
				sendInfoMessage(lang("NotConnected"));
				return;
			}

			connect(voiceChannel);

		}
	}
	
}
