package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;
import utilities.specifics.CommandConfirmed;
import vendor.exceptions.BadContentException;

import errorHandling.BotError;

public class CommandMusicSkip extends MusicCommands {
	
	@Override
	public void action(){
		
		Boolean canSkipAll = canSkipAll(false);
		
		if(canSkipAll != null){
			
			if(canSkipAll){
				
				new CommandMusicSkipAll().action();
				
			}
			else{
				
				MusicPlayer player = MusicManager.get().getPlayer(getGuild());
				
				if(getContent() == null){
					
					if(!isPlaying()){
						new BotError(this, lang("NotPlaying"));
					}
					else{
						
						if(player.skipTrack()){
							sendInfoMessage(lang("SkippedNowPlaying", player
									.getAudioPlayer().getPlayingTrack()
									.getInfo().title));
						}
						else{
							sendInfoMessage(lang("NoMoreMusic"));
							
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
								
								sendInfoMessage(lang(
										"SkippedNowPlaying",
										player.getAudioPlayer()
												.getPlayingTrack().getInfo().title));
								
							}
							else{
								
								new CommandConfirmed(this){
									@Override
									public String getConfMessage(){
										return lang("OverflowConfirm",
												skipAmount,
												player.getNumberOfTracks());
									}
									
									@Override
									public void confirmed(){
										
										new CommandMusicSkipAll().action();
										
									}
								};
								
							}
							
						}
						
					}
					catch(NumberFormatException e){
						new BotError(this, lang("NumberNotANumber"));
					}
					catch(BadContentException e){
						new BotError(this,
								lang("NumberNotBetweenRange", 0, 100));
					}
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_SKIP;
	}

	@Override
	public String getCommandDescription() {
		return "Skip the song that is currently playing";
	}
}
