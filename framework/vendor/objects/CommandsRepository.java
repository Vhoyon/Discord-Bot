package vendor.objects;

import vendor.abstracts.CommandsLinker;

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
