package io.github.vhoyon.utilities.abstracts;

import net.dv8tion.jda.core.entities.VoiceChannel;
import io.github.vhoyon.utilities.BotCommand;

/**
 * Class that extends {@link io.github.vhoyon.utilities.BotCommand BotCommand} that is used to
 * categorize all music commands. It adds few utilities that all music commands
 * might use.
 *
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class MusicCommands extends BotCommand {
	
	/**
	 * Determines if the bot is connected or trying to connect to a
	 * VoiceChannel.
	 *
	 * @return {@code true} if the bot is playing some music, {@code false}
	 *         otherwise.
	 * @since v0.5.0
	 */
	protected boolean isPlaying(){
		return getGuild().getAudioManager().isConnected()
				|| getGuild().getAudioManager().isAttemptingToConnect();
	}
	
	/**
	 * Tries to connect the bot to the VoiceChannel of the user who sent this
	 * command. It will not connect if the bot is already playing music or if
	 * the user is not connected to a VoiceChannel.
	 *
	 * @return {@code true} if the bot was able to join a VoiceChannel,
	 *         {@code false} otherwise.
	 * @since v0.5.0
	 */
	public boolean connectIfNotPlaying(){
		
		if(isPlaying()){
			return false;
		}
		else{
			
			VoiceChannel voiceChannel = getConnectedVoiceChannelMember();
			
			if(voiceChannel == null){
				return false;
			}
			
			connect(voiceChannel);
			
			return true;
			
		}
		
	}
	
}
