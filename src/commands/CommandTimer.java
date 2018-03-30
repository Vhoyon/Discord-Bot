package commands;

import utilities.Command;
import vendor.exceptions.NoParameterContentException;
import errorHandling.BotError;

public class CommandTimer extends Command {
	
	private int[] timeRef = new int[3];
	private boolean isAlive = true;
	
	@Override
	public void action(){
		
		int seconds = 0;
		int hours = 0;
		int minutes = 0;
		
		try{
			// seconds = Integer.parseInt(constraints[0]);
			if(hasParameter("h")){
				hours = Integer.parseInt(getParameter("h")
						.getParameterContent());
			}
			if(hasParameter("m")){
				minutes = Integer.parseInt(getParameter("m")
						.getParameterContent());
			}
			if(hasParameter("s")){
				seconds = Integer.parseInt(getParameter("s")
						.getParameterContent());
			}
			
			if(!hasParameter("h", "m", "s")){
				throw new NullPointerException();
			}
			
			int totalTime = (hours * 3600) + (minutes * 60) + seconds;
			String timerMessageId = null;
			// long totalTime = (seconds * 1000);
			// long temps = System.currentTimeMillis();
			timerMessageId = sendMessage(hours + "hours " + minutes + " minutes "
					+ seconds + " seconds");
			
			for(int i = totalTime; i >= 0 && isAlive; i--){
				
				if(i < totalTime){
					timeConstruct(i);
					editMessage(timeRef[0] + "hours " + timeRef[1] + " minutes "
							+ timeRef[2] + " seconds", timerMessageId);
				}
				
				if(i == 0){
					sendMessage("TimerEnded");
				}
				try{
					Thread.sleep(1000);
				}
				catch(InterruptedException e){}
			}
			
		}
		catch(NullPointerException e){
			sendMessage("You must give an amount of time to the "
					+ buildVCommand("timer") + " command for it to count");
		}
		catch(NumberFormatException | NoParameterContentException e){
			new BotError(this, "One of the value provided isn't a number!");
		}
		
	}
	
	private void timeConstruct(int remainingTime){
		
		if(remainingTime / 3600 >= 0){
			timeRef[0] = remainingTime / 3600;
			remainingTime -= timeRef[0] * 3600;
		}
		else
			timeRef[0] = 0;
		if(remainingTime / 60 >= 0){
			timeRef[1] = remainingTime / 60;
			remainingTime -= timeRef[1] * 60;
		}
		else
			timeRef[1] = 0;
		if(remainingTime >= 0){
			timeRef[2] = remainingTime;
		}
		
	}
	
	@Override
	public boolean stopAction(){
		isAlive = false;
		return true;
	}
	
}
