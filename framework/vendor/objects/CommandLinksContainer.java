package vendor.objects;

import java.util.HashMap;

import vendor.interfaces.LinkableCommand;
import vendor.modules.Logger;

public abstract class CommandLinksContainer {
	
	private static HashMap<String, Link> linkMap;
	
	@SafeVarargs
	/**
	 * The latest links commands will always replace the first command call.
	 * @param links
	 */
	public CommandLinksContainer(Link... links){
		
		linkMap = new HashMap<>();
		
		for(Link link : links){
			
			String[] calls = link.getCalls();
			
			for(String call : calls){
				
				linkMap.put(call, link);
				
			}
			
		}
		
	}
	
	public LinkableCommand initiateLink(String commandName){
		try{
			
			Link link = linkMap.get(commandName);
			
			if(link == null){
				return whenCommandNotFound(commandName);
			}
			else{
				return link.initiate();
			}
			
		}
		catch(Exception e){
			Logger.log(e);
			
			return whenCommandNotFound(commandName);
		}
	}
	
	public abstract LinkableCommand whenCommandNotFound(String commandName);
	
}
