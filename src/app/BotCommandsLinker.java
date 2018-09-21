package app;

import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;
import vendor.utilities.formatting.DiscordFormatter;

/**
 * Linker that links the commands package which should contain all of the
 * commands available in Discord for our bot.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see vendor.abstracts.CommandsLinker
 */
public class BotCommandsLinker extends CommandsLinker implements Commands,
		DiscordFormatter {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		return new CommandLinksContainer("commands"){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new BotError(lang("NoActionForCommand",
						code(commandName)), false);
				
			}
			
		};
		
	}
	
	@Override
	public String formatCommand(String command){
		return code(command);
	}
	
}
