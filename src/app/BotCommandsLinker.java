package app;

import commands.*;
import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;

public class BotCommandsLinker extends CommandsLinker implements Commands {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer(
			CommandHello.class,
			CommandHelp.class,
			CommandMusicPlay.class,
			CommandMusicPause.class,
			CommandMusicSkip.class,
			CommandMusicSkipAll.class,
			CommandMusicDisconnect.class,
			CommandMusicVolume.class,
			CommandMusicList.class,
			CommandClear.class,
			CommandSpam.class,
			CommandTerminate.class,
			CommandStop.class,
			CommandGameInitial.class,
			CommandGameAdd.class,
			CommandGameRemove.class,
			CommandGameRoll.class,
			CommandGameList.class,
			CommandTimer.class,
			CommandLanguage.class
		){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new BotError(lang("NoActionForCommand",
						buildVCommand(commandName)), false);
				
			}
			
		};
		
	}
	
	@Override
	public String formatCommand(String command){
		return buildVText(command);
	}
	
}
