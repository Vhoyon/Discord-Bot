package io.github.vhoyon.bot.commands;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.ved.jsanitizers.IntegerSanitizer;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.bot.utilities.interfaces.PartiallyParallelRunnable;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.interfaces.Stoppable;
import io.github.vhoyon.vramework.objects.ParametersHelp;
import io.github.vhoyon.vramework.utilities.MessageManager;
import io.github.vhoyon.vramework.utilities.TimerManager;

/**
 * Command to create a timer in the TextChannel where this command was called
 * from.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandTimer extends BotCommand implements Stoppable,
		PartiallyParallelRunnable {
	
	private int seconds;
	private int hours;
	private int minutes;
	
	@Override
	public void actions(){
		
		if(!hasContent() && !getRequest().hasParameters()){
			
			try{
				
				int timeRemaining = TimerManager
						.getTimeRemaining("CommandTimer" + getKey()) / 1000;
				
				timeConstruct(timeRemaining);
				
				sendMessage("Time remaining : "
						+ formatDate(hours, minutes, seconds));
				
				return;
				
			}
			catch(NullPointerException e){}
			
		}
		
		seconds = 0;
		hours = 0;
		minutes = 0;
		
		try{
			
			if(!hasParameter("h", "m", "s")){
				throw new NullPointerException();
			}
			
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
			
			timeConstruct(totalTime);
			
			AtomicBoolean shouldShowTimer = new AtomicBoolean(hasParameter("v",
					"r"));
			
			if(shouldShowTimer.get()){
				
				AtomicInteger refreshRateRef = new AtomicInteger(1);
				
				onParameterPresent(
						"r",
						parameter -> {
							try{
								
								int refreshRate = IntegerSanitizer
										.sanitizeValue(parameter.getContent(),
												1, totalTime);
								
								refreshRateRef.set(refreshRate);
								
							}
							catch(BadFormatException e){
								if(e.getErrorCode() != 4)
									throw e;
								
								shouldShowTimer.set(false);
							}
						});
				
				if(shouldShowTimer.get()){
					
					String timerMessageId = null;
					timerMessageId = sendMessage(formatDate(hours, minutes,
							seconds));
					
					int refreshRate = refreshRateRef.get();
					
					for(int i = totalTime; i >= 0 && isAlive(); i -= refreshRate){
						
						if(i < totalTime){
							Thread.sleep(1000 * refreshRate);
							
							timeConstruct(i);
							
							editMessageQueue(timerMessageId,
									formatDate(hours, minutes, seconds));
						}
						
					}
					
				}
				
			}
			
			if(!shouldShowTimer.get()){
				createTimerBackground(totalTime);
			}
			
			if(isAlive())
				sendMessage("TimerEnded");
			
		}
		catch(InterruptedException | IllegalStateException e){
			TimerManager.stopTimer("CommandTimer" + getKey());
		}
		catch(NullPointerException e){
			new BotError(this, "You must give an amount of time to the "
					+ buildVCommand("timer") + " command for it to count.");
		}
		catch(NumberFormatException e){
			new BotError(this, "One of the value provided isn't a number!");
		}
		catch(BadFormatException e){
			
			switch(e.getErrorCode()){
			case 1:
				new BotError(this, "Refresh rate's parameter cannot be empty!");
				break;
			case 2:
				new BotError(this,
						"The value given to the refresh rate parameter isn't a number!");
				break;
			case 3:
				new BotError(this,
						"The number given to the refresh rate parameter needs to be 1 or more!");
				break;
			}
			
		}
		
	}
	
	private void createTimerBackground(int totalTime)
			throws InterruptedException{
		
		MessageManager manager = setupMessageManager();
		
		int indice = 0;
		
		if(hours > 0){
			indice += 1;
			manager.addReplacement("hours", hours);
		}
		if(minutes > 0){
			indice += 2;
			manager.addReplacement("minutes", minutes);
		}
		if(seconds > 0){
			indice += 4;
			manager.addReplacement("seconds", seconds);
		}
		manager.addReplacement("timer", buildVCommand(TIMER));
		
		sendInfoMessage(manager.getMessage(indice, getDictionary(), this));
		
		Object handler = new Object();
		
		TimerManager.schedule("CommandTimer" + getKey(), totalTime * 1000,
				null, () -> {
					synchronized(handler){
						handler.notifyAll();
					}
				});
		
		synchronized(handler){
			handler.wait();
		}
		
	}
	
	private MessageManager setupMessageManager(){
		
		MessageManager manager = new MessageManager();
		
		manager.addMessage(1, "BackHours", "hours", "timer");
		manager.addMessage(2, "BackMinutes", "minutes", "timer");
		manager.addMessage(3, "BackHoursMinutes", "hours", "minutes", "timer");
		manager.addMessage(4, "BackSeconds", "seconds", "timer");
		manager.addMessage(5, "BackHoursSeconds", "hours", "seconds", "timer");
		manager.addMessage(6, "BackMinutesSeconds", "minutes", "seconds",
				"timer");
		manager.addMessage(7, "BackHoursMinutesSeconds", "hours", "minutes",
				"seconds", "timer");
		
		return manager;
		
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
	public String getCall(){
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
			new ParametersHelp("Displays the timer in real time.", "v",
					"visual"),
			new ParametersHelp(
					"Sets the rate (in seconds) at which the visual timer should be updated. This parameter implies the use of the parameter "
							+ buildVParameter("v")
							+ ", therefore making the latter redundant when this is used.",
					"r", "rate", "refresh_rate"),
		};
	}
	
	@Override
	public boolean duplicatedRunnableCondition(AbstractBotCommand thisCommand,
			AbstractBotCommand runningCommand){
		return thisCommand.getCommandName().equals(TIMER)
				&& !thisCommand.hasContent()
				&& !thisCommand.getRequest().hasParameters();
	}
	
}
