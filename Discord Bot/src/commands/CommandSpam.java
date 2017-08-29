package commands;

import ressources.Ressources;
import framework.Command;

public class CommandSpam extends Command {
	
	@Override
	public void action(){
		
		// Defaults to 10 messages.
		int numberOfSpam = 10;
		
		try{
			
			String[] content = getSplitContentMaxed(2);
			
			if(getContent() != null)
				numberOfSpam = Integer.parseInt(content[0]);
			
			getBuffer().push(true, "SPAM_CONDITION");
			
			boolean hasCustomMessage = content.length == 2;
			
			for(int i = 0; i < numberOfSpam
					&& (boolean)getBuffer().get("SPAM_CONDITION"); i++){
				
				if(i != 0)
					try{
						Thread.sleep(1250);
					}
					catch(InterruptedException e){}
				
				if(hasCustomMessage)
					sendMessage(content[1]);
				else
					sendMessage("Spam #" + (i + 1));
				
			}
			
			getBuffer().remove("SPAM_CONDITION");
			
		}
		catch(NumberFormatException e){
			
			String commandStart = "`" + Ressources.PREFIX + "spam";
			
			String command1 = commandStart + "` : Send a default of "
					+ numberOfSpam + " spam messages.";
			String command2 = commandStart
					+ " [number of times to spam]` : Send a **[number of spam]**.";
			String command3 = commandStart
					+ " [number of times to spam] [custom message]` : Send a **[number of spam]** with a **[custom message]**.";
			
			sendMessage("Usage :\n" + command1 + "\n" + command2 + "\n"
					+ command3);
			
		}
		
	}
}
