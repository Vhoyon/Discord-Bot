package commands;

import utilities.BotCommand;
import vendor.exceptions.NoContentException;
import errorHandling.BotError;
import vendor.objects.ParametersHelp;

public class CommandTimer extends BotCommand {
	
	private int seconds;
	private int hours;
	private int minutes;
	
	@Override
	public void action(){
		
		seconds = 0;
		hours = 0;
		minutes = 0;
		
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
			timeConstruct(totalTime);
			timerMessageId = sendMessage(formatDate(hours, minutes, seconds));
			
			for(int i = totalTime; i >= 0 && isAlive(); i--){
				
				if(i < totalTime){
					Thread.sleep(1000);
					
					timeConstruct(i);
					editMessageQueue(timerMessageId,
							formatDate(hours, minutes, seconds));
				}
				
			}
			
			if(isAlive())
				sendMessage("TimerEnded");
			
		}
		catch(InterruptedException e){}
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
			hours = remainingTime / 3600;
			remainingTime -= hours * 3600;
		}
		else
			hours = 0;
		if(remainingTime / 60 >= 0){
			minutes = remainingTime / 60;
			remainingTime -= minutes * 60;
		}
		else
			minutes = 0;
		if(remainingTime >= 0){
			seconds = remainingTime;
		}
		
	}
	
	private String formatDate(int hours, int minutes, int seconds){
		return format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	@Override
	public boolean stopAction(){
		kill();
		return true;
	}
	
	@Override
	public Object getCalls(){
		return TIMER;
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
