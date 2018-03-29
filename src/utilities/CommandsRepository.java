package utilities;

import vendor.abstracts.CommandsLinker;
import vendor.objects.CommandLinksContainer;
import vendor.objects.Dictionary;

public class CommandsRepository {
	
	private Dictionary dict;
	
	private CommandsLinker commandsLinker;
	
	public CommandsRepository(CommandsLinker commandLinker){
		this.commandsLinker = commandLinker;
	}
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	public CommandLinksContainer getContainer(){
		commandsLinker.setDictionary(dict);
		
		return commandsLinker.getContainer();
	}
	
}
