package app;

import commands.CommandClear;
import commands.CommandHelp;
import commands.CommandLanguage;
import commands.CommandTimer;
import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

public class CommandLinksBot extends CommandsLinker implements Commands {
	
	@Override
	public CommandLinksContainer getContainer(){
		
		return new CommandLinksContainer(new Link[]
		{
			new Link(CommandClear.class, CLEAR),
			new Link(CommandHelp.class, HELP),
			new Link(CommandLanguage.class, LANG, LANGUAGE),
			new Link(CommandTimer.class, TIMER)
		}){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new BotError(lang("NoActionForCommand",
						buildVCommand(commandName)), false);
				
			}
			
		};
		
	}
	
}
