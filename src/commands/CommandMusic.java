package commands;

import music.MusicManager;
import music.MusicPlayer;
import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;
import errorHandling.exceptions.BadParameterException;
import framework.Command;

public class CommandMusic extends Command {
	
	public enum CommandType{
		PLAY, PAUSE, SKIP, VOLUME
	}
	
	private CommandType commandType;
	
	public CommandMusic(CommandType commandType){
		this.commandType = commandType;
	}
	
	@Override
	public void action(){
		
		switch(commandType){
		case PLAY:
			play();
			break;
		case PAUSE:
			pause();
			break;
		case SKIP:
			skip();
			break;
		case VOLUME:
			volume();
			break;
		default:
			break;
		}
		
	}
	
	public void play(){
		
		if(getGuild() == null)
			return;
		
		if(!getGuild().getAudioManager().isConnected()
				&& !getGuild().getAudioManager().isAttemptingToConnect()){
			
			VoiceChannel voiceChannel = getGuild().getMember(getUser())
					.getVoiceState().getChannel();
			
			if(voiceChannel == null){
				sendMessage("Connect to a voice lobby");
				return;
			}
			
			getGuild().getAudioManager().openAudioConnection(voiceChannel);
		}
		
		MusicManager.get().loadTrack(this, getContent());
		
	}
	
	public void pause(){
		// TODO : Implement pause() logic.
	}
	
	public void skip(){
		
		if(!getGuild().getAudioManager().isConnected() && !getGuild().getAudioManager().isAttemptingToConnect()){
			sendMessage("No music is currently playing.");
			return;
		}
		MusicPlayer player = MusicManager.get().getPlayer(getGuild());
		
		player.skipTrack();
	
		sendMessage("Skipped! Now playing : " + player.getAudioPlayer().getPlayingTrack().getInfo().title);
		
	}
	
	public void volume(){
		
		String content = getContent();
		
		try{
			
			int volume = Integer.valueOf(content);
			
			if(volume < 0 || volume > 100){
				throw new BadParameterException();
			}
			else{
				
				MusicManager.get().getPlayer(getGuild()).getAudioPlayer().setVolume(volume / (100 / MusicPlayer.MAX_VOLUME));
				
				sendMessage("The volume has been set to " + volume + "%%!");
				
			}
			
		}
		catch(NumberFormatException e){
			new BotError(this, "Please use a number!");
		}
		catch (BadParameterException e) {
			new BotError(this, "Please enter a number between 0 and 100.");
		}
		
		
	}
	
}
