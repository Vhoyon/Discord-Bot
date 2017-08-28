package commands;

import framework.Command;

public class CommandSpam extends Command {
	
	@Override
	public void action(){
		
		for(int i = 0; i < 10; i++){
			
			sendMessage("spam");
			
		}
		
	}
	
}
