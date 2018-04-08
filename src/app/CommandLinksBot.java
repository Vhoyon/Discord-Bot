package app;

import commands.*;
import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

public class CommandLinksBot extends CommandsLinker implements Commands {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer(new Link[]
		{
			
			new Link(CommandHello.class),
			
			new Link(CommandHelp.class),
			
			new Link(CommandMusicPlay.class),
			
			new Link(CommandMusicPause.class),
			
			new Link(CommandMusicSkip.class),
			
			new Link(CommandMusicSkipAll.class),
			
			new Link(CommandMusicDisconnect.class),
			
			new Link(CommandMusicVolume.class),
			
			new Link(CommandMusicList.class),
			
			new Link(CommandClear.class),
			
			new Link(CommandSpam.class),
			
			new Link(CommandTerminate.class),
			
			new Link(CommandStop.class),
			
			new Link(CommandGameInitial.class),
			
			new Link(CommandGameAdd.class),
			
			new Link(CommandGameRemove.class),
			
			new Link(CommandGameRoll.class),
			
			new Link(CommandGameList.class),
			
			new Link(CommandTimer.class),
			
			new Link(CommandLanguage.class),
		
		}){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new BotError(lang("NoActionForCommand",
						buildVCommand(commandName)), false);
				
			}
			
		};
		
	}
	
	@Override
	public String formatCommand(String command){
		return buildVCommand(command);
	}

}
