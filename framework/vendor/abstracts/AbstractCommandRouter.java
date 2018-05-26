package vendor.abstracts;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import vendor.exceptions.NoCommandException;
import vendor.interfaces.Command;
import vendor.interfaces.Utils;
import vendor.objects.*;

public abstract class AbstractCommandRouter extends Thread implements Utils {
	
	private Request request;
	private Buffer buffer;
	private Command command;
	private Dictionary dict;
	private CommandsRepository commandsRepo;
	private MessageEventDigger eventDigger;
	
	public AbstractCommandRouter(MessageReceivedEvent event,
			String receivedMessage, Buffer buffer,
			CommandsRepository commandsRepo){
		
		this.buffer = buffer;
		
		eventDigger = new MessageEventDigger(event);
		
		try{
			
			Object bufferedDict = buffer.get(Dictionary.BUFFER_LOCATION,
					eventDigger.getGuildId());
			dict = (Dictionary)bufferedDict;
			
		}
		catch(NullPointerException e){
			
			dict = new Dictionary();
			
			try{
				buffer.push(dict, Dictionary.BUFFER_LOCATION,
						eventDigger.getGuildId());
			}
			catch(NullPointerException e1){}
			
		}
		
		commandsRepo.setDictionary(dict);
		
		this.commandsRepo = commandsRepo;
		
		this.request = createRequest(receivedMessage, dict);
		
	}
	
	protected abstract Request createRequest(String receivedMessage,
			Dictionary dict);
	
	public Command getCommand(){
		return this.command;
	}
	
	public void setCommand(Command command){
		this.command = command;
	}
	
	public CommandsRepository getCommandsRepo(){
		return this.commandsRepo;
	}
	
	public MessageEventDigger getEventDigger(){
		return this.eventDigger;
	}
	
	public MessageReceivedEvent getEvent(){
		return getEventDigger().getEvent();
	}
	
	public Buffer getBuffer(){
		return this.buffer;
	}
	
	public Request getRequest(){
		return this.request;
	}
	
	public String getString(String key){
		return dict.getDirectString(key);
	}
	
	public String getString(String key, Object... replacements){
		return dict.getDirectString(key, replacements);
	}
	
	/**
	 * Method that validates the message received and return the command to
	 * execute if it is not validated. In the case where the message received
	 * isn't a command (a message that starts with
	 * <i>Ressources.<b>PREFIX</b></i>), a <i>NoCommandException</i> is thrown.
	 * <p>
	 * If the message received is from a Private Channel, the
	 * {@link #commandWhenFromPrivate()} method is called and returns its value.
	 * </p>
	 * <p>
	 * If the message is from a Text Channel but is only the text of the
	 * {@link #getCommandPrefix()}, the value of the
	 * {@link #commandWhenFromServerIsOnlyPrefix()} is returned.
	 * </p>
	 * 
	 * @return The content of {@link #commandIfValidated()} if valid; a command
	 *         to execute otherwise.
	 * @throws NoCommandException
	 *             Generic exception thrown if the message isn't a command.
	 */
	private Command validateMessage() throws NoCommandException{
		
		MessageReceivedEvent event = getEvent();
		
		if(event.isFromType(ChannelType.PRIVATE)){
			
			return commandWhenFromPrivate();
			
		}
		else if(event.isFromType(ChannelType.TEXT)){
			
			Request request = getRequest();
			String commandPrefix = getCommandPrefix();
			
			if(!request.getCommandNoFormat().matches(commandPrefix + ".+")){
				throw new NoCommandException();
			}
			else{
				
				if(request.getCommand().equals(commandPrefix)){
					
					return commandWhenFromServerIsOnlyPrefix();
					
				}
				
			}
			
		}
		
		return commandIfValidated();
		
	}
	
	/**
	 * @return <code>null</code> by default, can be overridden to return another
	 *         value for {@link #validateMessage()}.
	 */
	protected Command commandIfValidated(){
		return null;
	}
	
	public abstract Command commandWhenFromPrivate();
	
	public abstract Command commandWhenFromServerIsOnlyPrefix();
	
	public abstract String getCommandPrefix();
	
}
