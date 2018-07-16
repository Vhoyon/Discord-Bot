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
				
				setCommand(validateMessage());
				
				if(getCommand() == null){
					
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
						setCommand(new BotError(request.getError(), false));
						
						getCommand().action();
						setCommand(null);
					}
					
					if(!confirmationConfirmed){
						
						String commandName = request.getCommand();
						
						if(CommandsThreadManager.isCommandRunning(commandName,
								eventDigger, this)){
							
							setCommand(new BotError(lang(
									"CommandIsRunningError", commandName)));
							
						}
						else{
							
							setCommand(getCommandsRepo().getContainer()
									.initiateLink(commandName));
							
						}
						
					}
					
				}
				
				try{
					
					getAbstractBotCommand().action();
					
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
			
			settings = new Setting(getDictionary(), SETTINGS);
			
			getBuffer().push(settings, settingsKey);
			
		}
		else{
			settings = (Setting)getBuffer().get(settingsKey);
		}
		
		String prefix = settings.getFieldValue("prefix");
		
		return prefix;
		
	}
	
}
