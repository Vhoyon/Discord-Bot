package vendor.objects;

import vendor.abstracts.AbstractTerminalCommand;
import vendor.abstracts.CommandsLinker;
import vendor.interfaces.LinkableCommand;
import vendor.modules.Logger;
import vendor.terminalcommands.*;

public class TerminalCommandsLinker extends CommandsLinker {
	
	@Override
	public CommandLinksContainer createLinksContainer(){
		
		return new CommandLinksContainer(
		        CommandStart.class,
                CommandStop.class,
                CommandRestart.class,
                CommandExit.class,
				CommandUptime.class,
				CommandNumberOfServers.class
        ){
			
			@Override
			public LinkableCommand whenCommandNotFound(String commandName){
				
				return new AbstractTerminalCommand(){
					@Override
					public String[] getCalls(){
						return null;
					}
					
					@Override
					public void action(){
						Logger.log("No command with the name \"" + commandName
								+ "\"!", Logger.LogType.WARNING, false);
					}
				};
				
			}
			
		};
		
	}
	
}
