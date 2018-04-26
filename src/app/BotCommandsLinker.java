package app;

import errorHandling.BotError;
import utilities.interfaces.Commands;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.objects.CommandLinksContainer;

public class BotCommandsLinker extends CommandsLinker implements Commands {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer("commands"){
			
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
