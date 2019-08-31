package io.github.vhoyon.bot.app;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.interfaces.Commands;
import io.github.vhoyon.vramework.abstracts.CommandsLinker;
import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.objects.CommandLinksContainer;
import io.github.vhoyon.vramework.util.formatting.DiscordFormatter;

/**
 * Linker that links the commands package which should contain all of the
 * commands available in Discord for our bot.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see io.github.vhoyon.vramework.abstracts.CommandsLinker
 */
public class BotCommandsLinker extends CommandsLinker implements Commands,
		DiscordFormatter {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		return new CommandLinksContainer("io.github.vhoyon.bot.commands"){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new BotError(ital(lang("NoActionForCommand",
						code(commandName))), false);
				
			}
			
		};
		
	}
	
	@Override
	public String formatCommand(String command){
		return code(command);
	}
	
}
