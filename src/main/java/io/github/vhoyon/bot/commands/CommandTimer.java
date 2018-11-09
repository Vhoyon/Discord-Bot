package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.interfaces.Stoppable;
import io.github.vhoyon.vramework.objects.ParametersHelp;

/**
 * Command to create a timer in the TextChannel where this command was called
 * from.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
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
	
	/**
	 * Sets the class' variables using the remaining time as a seconds
	 * measurement.
	 * 
	 * @param remainingTime
	 *            Time in seconds that is remaining to update hours, minutes and
	 *            seconds variables.
	 * @since v0.4.0
	 */
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
	
	/**
	 * Formats the date of hours, minutes and seconds into a single string.
	 * 
	 * @param hours
	 *            The integer value of hours to show.
	 * @param minutes
	 *            The integer value of minutes to show.
	 * @param seconds
	 *            The integer value of seconds to show.
	 * @return A string formatted with time such that it returns
	 *         {@code HH:MM:SS}.
	 * @since v0.4.0
	 */
	private String formatDate(int hours, int minutes, int seconds){
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
