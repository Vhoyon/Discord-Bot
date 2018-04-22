package vendor.objects;

import java.util.LinkedHashMap;

import vendor.exceptions.CommandNotFoundException;
import vendor.interfaces.LinkableCommand;
import vendor.modules.Logger;

public abstract class CommandLinksContainer {
	
	private LinkedHashMap<String, Link> linkMap;
	
	/**
	 * The latest links commands will always replace the first command call.
	 * 
	 * @param links
	 */
	public CommandLinksContainer(Link... links){
		initializeContainer(links);
	}
	
	/**
	 * The latest links commands will always replace the first command call.
	 *
	 * @param commands
	 */
	@SafeVarargs
	public CommandLinksContainer(Class<? extends LinkableCommand>... commands){
		Link[] links = new Link[commands.length];
		
		for(int i = 0; i < commands.length; i++){
			links[i] = new Link(commands[i]);
		}
		
		initializeContainer(links);
	}
	
	private void initializeContainer(Link[] links){
		
		linkMap = new LinkedHashMap<>();
		
		for(Link link : links){
			
			String[] calls = link.getCalls();
			
			if(calls != null){
				for(String call : calls){
					
					linkMap.put(call, link);
					
				}
			}
			
		}
		
	}
	
	public LinkableCommand initiateLink(String commandName){
		try{
			
			Link link = findLink(commandName);
			
			if(link == null){
				return whenCommandNotFound(commandName);
			}
			else{
				return link.getInstance();
			}
			
		}
		catch(Exception e){
			Logger.log(e);
			
			return whenCommandNotFound(commandName);
		}
	}
	
	public abstract LinkableCommand whenCommandNotFound(String commandName);
	
	public LinkedHashMap<String, Link> getLinkMap(){
		return this.linkMap;
	}
	
	public Link findLink(String commandName){
		return getLinkMap().get(commandName);
	}
	
	public LinkableCommand findCommand(String commandName) throws Exception{
		
		Link link = findLink(commandName);
		
		if(link == null){
			throw new CommandNotFoundException(commandName);
		}
		else{
			
			return link.getInstance();
			
		}
		
	}
	
}
