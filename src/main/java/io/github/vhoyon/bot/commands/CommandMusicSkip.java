package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.MusicCommand;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.bot.utilities.music.MusicPlayer;
import io.github.vhoyon.bot.utilities.specifics.CommandConfirmed;
import io.github.vhoyon.vramework.exceptions.BadContentException;
import io.github.vhoyon.vramework.objects.ParametersHelp;

/**
 * Command that skips tracks on demand. If there is no song that is playing
 * currently, it tells the user that there is no music to skip.
 * <p>
 * The default behavior is to skip a single track, but the user can add the
 * parameter {@code -a} or enter "{@code all}" as the command's content to skip
 * all tracks if he so desires.
 * </p>
 * <p>
 * There is also the option to skip a number of tracks, and to do so, the user
 * can enter the number of tracks to skip as the command's content and bot will
 * skip this amount of tracks before playing the next track in the list. <br>
 * In the case where he tries to skip more than the number of tracks available
 * in the current playlist, a confirmation message will ask the user if he
 * confirms to skip through all tracks.
 * </p>
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandMusicSkip extends MusicCommand {
	
	@Override
	public void musicAction(){
		
		if(!isPlaying()){
			new BotError(this, lang("NotPlaying"));
		}
		else{
			
			// Skip all songs if has appropriate parameter or content is "all"
			if(hasParameter("a") || ("all".equals(getContent()))){
				
				MusicManager.get().emptyPlayer(this);
				
				sendInfoMessage(lang("SkippedAllMusic"));
				
			}
			else{
				
				MusicPlayer player = MusicManager.get().getPlayer(this);
				
				if(!hasContent()){
					
					if(this.hasMemory("MUSIC_LOOP")
							&& (boolean)this.getMemory("MUSIC_LOOP")){
						forget("MUSIC_LOOP");
					}
					if(player.skipTrack()){
						sendInfoMessage(lang("SkippedNowPlaying",
								code(player.getAudioPlayer().getPlayingTrack()
										.getInfo().title)));
					}
					else{
						MusicManager.get().emptyPlayer(this);
						
						sendInfoMessage(lang("NoMoreMusic"));
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
										return lang(
												"OverflowConfirm",
												code(skipAmount),
												code(player.getNumberOfTracks()));
									}
									
									@Override
									public void confirmed(){
										
										MusicManager.get().emptyPlayer(
												CommandMusicSkip.this);
										
										sendInfoMessage(lang("SkippedAllMusic"));
										
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
	public String getCall(){
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
					"Skips all the songs added and disconnect the bot.", false,
					"a", "all")
		};
	}
	
}
