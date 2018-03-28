package app;

import commands.*;
import utilities.Dictionary;
import vendor.objects.CommandLinker;
import vendor.objects.Link;

public class CommandsRepository {
	
	private CommandsRepository(){}
	
	public static CommandLinker create(Dictionary dict){
		
		return new CommandLinker(new Link[]
		{
			new Link(CommandClear.class, "clear"),
			new Link(CommandHelp.class, "help"),
			new Link(CommandLanguage.class, "lang"),
			new Link(CommandMusic.class, ""),
		});
		
	}
	
}
