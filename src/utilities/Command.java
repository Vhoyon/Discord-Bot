package utilities;

import java.util.ArrayList;

import app.CommandRouter;
import utilities.interfaces.*;
import vendor.abstracts.Translatable;
import vendor.exceptions.NoParameterContentException;
import vendor.interfaces.Emojis;
import vendor.interfaces.LinkableCommand;
import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.objects.Request;
import vendor.objects.Request.Parameter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command extends Translatable implements Commands,
		Resources, Emojis, Utils, LinkableCommand {
	
	private CommandRouter router;
	private Buffer buffer;
	private MessageReceivedEvent event;
	private Guild guild;
	private Request request;
	
	public Command(){}
	
	public Command(Command commandToCopy){
		setRouter(commandToCopy.getRouter());
		setContext(commandToCopy.getEvent());
		setBuffer(commandToCopy.getBuffer());
		setGuild(commandToCopy.getGuild());
		setRequest(commandToCopy.getRequest());
		setDictionary(commandToCopy.getDictionary());
	}
	
	public String getCommandName(){
		return request.getCommand();
	}
	
	protected String getContent(){
		return request.getContent();
	}
	
	protected String[] getSplitContent(){
		
		if(getContent() != null)
			return getContent().split(" ");
		else
			return null;
		
	}
	
	protected String[] getSplitContentMaxed(int maxSize){
		
		if(getContent() != null)
			return getContent().split(" ", maxSize);
		else
			return null;
		
	}
	
	public CommandRouter getRouter(){
		return router;
	}
	
	public void setRouter(CommandRouter router){
		this.router = router;
	}
	
	public Buffer getBuffer(){
		return buffer;
	}
	
	public void setBuffer(Buffer buffer){
		this.buffer = buffer;
	}
	
	public boolean remember(Object object, String associatedName){
		return getBuffer().push(object, associatedName, getGuildId());
	}
	
	public Object getMemory(String associatedName) throws NullPointerException{
		return getBuffer().get(associatedName, getGuildId());
	}
	
	public boolean forget(String associatedName){
		return getBuffer().remove(associatedName, getGuildId());
	}
	
	public boolean hasMemory(String associatedName){
		try{
			return getMemory(associatedName) != null;
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	protected MessageReceivedEvent getEvent(){
		return event;
	}
	
	public void setContext(MessageReceivedEvent event){
		this.event = event;
		
		this.guild = event.getGuild();
	}
	
	protected TextChannel getTextContext(){
		return event.getTextChannel();
	}
	
	public User getUser(){
		return getEvent().getAuthor();
	}
	
	public String getUsername(){
		return getUser().getName();
	}
	
	public Guild getGuild(){
		return this.guild;
	}
	
	public void setGuild(Guild guild){
		this.guild = guild;
	}
	
	public String getGuildId(){
		return getTextContext().getId();
	}
	
	public Request getRequest(){
		return request;
	}
	
	public void setRequest(Request request){
		this.request = request;
	}
	
	public ArrayList<Parameter> getParameters(){
		return this.getRequest().getParameters();
	}
	
	public Parameter getParameter(String parameterName)
			throws NoParameterContentException{
		return this.getRequest().getParameter(parameterName);
	}
	
	public boolean hasParameter(Parameter parameter){
		return this.getRequest().hasParameter(parameter);
	}
	
	public boolean hasParameter(Parameter... parameter){
		return this.getRequest().hasParameter(parameter);
	}
	
	public boolean hasParameter(String parameterName){
		return this.getRequest().hasParameter(parameterName);
	}
	
	public boolean hasParameter(String... parameterNames){
		return this.getRequest().hasParameter(parameterNames);
	}
	
	public abstract void action();
	
	public boolean stopAction(){
		return false;
	}
	
	public void connect(VoiceChannel voiceChannel){
		getGuild().getAudioManager().openAudioConnection(voiceChannel);
		
		remember(voiceChannel, BUFFER_VOICE_CHANNEL);
	}
	
	public VoiceChannel getConnectedVoiceChannel(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	public void disconnect(){
		
		if(getConnectedVoiceChannel() != null){
			
			getGuild().getAudioManager().closeAudioConnection();
			
		}
		
		forget(BUFFER_VOICE_CHANNEL);
		
	}
	
	public String sendMessage(String messageToSend){
		if(messageToSend == null){
			log("The bot attempted to send a null message - probably a fail safe, but concerning nontheless...");
			
			return null;
		}
		
		try{
			return this.getTextContext().sendMessage(messageToSend).complete()
					.getId();
		}
		catch(IllegalArgumentException e){
			log(e.getMessage());
			
			return null;
		}
	}
	
	public String sendPrivateMessage(String messageToSend){
		
		PrivateChannel channel = this.getUser().openPrivateChannel().complete();
		
		if(getUser().hasPrivateChannel()){
			
			if(messageToSend == null){
				log("The bot attempted to send a null message - probably a fail safe, but concerning nonetheless...");
				
				return null;
			}
			
			try{
				return channel.sendMessage(messageToSend).complete().getId();
			}
			catch(IllegalArgumentException e){
				log(e.getMessage());
				
				return null;
			}
			
		}
		else{
			return sendMessage(lang(true, "CommandUserHasNoPrivateChannel"));
		}
		
	}
	
	public String createInfoMessage(String messageToSend, boolean isOneLiner){
		
		String infoChars = "\\~\\~";
		
		String separator;
		
		if(isOneLiner)
			separator = " ";
		else
			separator = "\n";
		
		return infoChars + separator + messageToSend + separator + infoChars;
		
	}
	
	public String sendInfoMessage(String messageToSend, boolean isOneLiner){
		return sendMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String sendInfoMessage(String messageToSend){
		return sendInfoMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend){
		return sendInfoPrivateMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		return sendPrivateMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String groupAndSendMessages(String... messages){
		
		StringBuilder messageToSend = new StringBuilder();
		
		for(int i = 0; i < messages.length; i++){
			
			messageToSend.append(messages[i]);
			
			if(i < messages.length - 1){
				messageToSend.append("\n");
			}
			
		}
		
		return sendMessage(messageToSend.toString());
		
	}
	
	public String groupAndSendMessages(ArrayList<String> messages){
		return groupAndSendMessages(messages
				.toArray(new String[messages.size()]));
	}
	
	public void editMessage(String messageToEdit, String messageId){
		getTextContext().editMessageById(messageId, messageToEdit).complete();
	}
	
	public void log(String message){
		Logger.log(message);
	}
	
	@Override
	public String formatParameter(String parameterToFormat){
		return buildVParameter(parameterToFormat);
	}
	
}
