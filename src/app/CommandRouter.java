package app;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utilities.*;
import utilities.interfaces.*;
import utilities.specifics.*;
import vendor.interfaces.Emojis;
import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.objects.CommandLinksContainer;
import vendor.objects.Dictionary;
import commands.SimpleTextCommand;
import errorHandling.BotError;
import errorHandling.BotErrorPrivate;
import errorHandling.exceptions.NoCommandException;

public class CommandRouter extends Thread implements Resources, Commands,
		Emojis, Utils {
	
	private MessageReceivedEvent event;
	private Request request;
	private Buffer buffer;
	private Command command;
	private Dictionary dict;
	private CommandLinksContainer commandLinks;
	
	public CommandRouter(MessageReceivedEvent event, String messageRecu,
			Buffer buffer, CommandsRepository commandsRepo){
		
		this.event = event;
		this.buffer = buffer;
		
		try{
			
			Object bufferedDict = buffer.get(BUFFER_LANG, event.getGuild()
					.getId());
			dict = (Dictionary)bufferedDict;
			
		}
		catch(NullPointerException e){
			
			dict = new Dictionary();
			buffer.push(dict, BUFFER_LANG, event.getGuild().getId());
			
		}
		
		commandsRepo.setDictionary(dict);
		
		commandLinks = commandsRepo.getContainer();
		
		this.request = new Request(messageRecu, dict);
		
	}
	
	public Command getCommand(){
		return command;
	}
	
	public String getString(String key){
		return dict.getDirectString(key);
	}
	
	public String getString(String key, Object... replacements){
		return dict.getDirectString(key, replacements);
	}
	
	public void log(String message){
		Logger.log(message);
	}
	
	public void log(String message, boolean appendDate){
		Logger.log(message, appendDate);
	}
	
	@Override
	public void run(){
		
		if(request.getCommandNoFormat().startsWith(PREFIX)){
			
			try{
				
				String commandGuildID = null;
				
				if((command = validateMessage()) == null){
					
					commandGuildID = event.getGuild().getId();
					
					this.setName(request.getCommand() + commandGuildID);
					
					boolean confirmationConfirmed = false;
					
					try{
						
						Object needsConfirmation = buffer.get(
								BUFFER_CONFIRMATION, commandGuildID);
						
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
						
						buffer.remove(BUFFER_CONFIRMATION, commandGuildID);
						
					}
					catch(NullPointerException e){}
					
					if((command = request.getError()) != null){
						command.setContext(event);
						command.action();
						command = null;
					}
					
					if(!confirmationConfirmed){
						
						if(CommandsThreadManager.isCommandRunning(
								request.getCommand(), commandGuildID, this)){
							command = new BotError(getString(
									"CommandIsRunningError",
									request.getCommand()));
						}
						else{
							command = buildCommandFromName(
									request.getCommand(), commandGuildID);
						}
						
					}
					
				}
				
				try{
					
					command.setRouter(this);
					command.setContext(event);
					command.setBuffer(buffer);
					command.setRequest(request);
					command.setDictionary(dict);
					
					command.action();
					
				}
				catch(NullPointerException e){}
				
			}
			catch(NoCommandException e){
				if(isDebugging())
					log(e.getMessage());
			}
			
		}
		
	}
	
	/**
	 * Method that validates the message received and return the command to
	 * execute if it is not validated. In the case where the message received
	 * isn't a command (a message that starts with
	 * <i>Ressources.<b>PREFIX</b></i>), a <i>NoCommandException</i> is thrown.
	 * <p>
	 * If the message received is from a private channel, a
	 * {@link errorHandling.BotErrorPrivate BotErrorPrivate} command is created,
	 * having the message that <i>you need to be in a server to intercat with
	 * the bot</i>.
	 * <p>
	 * In another case where the received message in a server is only "
	 * <code>!!</code>" (the <code><i>PREFIX</i></code> value), a
	 * {@link commands.SimpleTextCommand SimpleTextCommand} command is created
	 * that will send the message
	 * "<i>... you wanted to call upon me or...?</i>".
	 * 
	 * @return <code>null</code> if valid; a command to execute otherwise.
	 * @throws NoCommandException
	 *             Generic exception thrown if the message isn't a command.
	 */
	private Command validateMessage() throws NoCommandException{
		
		Command command = null;
		
		// Only interactions are through a server, no single conversations permitted!
		if(event.isFromType(ChannelType.PRIVATE)){
			
			command = new BotErrorPrivate("*"
					+ getString("MessageReceivedFromPrivateResponse") + "*",
					true);
			
		}
		else if(event.isFromType(ChannelType.TEXT)){
			
			if(!request.getCommandNoFormat().matches(PREFIX + ".+")){
				throw new NoCommandException();
			}
			else{
				
				if(request.getCommand().equals(PREFIX)){
					
					command = new SimpleTextCommand(
							getString("MessageIsOnlyPrefixResponse"));
					
				}
				
			}
			
		}
		
		return command;
		
	}
	
	private Command buildCommandFromName(String commandName, String guildId){
		
		Command command = null;
		
		command = (Command)commandLinks.initiateLink(commandName);
		
		//		case HELLO:
		//			command = new SimpleTextCommand(getString("HelloResponse"), event
		//					.getAuthor().getName());
		//			break;
		//		case STOP:
		//			command = new CommandStop(getCommandRunning(request.getContent(),
		//					guildId));
		//			break;
		//		case TEST:
		//			command = new Command(){
		//				@Override
		//				public void action(){
		//					
		//					sendMessage(lang("TestingReplacements", event
		//							.getAuthor().getName()));
		//					
		//				}
		//			};
		//			break;
		
		return command;
	}
}
