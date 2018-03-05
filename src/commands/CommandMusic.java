package commands;

import music.MusicManager;
import music.MusicPlayer;
import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;
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
		
		MusicManager.get().getPlayer(getGuild()).skipTrack();
		
		sendMessage("Skipped!");
		
	}
	
	public void volume(){
		
		String content = getContent();
		
		try{
			
			int volume = Integer.valueOf(content);
			
			if(volume < 0 || volume > 100){
				throw new IllegalArgumentException();
			}
			else{
				MusicManager.get().getPlayer(getGuild()).getAudioPlayer().setVolume(volume / (100 / MusicPlayer.MAX_VOLUME));
			}
			
		}
		catch(NumberFormatException e){
			new BotError("Please use a number!");
		}
		catch (IllegalArgumentException e) {
			new BotError("Please enter a number between 0 and 100.");
		}
		
		
	}
	
}
