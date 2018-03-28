package vendor.objects;

import java.util.HashMap;

public class CommandLinker {
	
	private Link[] links;
	
	private static HashMap<String, Link> linkMap;
	
	@SafeVarargs
	/**
	 * The latest links commands will always replace the first command call.
	 * @param links
	 */
	public CommandLinker(Link... links){
		this.links = links;
		
		linkMap = new HashMap<>();
		
		for(Link link : links){
			
			String[] calls = link.getCalls();
			
			for(String call : calls){
				
				linkMap.put(call, link);
				
			}
			
		}
	}
	
	public void initiateLink(String commandName){
		
		HashMap<String, Link> debug = linkMap;
		
		Link link = linkMap.get(commandName);
		
		link.initiate();
	}
	
}
