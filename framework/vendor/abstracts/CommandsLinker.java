package vendor.abstracts;

import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class CommandsLinker extends Translatable {
	
	private static String fullHelpString;
	
	public abstract CommandLinksContainer getContainer();
	
	public String getFullHelpString(){
		
		if(fullHelpString != null){
			return fullHelpString;
		}
		
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
			
			builder.append("~ `!!").append(key).append("`");
			
			if(!isSubstitute){
				
				try{
					
					String helpString = link.initiate().getHelpString();
					
					if(helpString != null){
						builder.append(" : ").append(helpString);
					}
					
				}
				catch(Exception e){}
				
			}
			
			builder.append("\n");
			
		});

		fullHelpString = builder.toString();
		
		return fullHelpString;
		
	}
	
}
