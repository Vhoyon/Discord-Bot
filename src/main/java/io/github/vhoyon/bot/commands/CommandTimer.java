package io.github.vhoyon.bot.commands;

import io.github.ved.jrequester.Option;
import io.github.ved.jsanitizers.IntegerSanitizer;
import io.github.ved.jsanitizers.exceptions.BadFormatException;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.bot.utilities.interfaces.PartiallyParallelRunnable;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.interfaces.Stoppable;
import io.github.vhoyon.vramework.utilities.MessageManager;
import io.github.vhoyon.vramework.utilities.TimerManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
		
		if(!hasContent() && !getRequest().hasOptions()){
			
			try{
				
				int timeRemaining = TimerManager
						.getTimeRemaining("CommandTimer" + getKey()) / 1000;
				
				timeConstruct(timeRemaining);
				
				sendMessage(lang("RemainingTime",
						formatDate(hours, minutes, seconds)));
				
				return;
				
			}
			catch(NullPointerException e){}
			
		}
		
		seconds = 0;
		hours = 0;
		minutes = 0;
		
		try{
			
			if(!hasOption("h", "m", "s")){
				throw new NullPointerException();
			}
			
			if(hasOption("h")){
				hours = Integer.parseInt(getOption("h").getContent());
			}
			if(hasOption("m")){
				minutes = Integer.parseInt(getOption("m").getContent());
			}
			if(hasOption("s")){
				seconds = Integer.parseInt(getOption("s").getContent());
			}
			
			int totalTime = (hours * 3600) + (minutes * 60) + seconds;
			
			timeConstruct(totalTime);
			
			AtomicBoolean shouldShowTimer = new AtomicBoolean(hasOption("v",
					"r"));
			
			if(shouldShowTimer.get()){
				
				AtomicInteger refreshRateRef = new AtomicInteger(1);
				
				onOptionPresent(
						"r",
						option -> {
							try{
								
								int refreshRate = IntegerSanitizer
										.sanitizeValue(option.getContent(), 1,
												totalTime);
								
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
			new BotError(this, lang("ErrorAmountNotGiven",
					buildVCommand("timer")));
		}
		catch(NumberFormatException e){
			new BotError(this, lang("ErrorValueNotANumber"));
		}
		catch(BadFormatException e){
			
			switch(e.getErrorCode()){
			case 1:
				new BotError(this, lang("ErrorRateOptionEmpty"));
				break;
			case 2:
				new BotError(this, lang("ErrorRateValueNotANumber"));
				break;
			case 3:
				new BotError(this, lang("ErrorRateValueTooLow"));
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
		return lang("Description");
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(lang("OptionHour"), "h", "hour", "hours"),
			new Option(lang("OptionMinute"), "m", "minute", "minutes"),
			new Option(lang("OptionSecond"), "s", "second", "seconds"),
			new Option(lang("OptionVisual"), "v", "visual"),
			new Option(lang("OptionRate", buildVOption("v")), "r", "rate",
					"refresh_rate"),
		};
	}
	
	@Override
	public boolean duplicatedRunnableCondition(AbstractBotCommand thisCommand,
			AbstractBotCommand runningCommand){
		return thisCommand.getCommandName().equals(TIMER)
				&& !thisCommand.hasContent()
				&& !thisCommand.getRequest().hasOptions();
	}
	
}
