package commands;

import utilities.Command;

public class CommandSpam extends Command {
	
	@Override
	public void action(){
		
		// Defaults to 10 messages.
		int numberOfSpam = 10;
		
		try{
			
			String[] content;
			
			content = getSplitContentMaxed(2);
			
			if(getContent() != null)
				numberOfSpam = Integer.parseInt(content[0]);
			
			remember(true, BUFFER_SPAM);
			
			boolean hasCustomMessage = content != null && content.length == 2;
			
			try{
				
				for(int i = 0; i < numberOfSpam; i++){
					
					if(i != 0)
						try{
							Thread.sleep(1250);
						}
						catch(InterruptedException e){}
					
					if(!(boolean)getMemory(BUFFER_SPAM))
						break;
					
					if(hasCustomMessage)
						sendMessage(content[1]);
					else
						sendMessage(lang("Spam") + " #" + (i + 1));
					
				}
				
			}
			catch(NullPointerException e){
				// Was probably removed by the stopAction.
			}
			finally{
				forget(BUFFER_SPAM);
			}
			
		}
		catch(NumberFormatException e){
			
			String commandStart = lang("UsageStart");
			String command = buildCommand(SPAM);
			
			String command1 = String.format(commandStart, command) + " : "
					+ String.format(lang("UsageFirstLine"), numberOfSpam);
			String command2 = String.format(commandStart, command
					+ " [number of times to spam]")
					+ " : " + lang("UsageSecondLine");
			String command3 = String.format(commandStart, command
					+ " [number of times to spam] [custom message]")
					+ " : " + lang("UsageThirdLine");
			
			sendMessage(lang(true, "Usage") + " :\n" + command1 + "\n" + command2
					+ "\n" + command3);
			
		}
		
	}
	
	@Override
	public boolean stopAction(){
		
		forget(BUFFER_SPAM);
		
		return true;
		
	}
	
}
