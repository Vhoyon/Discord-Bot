package vendor.abstracts;

import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class CommandsLinker extends Translatable {
	
	private CommandLinksContainer container;
	private String fullHelpString;
	
	public abstract CommandLinksContainer createLinksContainer();
	
	public CommandLinksContainer getContainer(){
		if(container != null)
			return container;
		
		return container = createLinksContainer();
	}
	
	public String getFullHelpString(String textHeader){
		
		if(fullHelpString != null){
			return fullHelpString;
		}
		
		CommandLinksContainer container = getContainer();
		
		StringBuilder builder = new StringBuilder();
		
		if(textHeader != null){
			builder.append(textHeader).append("\n\n");
		}
		
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
			
			builder.append("~ ").append(formatCommand(key));
			
			if(!isSubstitute){
				
				try{
					
					String helpString = link.getInstance()
							.getCommandDescription();
					
					if(helpString != null){
						builder.append(" : ").append(
								formatHelpString(helpString));
					}
					
				}
				catch(Exception e){}
				
			}
			
			builder.append("\n");
			
		});
		
		fullHelpString = builder.toString();
		
		return fullHelpString;
		
	}
	
	public String formatCommand(String command){
		return command;
	}
	
	public String formatHelpString(String helpString){
		return helpString;
	}
	
}
