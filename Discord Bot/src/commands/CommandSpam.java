package commands;

import framework.Command;

public class CommandSpam extends Command {
	
	@Override
	public void action(){
		
		// Defaults to 10 messages.
		int numberOfSpam = 10;
		
		try{
			
			String[] content;
			
			try{
				content = getSplitContentMaxed(2);
			}
			catch(Exception e){
				content = new String[]{};
			}
			
			if(getContent() != null)
				numberOfSpam = Integer.parseInt(content[0]);
			
			getBuffer().push(true, BUFFER_SPAM);
			
			boolean hasCustomMessage = content.length == 2;
			
			try{
				
				for(int i = 0; i < numberOfSpam ; i++){
					
					if(i != 0)
						try{
							Thread.sleep(1250);
						}
						catch(InterruptedException e){}
					
					if(!(boolean)getBuffer().get(BUFFER_SPAM))
						break;
					
					if(hasCustomMessage)
						sendMessage(content[1]);
					else
						sendMessage("Spam #" + (i + 1));
					
				}
				
				getBuffer().remove(BUFFER_SPAM);
				
			}
			catch(NullPointerException e){
				
				sendMessage("\\~\\~YOU BROKE ME... <3\\~\\~");
				
			}
			
		}
		catch(NumberFormatException e){
			
			String commandStart = "`" + buildCommand(SPAM);
			
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
