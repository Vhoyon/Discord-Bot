package vendor.abstracts;

import vendor.objects.CommandLinksContainer;
import vendor.objects.Link;

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
			
			boolean isSubstitute = defaultCommands.containsKey(link
					.getDefaultCall());
			
			if(!isSubstitute){
				defaultCommands.put(link.getDefaultCall(), link);
			}
			
		});
		
		String prependChars = getPrependChars();
		String prependCharsVariants = getPrependCharsForVariants();
		
		defaultCommands.forEach((key, link) -> {
			
			String wholeCommandString = formatWholeCommand(prependChars, key);
			
			String wholeHelpString = null;
			
			try{

				// Try to get the help string of a link
				String helpString = link.getInstance().getCommandDescription();
				
				wholeHelpString = formatHelpString(helpString);
				
			}
			catch(Exception e){}
			
			if(wholeHelpString == null){
				builder.append(wholeCommandString);
			}
			else{
				builder.append(formatWholeHelpLine(wholeCommandString,
						wholeHelpString));
			}
			
			builder.append("\n");
			
			String[] calls = link.getCalls();

			// Add all of the non default calls of a link as a variant
			for(int i = 1; i < calls.length; i++){
				
				builder.append(formatVariant(formatWholeCommand(prependCharsVariants,
						calls[i])));
				
				builder.append("\n");
				
			}
			
		});
		
		fullHelpString = builder.toString().trim();
		
		return fullHelpString;
		
	}
	
	private String formatWholeCommand(String prependChars, String command){
		if(prependChars == null)
			return formatCommand(command);
		
		return prependChars + formatCommand(command);
	}
	
	public String formatWholeHelpLine(String wholeCommandString,
			String wholeHelpString){
		return wholeCommandString + " : " + wholeHelpString;
	}
	
	public String formatCommand(String command){
		return command;
	}
	
	public String formatHelpString(String helpString){
		return helpString;
	}
	
	public String formatVariant(String variant){
		return "\t" + variant;
	}
	
	public String getPrependChars(){
		return "~ ";
	}
	
	public String getPrependCharsForVariants(){
		return getPrependChars();
	}
	
}
