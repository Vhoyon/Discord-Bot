package commands;

import utilities.Command;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;
import utilities.specifics.CommandConfirmed;
import vendor.exceptions.BadContentException;
import vendor.exceptions.BadParameterException;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.VoiceChannel;
import errorHandling.BotError;

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
		
		if(getContent() == null && !MusicManager.get().hasPlayer(getGuild())){
			new BotError(this, lang("PlayNoContent"));
		}
		else{
			
			if(!isPlaying() && getContent() != null){
				
				VoiceChannel voiceChannel = getGuild().getMember(getUser())
						.getVoiceState().getChannel();
				
				if(voiceChannel == null){
					sendInfoMessage(lang("PlayNotConnected"));
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
					
					sendMessage(lang("PlayResuming"));
				}
				else{
					new BotError(this, lang("PlayNoContent"));
				}
				
			}
			
		}
		
	}
	
	public void pause(){
		
		if(getGuild() == null)
			return;
		
		if(!isPlaying()){
			new BotError(this, lang("SkipNotPlaying"));
		}
		else{
			
			MusicPlayer player = MusicManager.get().getPlayer(getGuild());
			
			if(player.isPaused()){
				new BotError(this, lang("PauseAlreadyPaused"));
			}
			else{
				player.setPause(true);
				
				sendInfoMessage(lang("PauseSuccess"));
			}
			
		}
		
	}
	
	private void skip(){
		
		MusicPlayer player = MusicManager.get().getPlayer(getGuild());
		
		if(getContent() == null){
			
			if(!isPlaying()){
				new BotError(this, lang("SkipNotPlaying"));
			}
			else{
				
				if(player.skipTrack()){
					sendInfoMessage(lang("SkippedNowPlaying", player
							.getAudioPlayer().getPlayingTrack().getInfo().title));
				}
				else{
					sendInfoMessage(lang("SkipNoMoreMusic"));
					
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
						
						sendInfoMessage(lang("SkippedNowPlaying",
								player.getAudioPlayer().getPlayingTrack()
										.getInfo().title));
						
					}
					else{
						
						new CommandConfirmed(this){
							@Override
							public String getConfMessage(){
								return lang("CommandMusicSkipOverflowConfirm",
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
				new BotError(this, lang("VolumeNotANumber"));
			}
			catch(BadContentException e){
				new BotError(this, lang("VolumeNotBetweenRange", 0, 100));
			}
			
		}
		
	}
	
	private void skipAll(){
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(lang("SkippedAllMusic"));
		
	}
	
	public void skipLogic(boolean skipAll){
		
		if(!isPlaying()){
			new BotError(this, lang("SkipNotPlaying"));
			return;
		}
		
		if(hasParameter("a", "all")){
			skipAll = true;
		}
		
		if(!skipAll)
			skip();
		else
			skipAll();
		
	}
	
	public void disconnect(){
		
		if(!isPlaying()){
			new BotError(this, lang("DisconnectNotConnected"));
			return;
		}
		
		MusicManager.get().emptyPlayer(this);
		
		sendInfoMessage(lang("DisconnectSuccess"));
		
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
				
				sendMessage(lang("VolumeChangedSuccess", volume));
				
			}
			
		}
		catch(NumberFormatException e){
			new BotError(this, lang("VolumeNotANumber"));
		}
		catch(BadParameterException e){
			new BotError(this, lang("VolumeNotBetweenRange", 0, 100));
		}
		
	}
	
	public void list(){
		
		if(!isPlaying()){
			new BotError(this, lang("ListNoList", buildVCommand(MUSIC_PLAY
					+ " [music]")));
		}
		else{
			
			StringBuilder sb = new StringBuilder();
			
			AudioTrack currentTrack = MusicManager.get().getPlayer(getGuild())
					.getAudioPlayer().getPlayingTrack();
			
			sb.append(lang("ListCurrentTrack", currentTrack.getInfo().title));
			
			if(MusicManager.get().getPlayer(getGuild()).getListener()
					.getTrackSize() != 0){
				
				sb.append("\n\n").append(lang("ListHeader")).append("\n\n");
				
				int i = 1;
				
				for(AudioTrack track : MusicManager.get().getPlayer(getGuild())
						.getListener().getTracks()){
					
					sb.append(lang("ListTrackInfo", i++, track.getInfo().title))
							.append("\n");
					
				}
				
			}
			
			sendMessage(sb.toString());
			
		}
		
	}
	
	private boolean isPlaying(){
		return getGuild().getAudioManager().isConnected()
				|| getGuild().getAudioManager().isAttemptingToConnect();
	}
	
}
