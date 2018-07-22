package app;

import errorHandling.BotError;
import errorHandling.BotErrorPrivate;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.abstracts.SimpleTextCommand;
import utilities.interfaces.Commands;
import utilities.interfaces.Resources;
import utilities.specifics.CommandConfirmed;
import vendor.abstracts.AbstractBotCommand;
import vendor.abstracts.AbstractCommandRouter;
import vendor.exceptions.NoCommandException;
import vendor.interfaces.Command;
import vendor.interfaces.Emojis;
import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.objects.*;
import vendor.utilities.CommandsThreadManager;
import vendor.utilities.formatting.DiscordFormatter;
import vendor.utilities.settings.Setting;

import java.util.ArrayList;

public class CommandRouter extends AbstractCommandRouter implements Resources,
		Commands, Emojis, DiscordFormatter {
	
	public CommandRouter(MessageReceivedEvent event, String receivedMessage,
			Buffer buffer, CommandsRepository commandsRepo){
		super(event, receivedMessage, buffer, commandsRepo);
	}
	
	@Override
	protected Request createRequest(String receivedMessage, Dictionary dict){
		return new Request(receivedMessage, dict, getCommandPrefix(),
				getCommandParameterPrefix());
	}
	
	@Override
	public void run(){
		
		Request request = getRequest();
		MessageEventDigger eventDigger = getEventDigger();
		
		try{
			
			setCommand(validateMessage());
			
			if(request.getCommandNoFormat().startsWith(getCommandPrefix())){
				
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
						
						getAbstractBotCommand().action();
						setCommand(null);
					}
					
					if(!confirmationConfirmed){
						
						String commandName = request.getCommand();
						
						if(CommandsThreadManager.isCommandRunning(commandName,
								eventDigger, this)){
							
							setCommand(new BotError(lang(
									"CommandIsRunningError", code(commandName))));
							
						}
						else{
							
							setCommand(getCommandsRepo().getContainer()
									.initiateLink(commandName));
							
						}
						
					}
					
				}
				
				try{
					
					ParametersHelp[] commandParamsHelp = getAbstractBotCommand()
							.getParametersDescriptions();
					
					if(commandParamsHelp != null){
						ArrayList<ArrayList<String>> paramsHelpMap = new ArrayList<>();
						
						for(ParametersHelp commandParamHelp : commandParamsHelp){
							paramsHelpMap.add(commandParamHelp.getAllParams());
						}
						
						getRequest().setParamLinkMap(paramsHelpMap);
					}
					
				}
				catch(NullPointerException e){}
				
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
	
	@Override
	public Command commandWhenFromPrivate(){
		return new BotErrorPrivate(
				ital(lang("MessageReceivedFromPrivateResponse")), true);
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
		
		try{
			
			String textChannelKey = getEventDigger().getChannelKey();
			
			String settingsKey = Utils
					.buildKey(textChannelKey, BUFFER_SETTINGS);
			
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
		catch(Exception e){
			return Request.DEFAULT_COMMAND_PREFIX;
		}
		
	}
	
	@Override
	public char getCommandParameterPrefix(){
		
		try{
			
			String textChannelKey = getEventDigger().getChannelKey();
			
			String settingsKey = Utils
					.buildKey(textChannelKey, BUFFER_SETTINGS);
			
			Setting settings = (Setting)getBuffer().get(settingsKey);
			
			char paramPrefix = settings.getFieldValue("param_prefix");
			
			return paramPrefix;
			
		}
		catch(Exception e){
			return Request.DEFAULT_PARAMETER_PREFIX;
		}
		
	}
	
}
