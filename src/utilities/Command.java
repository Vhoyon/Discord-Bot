package utilities;

import java.util.ArrayList;

import errorHandling.exceptions.*;
import utilities.interfaces.*;
import utilities.specifics.Request;
import utilities.specifics.Request.Parameter;
import vendor.modules.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command implements Commands, Ressources, Emojis, Utils {
	
	private Buffer buffer;
	private MessageReceivedEvent event;
	private Guild guild;
	private Request request;
	private Dictionary dictionary;
	
	public Command(){}
	
	public Command(Command commandToCopy){
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
		
		if(request.getContent() != null)
			return request.getContent().split(" ");
		else
			return null;
		
	}
	
	protected String[] getSplitContentMaxed(int maxSize){
		
		if(request.getContent() != null)
			return request.getContent().split(" ", maxSize);
		else
			return null;
		
	}
	
	public Buffer getBuffer(){
		
		buffer.setLatestGuildID(getGuildId());
		
		return buffer;
		
	}
	
	public void setBuffer(Buffer buffer){
		this.buffer = buffer;
	}
	
	public boolean remember(Object object, String associatedName){
		return getBuffer().push(object, associatedName);
	}
	
	public Object getMemory(String associatedName) throws NullPointerException{
		return getBuffer().get(associatedName);
	}
	
	public boolean forget(String associatedName){
		return getBuffer().remove(associatedName);
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
		return getGuild().getId();
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
	
	public boolean isParameterPresent(Parameter parameter){
		return getRequest().isParameterPresent(parameter);
	}
	
	public boolean isParameterPresent(Parameter... parameter){
		return getRequest().isParameterPresent(parameter);
	}
	
	public boolean isParameterPresent(String parameterName){
		return getRequest().isParameterPresent(parameterName);
	}
	
	public boolean isParameterPresent(String... parameterNames){
		return getRequest().isParameterPresent(parameterNames);
	}
	
	public String getString(String key){
		return getDictionary().getString(key);
	}
	
	public String getStringEz(String shortKey){
		return getString(getClass().getSimpleName() + shortKey);
	}
	
	public String getString(String key, Object... replacements){
		return getDictionary().getString(key, replacements);
	}
	
	public String getStringEz(String shortKey, Object... replacements){
		return getString(getClass().getSimpleName() + shortKey, replacements);
	}
	
	public void setDictionary(Dictionary dictionary){
		this.dictionary = dictionary;
	}
	
	public Dictionary getDictionary(){
		return dictionary;
	}
	
	public abstract void action();
	
	public boolean stopAction(){
		return false;
	}
	
	public String sendMessage(String messageToSend){
		return getTextContext().sendMessage(messageToSend).complete().getId();
	}
	
	public String sendPrivateMessage(String messageToSend){
		
		PrivateChannel channel = getEvent().getAuthor().openPrivateChannel()
				.complete();
		
		if(getUser().hasPrivateChannel()){
			return channel.sendMessage(messageToSend).complete().getId();
		}
		else{
			return sendMessage(getString("CommandUserHasNoPrivateChannel"));
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
	
}
