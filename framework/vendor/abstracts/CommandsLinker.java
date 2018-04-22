package vendor.abstracts;

import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

import java.util.ArrayList;
import java.util.HashMap;
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
		
		HashMap<String, Link> linksMap = container.getLinkMap();
		
		TreeMap<String, Link> defaultCommands = new TreeMap<>();
		
		linksMap.forEach((key, link) -> {
			
			boolean isSubstitute = defaultCommands.containsKey(link.getDefaultCall());
			
			if(!isSubstitute){
				defaultCommands.put(link.getDefaultCall(), link);
			}

		});
		
		String prependChars = getPrependChars();
		
		defaultCommands.forEach((key, link) -> {

			if(prependChars != null){
				builder.append(formatWholeCommand(key));
			}
			else {
				builder.append(formatCommand(key));
			}

			try{

				String helpString = link.getInstance()
						.getCommandDescription();

				if(helpString != null){
					builder.append(" : ").append(
							formatHelpString(helpString));
				}

			}
			catch(Exception e){}

			builder.append("\n");

			String[] calls = link.getCalls();
			
			for(int i = 1; i < calls.length; i++){
				
				builder.append("\t");

				if(prependChars != null){
					builder.append(formatWholeCommand(calls[i]));
				}
				else {
					builder.append(formatCommand(calls[i]));
				}
				
				builder.append("\n");
				
			}
			
		});
		
		fullHelpString = builder.toString().trim();
		
		return fullHelpString;
		
	}

	private String formatWholeCommand(String command){
		return getPrependChars() + formatCommand(command);
	}
	
	public String formatCommand(String command){
		return command;
	}
	
	public String formatHelpString(String helpString){
		return helpString;
	}
	
	public String getPrependChars(){
		return "~ ";
	}
	
}
