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

				MusicPlayer player = MusicManager.get().getPlayer(this);

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
	public Object getCalls(){
		return MUSIC_PLAY;
	}

	@Override
	public String getCommandDescription() {
		return "Start a song by giving a youtube link or restart a paused song by not giving a link";
	}
}
