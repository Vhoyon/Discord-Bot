package utilities.abstracts;

import errorHandling.BotError;
import net.dv8tion.jda.core.entities.VoiceChannel;
import utilities.BotCommand;

public abstract class MusicCommands extends BotCommand {

	protected boolean isPlaying(){
		return getGuild().getAudioManager().isConnected()
				|| getGuild().getAudioManager().isAttemptingToConnect();
	}
	
	public boolean connectIfNotPlaying(){
		
		if(isPlaying()){
			return false;
		}
		else{

			VoiceChannel voiceChannel = getMember().getVoiceState()
					.getChannel();

			if (voiceChannel == null) {
				sendInfoMessage(lang("NotConnected"));
				return false;
			}

			connect(voiceChannel);
			
			return true;

		}
		
	}
	
}
