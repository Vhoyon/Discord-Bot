import java.util.Set;

import ressources.*;
import commands.*;
import commands.GameInteractionCommand.CommandType;
import errorHandling.*;
import errorHandling.exceptions.*;
import framework.*;
import framework.specifics.CommandConfirmed;
import framework.specifics.Request;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRouter extends Thread implements Ressources, Commands,
		Emojis, Utils {
	
	private MessageReceivedEvent event;
	private Request request;
	private Buffer buffer;
	private Command command;
	private Dictionary dict;
	
	public CommandRouter(MessageReceivedEvent event, String messageRecu,
			Buffer buffer){
		
		this.event = event;
		this.buffer = buffer;
		
		try{
			
			Object object = buffer.get(BUFFER_LANG);
			dict = (Dictionary)object;
			
		}
		catch(NullPointerException e){
			
			dict = new Dictionary();
			buffer.push(dict, BUFFER_LANG);
			
		}
		
		this.request = new Request(messageRecu, dict);
		
	}
	
	public Command getCommand(){
		return command;
	}
	
	public String getString(String key){
		return dict.getString(key);
	}
	
	public String getString(String key, Object... replacements){
		return dict.getString(key, replacements);
	}
	
	@Override
	public void run(){
		
		try{
			
			String commandGuildID = null;
			
			if((command = validateMessage()) == null){
				
				commandGuildID = event.getGuild().getId();
				
				this.setName(request.getCommand() + commandGuildID);
				
				buffer.setLatestGuildID(commandGuildID);
				
				boolean confirmationConfirmed = false;
				
				try{
					
					Object needsConfirmation = buffer.get(BUFFER_CONFIRMATION);
					
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
					
					buffer.remove(BUFFER_CONFIRMATION);
					
				}
				catch(NullPointerException e){}
				
				if((command = request.getErrorMessage()) != null){
					command.setContext(event);
					command.action();
					command = null;
				}
				
				if(!confirmationConfirmed){
					
					if(isCommandRunning(request.getCommand(), commandGuildID)){
						command = new BotError(getString(
								"CommandIsRunningError", request.getCommand()));
					}
					else{
						command = buildCommandFromName(request.getCommand(),
								commandGuildID);
					}
					
				}
				
			}
			
			try{
				
				command.setContext(event);
				command.setBuffer(buffer);
				command.setRequest(request);
				command.setDictionary(dict);
				
				command.action();
				
			}
			catch(NullPointerException e){
				if(isDebugging())
					e.printStackTrace();
			}
			
		}
		catch(NoCommandException e){
			if(isDebugging())
				e.printStackTrace();
		}
		
	}
	
	/**
	 * Method that determines whether a command is running by scanning all the
	 * threads used in the server of the <code>guildID</code> parameter, looking
	 * for the desired <code>command</code> parameter.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param guildID
	 *            The server's <code>guildID</code> required to search for
	 *            commands running in said server.
	 * @return The command found with all of it's attribute in a
	 *         <code>Command</code> object, <code>null</code> if the command
	 *         wasn't found.
	 */
	private Command getCommandRunning(String commandName, String guildID){
		
		Command commandFound = null;
		
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		
		for(Thread thread : threadArray){
			
			if(thread instanceof CommandRouter && !thread.equals(this)
					&& thread.getName().equals(commandName + guildID)){
				
				commandFound = ((CommandRouter)thread).getCommand();
				break;
				
			}
			
		}
		
		return commandFound;
		
	}
	
	/**
	 * Method that quickly tells if a command is running based off its name in
	 * the guild provided in parameters.
	 * <p>
	 * Internally, this uses the method <code>getCommandRunning()</code> and
	 * tests if that returns <code>null</code> or not.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param guildID
	 *            The server's <code>guildID</code> required to search for
	 *            commands running in said server.
	 * @return <code>true</code> if the command is running in the specified
	 *         guild id, <code>false</code> otherwise.
	 */
	private boolean isCommandRunning(String commandName, String guildID){
		return getCommandRunning(commandName, guildID) != null;
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
		
		Command command;
		
		switch(commandName){
		case HELLO:
			command = new SimpleTextCommand(getString("HelloResponse"), event
					.getAuthor().getName());
			break;
		case HELP:
			command = new CommandHelp();
			break;
		case MUSIC_PLAY:
			command = new CommandMusic(CommandMusic.CommandType.PLAY);
			break;
		case MUSIC_PAUSE:
			command = new CommandMusic(CommandMusic.CommandType.PAUSE);
			break;
		case MUSIC_SKIP:
			command = new CommandMusic(CommandMusic.CommandType.SKIP);
			break;
		case MUSIC_SKIP_ALL1:
		case MUSIC_SKIP_ALL2:
		case MUSIC_SKIP_ALL3:
			command = new CommandMusic(CommandMusic.CommandType.SKIP_ALL);
			break;
		case MUSIC_DISCONNECT:
			command = new CommandMusic(CommandMusic.CommandType.DISCONNECT);
			break;
		case MUSIC_VOLUME:
			command = new CommandMusic(CommandMusic.CommandType.VOLUME);
			break;
		case MUSIC_LIST:
			command = new CommandMusic(CommandMusic.CommandType.LIST);
			break;
		case CLEAR:
			command = new CommandClear();
			break;
		case SPAM:
			command = new CommandSpam();
			break;
		case TERMINATE:
			command = new SimpleTextCommand(getString("TERMINATE"));
			break;
		case STOP:
			command = new CommandStop(getCommandRunning(request.getContent(),
					guildId));
			break;
		case GAME:
			command = new GameInteractionCommand(CommandType.INITIAL);
			break;
		case GAME_ADD:
			command = new GameInteractionCommand(CommandType.ADD);
			break;
		case GAME_REMOVE:
			command = new GameInteractionCommand(CommandType.REMOVE);
			break;
		case GAME_ROLL:
		case GAME_ROLL_ALT:
			command = new GameInteractionCommand(CommandType.ROLL);
			break;
		case GAME_LIST:
			command = new GameInteractionCommand(CommandType.LIST);
			break;
		case TIMER:
			command = new CommandTimer();
			break;
		case LANGUAGE:
		case LANG:
			command = new CommandLanguage();
			break;
		case TEST:
			command = new Command(){
				@Override
				public void action(){
					
					sendMessage(getString("TestingReplacements", event
							.getAuthor().getName()));
					
				}
			};
			break;
		default:
			command = new BotError(getString("NoActionForCommand",
					buildVCommand(request.getCommand())), false);
			break;
		}
		
		return command;
	}
}
