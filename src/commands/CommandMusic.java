package commands;

import utilities.Command;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;
import utilities.specifics.CommandConfirmed;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;
import errorHandling.exceptions.BadContentException;
import errorHandling.exceptions.BadParameterException;

public class CommandMusic extends Command {
	
	public enum CommandType{
		PLAY, PAUSE, SKIP, SKIP_ALL, VOLUME, LIST, DISCONNECT
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
		case DISCONNECT:
			disconnect();
			break;
		case VOLUME:
			volume();
			break;
		case LIST:
			list();
			break;
		default:
			break;
		}
		
	}
	
	public void play(){
		
		if(getGuild() == null)
			return;
		
		if(!isPlaying() && getContent() != null){
			
			VoiceChannel voiceChannel = getGuild().getMember(getUser())
					.getVoiceState().getChannel();
			
			if(voiceChannel == null){
				sendInfoMessage(getStringEz("PlayNotConnected"));
				return;
			}
			
			getGuild().getAudioManager().openAudioConnection(voiceChannel);
			
		}
		
		if(getContent() == null && !MusicManager.get().hasPlayer(getGuild())){
			new BotError(this, getStringEz("PlayNoContent"));
		}
		else{
			
			if(getContent() != null){
				MusicManager.get().loadTrack(this, getContent());
			}
			else{
				
				MusicPlayer player = MusicManager.get().getPlayer(getGuild());
				
				if(player.isPaused()){
					player.setPause(false);
					
					sendMessage(getStringEz("PlayResuming"));
				}
				else{
					new BotError(this, getStringEz("PlayNoContent"));
				}
				
			}
			
		}
		
	}
	
	public void pause(){
		
		if(getGuild() == null)
			return;
		
		if(!isPlaying()){
			new BotError(this, getStringEz("SkipNotPlaying"));
		}
		else{
			
			MusicPlayer player = MusicManager.get().getPlayer(getGuild());
			
			if(player.isPaused()){
				new BotError(this, getStringEz("PauseAlreadyPaused"));
			}
			else{
				player.setPause(true);
				
				sendInfoMessage(getStringEz("PauseSuccess"));
			}
			
		}
		
	}
	
	private void skip(){
		
		MusicPlayer player = MusicManager.get().getPlayer(getGuild());
		
		if(getContent() == null){
			
			if(!isPlaying()){
				new BotError(this, getStringEz("SkipNotPlaying"));
			}
			else{
				
				if(player.skipTrack()){
					sendInfoMessage(getStringEz("SkippedNowPlaying", player
							.getAudioPlayer().getPlayingTrack().getInfo().title));
				}
				else{
					sendInfoMessage(getStringEz("SkipNoMoreMusic"));
					
					MusicManager.get().emptyPlayer(this);
				}
				
			}
			
		}
		else{
			
			try{
				
				int skipAmount = Integer.valueOf(getContent());
				
				if(skipAmount < 1){
					throw new BadContentException();
				}
				else{
					
					if(skipAmount <= player.getNumberOfTracks()){
						
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
								return getString(
										"CommandMusicSkipOverflowConfirm",
										skipAmount, player.getNumberOfTracks());
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
		
		if(!isPlaying()){
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
	
	public void disconnect(){
		
		if(!isPlaying()){
			new BotError(this, getStringEz("DisconnectNotConnected"));
			return;
		}
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(getStringEz("DisconnectSuccess"));
		
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
	
	public void list(){
		
		if(!isPlaying()){
			new BotError(this, getStringEz("ListNoList",
					buildVCommand(MUSIC_PLAY + " [music]")));
		}
		else{
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(getStringEz("ListHeader")).append("\n\n");
			
			int i = 1;
			
			for(AudioTrack track : MusicManager.get().getPlayer(getGuild())
					.getListener().getTracks()){
				
				sb.append(
						getStringEz("ListTrackInfo", i++, track.getInfo().title))
						.append("\n");
				
			}
			
			sendMessage(sb.toString());
			
		}
		
	}
	
	private boolean isPlaying(){
		return getGuild().getAudioManager().isConnected()
				|| getGuild().getAudioManager().isAttemptingToConnect();
	}
	
}
