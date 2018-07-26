package utilities.abstracts;

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

			VoiceChannel voiceChannel = getConnectedVoiceChannelMember();

			if (voiceChannel == null) {
				return false;
			}

			connect(voiceChannel);
			
			return true;

		}
		
	}
	
}
