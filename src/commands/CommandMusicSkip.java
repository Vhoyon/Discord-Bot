package commands;

import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;
import utilities.specifics.CommandConfirmed;
import vendor.exceptions.BadContentException;

import errorHandling.BotError;
import vendor.objects.ParametersHelp;

public class CommandMusicSkip extends MusicCommands {
	
	@Override
	public void action(){
		
		if(!isPlaying()){
			new BotError(this, lang("NotPlaying"));
		}
		else{
			
			if(hasParameter("a")){
				
				MusicManager.get().emptyPlayer(this);
				
				sendInfoMessage(lang("SkippedAllMusic"));
				
			}
			else{
				
				MusicPlayer player = MusicManager.get().getPlayer(this);
				
				if(getContent() == null){
					
					if(this.hasMemory("MUSIC_LOOP")
							&& (boolean)this.getMemory("MUSIC_LOOP")){
						forget("MUSIC_LOOP");
					}
					if(player.skipTrack()){
						sendInfoMessage(lang(
								"SkippedNowPlaying",
								code(player.getAudioPlayer()
										.getPlayingTrack().getInfo().title)));
					}
					else{
						sendInfoMessage(lang("NoMoreMusic"));
						
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
							
							if(skipAmount <= player.getNumberOfTracks()){
								
								for(int i = 0; i < skipAmount; i++){
									player.skipTrack();
								}
								
								sendInfoMessage(lang("SkippedNowPlaying",
										player.getAudioPlayer()
												.getPlayingTrack()
												.getInfo().title));
								
							}
							else{
								
								new CommandConfirmed(this){
									@Override
									public String getConfMessage(){
										return lang(
												"OverflowConfirm",
												code(skipAmount),
												code(player
														.getNumberOfTracks()));
									}
									
									@Override
									public void confirmed(){
										callCommand(new CommandMusicSkipAll());
									}
								};
								
							}
							
						}
						
					}
					catch(NumberFormatException e){
						new BotError(this, lang("NumberNotANumber"));
					}
					catch(BadContentException e){
						new BotError(this, lang("NumberNotBetweenRange", 0,
								100));
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
	public String getCommandDescription(){
		return "Skip the song that is currently playing";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"Skips all the songs added and disconnect the bot.", "a", "all")
		};
	}
	
}
