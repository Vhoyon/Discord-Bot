package commands;

import music.MusicManager;
import music.MusicPlayer;
import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;
import errorHandling.exceptions.BadContentException;
import errorHandling.exceptions.BadParameterException;
import framework.Command;
import framework.specifics.CommandConfirmed;

public class CommandMusic extends Command {
	
	public enum CommandType{
		PLAY, PAUSE, SKIP, SKIP_ALL, VOLUME
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
			skipLogic(false);
			break;
		case SKIP_ALL:
			skipLogic(true);
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
				&& !getGuild().getAudioManager().isAttemptingToConnect()
				&& getContent() != null){
			
			VoiceChannel voiceChannel = getGuild().getMember(getUser())
					.getVoiceState().getChannel();
			
			if(voiceChannel == null){
				sendMessage(getStringEz("PlayNotConnected"));
				return;
			}
			
			getGuild().getAudioManager().openAudioConnection(voiceChannel);
			
		}
		
		if(getContent() == null && !MusicManager.get().hasPlayer(getGuild())){
			new BotError(this,
					"Please enter the title or the link to your music after the command!");
		}
		else{
			
			if(getContent() != null){
				MusicManager.get().loadTrack(this, getContent());
			}
			else{
				
				MusicPlayer player = MusicManager.get().getPlayer(getGuild());
				
				if(player.isPaused()){
					player.setPause(false);
					
					sendMessage("Resuming where you left off!");
				}
				else{
					new BotError(this,
							"Please enter the title or the link to your music after the command!");
				}
				
			}
			
		}
		
	}
	
	public void pause(){
		
		if(getGuild() == null)
			return;
		
		if(!getGuild().getAudioManager().isConnected()){
			new BotError(this,
					"You cannot pause the music when the bot is not playing any!");
		}
		else{
			
			MusicPlayer player = MusicManager.get().getPlayer(getGuild());
			
			if(player.isPaused()){
				new BotError(this, "The bot is already paused!");
			}
			else{
				player.setPause(true);
				
				sendInfoMessage("The music has been paused!");
			}
			
		}
		
	}
	
	private void skip(){
		
		MusicPlayer player = MusicManager.get().getPlayer(getGuild());
		
		if(getContent() == null){
			
			if(player.skipTrack()){
				sendInfoMessage(getStringEz("SkippedNowPlaying", player
						.getAudioPlayer().getPlayingTrack().getInfo().title));
			}
			else{
				sendInfoMessage("No more music to the queue!");
				
				MusicManager.get().emptyPlayer(this);
			}
			
		}
		else{
			
			try{
				
				int skipAmount = Integer.valueOf(getContent());
				
				if(skipAmount < 1){
					throw new BadContentException();
				}
				else{
					
					if(skipAmount < player.getNumberOfTracks()){
						
						for(int i = 0; i < skipAmount; i++){
							player.skipTrack();
						}
						
						sendInfoMessage(getStringEz("SkippedNowPlaying",
								player.getAudioPlayer().getPlayingTrack()
										.getInfo().title));
						
					}
					else{
						
						new CommandConfirmed(this){
							@Override
							public String getConfMessage(){
								return "Are you sure about that? You are trying to skip all the tracks as you entered a number (`"
										+ skipAmount
										+ "`) higher than the remaining number of songs (`"
										+ player.getNumberOfTracks()
										+ "`) in the playlist.";
							}
							
							@Override
							public void confirmed(){
								skipAll();
							}
						};
						
					}
					
				}
				
			}
			catch(NumberFormatException e){
				new BotError(this, getStringEz("VolumeNotANumber"));
			}
			catch(BadContentException e){
				new BotError(this, getStringEz("VolumeNotBetweenRange", 0, 100));
			}
			
		}
		
	}
	
	private void skipAll(){
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(getStringEz("SkippedAllMusic"));
		
	}
	
	public void skipLogic(boolean skipAll){
		
		if(!getGuild().getAudioManager().isConnected()
				&& !getGuild().getAudioManager().isAttemptingToConnect()){
			new BotError(this, getStringEz("SkipNotPlaying"));
			return;
		}
		
		if(isParameterPresent("a", "all")){
			skipAll = true;
		}
		
		if(!skipAll)
			skip();
		else
			skipAll();
		
	}
	
	public void volume(){
		
		String content = getContent();
		
		try{
			
			int volume = Integer.valueOf(content);
			
			if(volume < 0 || volume > 100){
				throw new BadParameterException();
			}
			else{
				
				MusicManager.get().getPlayer(getGuild()).getAudioPlayer()
						.setVolume(volume / (100 / MusicPlayer.MAX_VOLUME));
				
				sendMessage(getStringEz("VolumeChangedSuccess", volume));
				
			}
			
		}
		catch(NumberFormatException e){
			new BotError(this, getStringEz("VolumeNotANumber"));
		}
		catch(BadParameterException e){
			new BotError(this, getStringEz("VolumeNotBetweenRange", 0, 100));
		}
		
	}
	
}
