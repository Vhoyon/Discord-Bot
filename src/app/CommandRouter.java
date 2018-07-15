package app;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.abstracts.SimpleTextCommand;
import utilities.interfaces.*;
import utilities.specifics.*;
import vendor.abstracts.AbstractBotCommand;
import vendor.abstracts.AbstractCommandRouter;
import vendor.exceptions.NoCommandException;
import vendor.interfaces.Command;
import vendor.interfaces.Emojis;
import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.objects.*;
import errorHandling.BotError;
import errorHandling.BotErrorPrivate;
import vendor.utilities.CommandsThreadManager;
import vendor.utilities.settings.Setting;

public class CommandRouter extends AbstractCommandRouter implements Resources,
		Commands, Emojis {
	
	public CommandRouter(MessageReceivedEvent event, String receivedMessage,
			Buffer buffer, CommandsRepository commandsRepo){
		super(event, receivedMessage, buffer, commandsRepo);
	}
	
	@Override
	protected Request createRequest(String receivedMessage, Dictionary dict){
		return new Request(receivedMessage, dict, getCommandPrefix(),
				Resources.PARAMETER_PREFIX);
	}
	
	@Override
	public void run(){
		
		Request request = getRequest();
		MessageEventDigger eventDigger = getEventDigger();
		
		if(request.getCommandNoFormat().startsWith(getCommandPrefix())){
			
			try{
				
				if((command = validateMessage()) == null){
					
					String routerKey = eventDigger.getCommandKey(request
							.getCommand());
					
					this.setName(routerKey);
					
					boolean confirmationConfirmed = false;
					
					try{
						
						String textChannelKey = eventDigger.getChannelKey();
						
						Object needsConfirmation = getBuffer().get(
								BUFFER_CONFIRMATION, textChannelKey);
						
						CommandConfirmed confirmationObject = (CommandConfirmed)needsConfirmation;
						
						if(request.getCommand().equals(CONFIRM)){
							confirmationObject.confirmed();
							confirmationConfirmed = true;
						}
						else{
							
							confirmationObject.cancelled();
							
							if(request.getCommand().equals(CANCEL))
								confirmationConfirmed = true;
							
						}
						
						getBuffer().remove(BUFFER_CONFIRMATION, textChannelKey);
						
					}
					catch(NullPointerException e){}
					
					if(request.hasError()){
						command = new BotError(request.getError(), false);
						
						command.action();
						command = null;
					}
					
					if(!confirmationConfirmed){
						
						String commandName = request.getCommand();
						
						if(CommandsThreadManager.isCommandRunning(commandName,
								eventDigger, this)){
							
							command = new BotError(lang(
									"CommandIsRunningError", commandName));
							
						}
						else{
							
							command = getCommandsRepo().getContainer()
									.initiateLink(commandName);
							
						}
						
					}
					
				}
				
				try{
					
					AbstractBotCommand botCommand = (AbstractBotCommand)command;
					
					botCommand.setRouter(this);
					botCommand.setDictionary(getDictionary());
					
					command.action();
					
				}
				catch(NullPointerException e){}
				
			}
			catch(NoCommandException e){
				if(isDebugging())
					Logger.log(e);
			}
			
		}
		
	}
	
	@Override
	public Command commandWhenFromPrivate(){
		return new BotErrorPrivate("*"
				+ lang("MessageReceivedFromPrivateResponse") + "*", true);
	}
	
	@Override
	public Command commandWhenFromServerIsOnlyPrefix(){
		return new SimpleTextCommand(){
			@Override
			public String getTextToSend(){
				return lang("MessageIsOnlyPrefixResponse");
			}
		};
	}
	
	@Override
	public String getCommandPrefix(){
		
		String textChannelKey = getEventDigger().getChannelKey();
		
		String settingsKey = Utils.buildKey(textChannelKey, BUFFER_SETTINGS);
		
		boolean hasSettings = getBuffer().has(settingsKey);
		
		Setting settings;
		
		if(!hasSettings){
			
			settings = new Setting(SETTINGS);
			
			getBuffer().push(settings, settingsKey);
			
		}
		else{
			settings = (Setting)getBuffer().get(settingsKey);
		}
		
		String prefix = settings.getFieldValue("prefix");
		
		return prefix;
		
	}
	
}
