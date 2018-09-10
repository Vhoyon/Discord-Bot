package commands;

import errorHandling.BotError;
import utilities.BotCommand;
import vendor.interfaces.Stoppable;
import vendor.objects.ParametersHelp;

public class CommandTimer extends BotCommand implements Stoppable {
	
	private int seconds;
	private int hours;
	private int minutes;
	
	@Override
	public void action(){
		
		seconds = 0;
		hours = 0;
		minutes = 0;
		
		try{
			
			if(!hasParameter("h", "m", "s")){
				throw new NullPointerException();
			}
			
			// seconds = Integer.parseInt(constraints[0]);
			if(hasParameter("h")){
				hours = Integer.parseInt(getParameter("h").getContent());
			}
			if(hasParameter("m")){
				minutes = Integer.parseInt(getParameter("m").getContent());
			}
			if(hasParameter("s")){
				seconds = Integer.parseInt(getParameter("s").getContent());
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
		catch(InterruptedException | IllegalStateException e){}
		catch(NullPointerException e){
			sendMessage("You must give an amount of time to the "
					+ buildVCommand("timer") + " command for it to count");
		}
		catch(NumberFormatException e){
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
		return formatS("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	@Override
	public boolean stopAction(){
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
			new ParametersHelp("Sets the hours of the timer.", "h", "hour",
					"hours"),
			new ParametersHelp("Sets the minutes of the timer.", "m", "minute",
					"minutes"),
			new ParametersHelp("Sets the seconds of the timer.", "s", "second",
					"seconds"),
		};
	}
	
}
