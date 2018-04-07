package app;

import commands.*;
import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;
import vendor.objects.LinkParams;

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
			
			new LinkParams(new Link(GameInteractionCommand.class, GAME),
					GameInteractionCommand.CommandType.INITIAL),
			
			new LinkParams(new Link(GameInteractionCommand.class, GAME_ADD),
					GameInteractionCommand.CommandType.ADD),
			
			new LinkParams(new Link(GameInteractionCommand.class, GAME_REMOVE),
					GameInteractionCommand.CommandType.REMOVE),
			
			new LinkParams(new Link(GameInteractionCommand.class, GAME_ROLL,
					GAME_ROLL_ALT), GameInteractionCommand.CommandType.ROLL),
			
			new LinkParams(new Link(GameInteractionCommand.class, GAME_LIST),
					GameInteractionCommand.CommandType.LIST),
			
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
