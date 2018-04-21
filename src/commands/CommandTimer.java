package commands;

import utilities.Command;
import vendor.exceptions.NoContentException;
import errorHandling.BotError;
import vendor.objects.ParametersHelp;

public class CommandTimer extends Command {
	
	private int[] timeRef = new int[3];
	private boolean isAlive = true;
	
	@Override
	public void action(){
		
		int seconds = 0;
		int hours = 0;
		int minutes = 0;
		
		try{
			
			if(!hasParameter("h", "m", "s", "hours")){
				throw new NullPointerException();
			}
			
			// seconds = Integer.parseInt(constraints[0]);
			if(hasParameter("h", "hours")){
				hours = Integer.parseInt(getParameter("h", "hours")
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
			
			int totalTime = (hours * 3600) + (minutes * 60) + seconds;
			String timerMessageId = null;
			// long totalTime = (seconds * 1000);
			// long temps = System.currentTimeMillis();
			timerMessageId = sendMessage(formatDate(timeRef[0], timeRef[1],
					timeRef[2]));
			
			try{
				
				for(int i = totalTime; i >= 0 && isAlive; i--){
					
					if(i < totalTime){
						Thread.sleep(1000);
						
						timeConstruct(i);
						editMessageQueue(timerMessageId,
								formatDate(timeRef[0], timeRef[1], timeRef[2]));
					}
					
				}

				sendMessage("TimerEnded");
				
			}
			catch(InterruptedException e){}
			
		}
		catch(NullPointerException e){
			sendMessage("You must give an amount of time to the "
					+ buildVCommand("timer") + " command for it to count");
		}
		catch(NumberFormatException | NoContentException e){
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
	
	private String formatDate(int hours, int minutes, int seconds){
		return format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	@Override
	public boolean stopAction(){
		isAlive = false;
		return true;
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			TIMER
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "Setup a timer and the bot will send a message when it's over!";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp("Sets the hours of the timer.", "h"),
			new ParametersHelp("Sets the minutes of the timer.", "m"),
			new ParametersHelp("Sets the seconds of the timer.", "s"),
		};
	}
	
}
