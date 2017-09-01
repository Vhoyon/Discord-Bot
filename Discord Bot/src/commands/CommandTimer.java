package commands;

import framework.Command;

public class CommandTimer extends Command {
	
	@Override
	public void action(){
		int seconds = 10;
		long temps = System.currentTimeMillis();
//		while(System.currentTimeMillis()-temps != seconds*1000){
//			String messageToSend = String.valueOf(System.currentTimeMillis()-temps == seconds);
//		}
		sendMessage("LOL");
		editMessage("IT WORKS", (String)getBuffer().get(BUFFER_LASTMSGID));
		
	}
	
}
