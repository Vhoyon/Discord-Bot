package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;

import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;

public class CommandMusicPlay extends MusicCommands {

	@Override
	public void action(){

		if(getGuild() == null)
			return;

		if(getContent() == null && !MusicManager.get().hasPlayer(getGuild())){
			new BotError(this, lang("NoContent"));
		}
		else{

			if(!isPlaying() && getContent() != null){

				VoiceChannel voiceChannel = getGuild().getMember(getUser())
						.getVoiceState().getChannel();

				if(voiceChannel == null){
					sendInfoMessage(lang("NotConnected"));
					return;
				}

				connect(voiceChannel);

			}

			if(getContent() != null){
				MusicManager.get().loadTrack(this, getContent());
			}
			else{

				MusicPlayer player = MusicManager.get().getPlayer(getGuild());

				if(player.isPaused()){
					player.setPause(false);

					sendMessage(lang("Resuming"));
				}
				else{
					new BotError(this, lang("NoContent"));
				}

			}

		}

	}

	@Override
	public String[] getCalls(){
		return new String[]
		{
			MUSIC_PLAY
		};
	}
}
