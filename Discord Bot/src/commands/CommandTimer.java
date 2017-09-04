package commands;

import framework.Command;

public class CommandTimer extends Command {
	
	@Override
	public void action(){
		
		String[] constraints = getSplitContent();
		int seconds = 0;
//		int count = 0;
//		boolean timerOn = true;
		try{
			seconds = Integer.parseInt(constraints[0]);
			
		}
		catch(NullPointerException e){
			sendMessage("You must give an amount of time to the "
					+ buildVCommand("timer") + " command for it too count");
		}
		
		String lastmsgId = null;
		//		long totalTime = (seconds * 1000);
		//		long temps = System.currentTimeMillis();
		sendMessage(seconds + " seconds");
		lastmsgId = (String)getBuffer().get(BUFFER_LASTMSGID);
		for(int i = seconds; i >= 0; i--){
			editMessage(i + " seconds", lastmsgId);
			if(i == 0){
				sendMessage("TimerEnded");
			}
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){}
		}
		
	}
	
}
