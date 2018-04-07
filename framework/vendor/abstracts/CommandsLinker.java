package vendor.abstracts;

import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class CommandsLinker extends Translatable {
	
	public abstract CommandLinksContainer getContainer();
	
	public String getFullHelpString(){
		
		CommandLinksContainer container = getContainer();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Available commands:").append("\n\n");
		
		TreeMap<String, Link> linksMap = container.getLinkMap();
		
		ArrayList<Link> links = new ArrayList<>();
		
		linksMap.forEach((key, link) -> {
			
			boolean isSubstitute = links.contains(link);
			
			if(!isSubstitute){
				links.add(link);
			}
			else{
				builder.append("\t");
			}
			
			builder.append("- ").append(key);
			
			builder.append("\n");
			
		});
		
		return builder.toString();
		
	}
	
}
