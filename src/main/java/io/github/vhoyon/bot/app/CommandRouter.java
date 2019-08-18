package io.github.vhoyon.bot.app;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.ved.jrequester.Option;
import io.github.ved.jrequester.Request;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.errorHandling.BotErrorPrivate;
import io.github.vhoyon.bot.utilities.abstracts.SimpleTextCommand;
import io.github.vhoyon.bot.utilities.interfaces.Commands;
import io.github.vhoyon.bot.utilities.interfaces.PartiallyParallelRunnable;
import io.github.vhoyon.bot.utilities.interfaces.Resources;
import io.github.vhoyon.bot.utilities.specifics.CommandConfirmed;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.abstracts.AbstractCommandRouter;
import io.github.vhoyon.vramework.exceptions.NoCommandException;
import io.github.vhoyon.vramework.interfaces.Command;
import io.github.vhoyon.vramework.modules.Audit;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.objects.CommandsRepository;
import io.github.vhoyon.vramework.objects.MessageEventDigger;
import io.github.vhoyon.vramework.utilities.CommandsThreadManager;
import io.github.vhoyon.vramework.utilities.formatting.DiscordFormatter;
import io.github.vhoyon.vramework.utilities.settings.Setting;

/**
 * This is the custom Router for Vhoyon's bot that routes common commands to
 * their appropriate actions, such as handling confirmations, calling Request's
 * linkages, etc.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see io.github.vhoyon.vramework.abstracts.AbstractCommandRouter
 */
public class CommandRouter extends AbstractCommandRouter implements Resources,
		Commands, DiscordFormatter {
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractCommandRouter
	 */
	public CommandRouter(MessageReceivedEvent event, Buffer buffer,
			CommandsRepository commandsRepo){
		super(event, buffer, commandsRepo);
	}
	
	@Override
	protected Request createRequest(String receivedMessage){
		return new Request(receivedMessage, getCommandPrefix(),
				getCommandOptionPrefix());
	}
	
	@Override
	public void route(){
		
		Request request = getRequest();
		MessageEventDigger eventDigger = getEventDigger();
		
		try{
			
			setCommand(validateMessage());
			
			if(request.isCommand()){
				
				Audit.audit(request.getInitialMessage());
				
				if(getCommand() == null){
					
					String routerKey = eventDigger.getCommandKey(request
							.getCommand());
					
					this.setName(routerKey);
					
					boolean confirmationConfirmed = false;
					
					try{
						
						String textChannelKey = eventDigger
								.getChannelKey(BUFFER_CONFIRMATION);
						
						CommandConfirmed confirmationObject = getBuffer().get(
								textChannelKey);
						
						if(request.getCommand().equals(CONFIRM)){
							confirmationObject.confirmed();
							confirmationConfirmed = true;
						}
						else{
							
							confirmationObject.cancelled();
							
							if(request.getCommand().equals(CANCEL))
								confirmationConfirmed = true;
							
						}
						
						getBuffer().remove(textChannelKey);
						
					}
					catch(IllegalStateException e){}
					
					if(request.hasError()){
						setCommand(new BotError(
								request.getDefaultErrorMessage(), false));
						
						getAbstractBotCommand().action();
						setCommand(null);
					}
					
					if(!confirmationConfirmed){
						
						String commandName = request.getCommand();
						
						AbstractBotCommand command = CommandsThreadManager
								.getCommandRunning(commandName, eventDigger,
										this);
						
						boolean notDuplicateCommand = command == null;
						
						if(!notDuplicateCommand
								&& command instanceof PartiallyParallelRunnable){
							
							setCommand(getLinkableCommand(commandName));
							
							AbstractBotCommand currentCommand = getAbstractBotCommand();
							
							notDuplicateCommand = ((PartiallyParallelRunnable)command)
									.duplicatedRunnableCondition(
											currentCommand, command);
							
						}
						
						if(!notDuplicateCommand){
							
							setCommand(new BotError(lang(
									"CommandIsRunningError", code(commandName))));
							
						}
						else if(getCommand() == null){
							setCommand(getLinkableCommand(commandName));
						}
						
					}
					
				}
				
				try{
					
					Option[] commandOptions = getAbstractBotCommand()
							.getOptions();
					
					if(commandOptions != null){
						
						getRequest().setOptions(commandOptions);
						
					}
					
				}
				catch(NullPointerException e){}
				
			}
			
			AbstractBotCommand command = getAbstractBotCommand();
			
			if(command != null){
				
				try{
					command.action();
				}
				catch(Exception e){
					Logger.log(e);
				}
				
			}
			
		}
		catch(NoCommandException e){
			if(isDebugging())
				Logger.log(e.getMessage(), Logger.LogType.INFO);
		}
		
	}
	
	@Override
	public Command commandWhenBotMention(){
		return new SimpleTextCommand(){
			@Override
			public String getTextToSend(){
				String userMention = getUser().getAsMention();
				String botNickname = bold(getBotMember().getNickname());
				String formattedHelp = buildVCommand(HELP);
				String formattedHelpCommand = buildVCommand(HELP + " [command]");
				
				return lang("MessageWhenOnlyMention", userMention, botNickname,
						formattedHelp, formattedHelpCommand);
			}
		};
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
			
			if(getRequest() != null && getRequest().getCommandPrefix() != null)
				return getRequest().getCommandPrefix();
			
			return getSettings().getSettingValue("prefix");
			
		}
		catch(Exception e){
			return Request.DEFAULT_COMMAND_PREFIX;
		}
		
	}
	
	@Override
	public char getCommandOptionPrefix(){
		
		try{
			
			if(getRequest() != null && getRequest().getOptionsPrefix() != 0)
				return getRequest().getOptionsPrefix();
			
			return getSettings().getSettingValue("param_prefix");
			
		}
		catch(Exception e){
			return Request.DEFAULT_OPTION_PREFIX;
		}
		
	}
	
	@Override
	protected Setting<Object>[] getDefaultSettings(){
		return SETTINGS;
	}
	
}
