package framework;

import java.util.ArrayList;

import errorHandling.exceptions.*;
import framework.specifics.Request;
import framework.specifics.Request.Parameter;
import ressources.*;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class Command implements Commands, Ressources, Emojis {
	
	private Buffer buffer;
	private MessageReceivedEvent event;
	private String guildID;
	private Request request;
	private Dictionary dictionary;
	
	public Command(){}
	
	public Command(Command commandToCopy){
		setContext(commandToCopy.getEvent());
		setBuffer(commandToCopy.getBuffer());
		setGuildID(commandToCopy.getGuildID());
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
		
		buffer.setLatestGuildID(getGuildID());
		
		return buffer;
		
	}
	
	public void setBuffer(Buffer buffer){
		this.buffer = buffer;
	}
	
	protected MessageReceivedEvent getEvent(){
		return event;
	}
	
	public void setContext(MessageReceivedEvent event){
		this.event = event;
	}
	
	protected TextChannel getTextContext(){
		return event.getTextChannel();
	}
	
	public String getGuildID(){
		return guildID;
	}
	
	public void setGuildID(String guildID){
		this.guildID = guildID;
	}
	
	public Request getRequest(){
		return request;
	}
	
	public void setRequest(Request request){
		this.request = request;
	}
	
	public ArrayList<Parameter> getParameters(){
		return this.request.getParameters();
	}
	
	public Parameter getParameter(String parameterName)
			throws NoParameterContentException{
		return this.request.getParameter(parameterName);
	}
	
	public boolean isParameterPresent(Parameter parameter){
		return request.isParameterPresent(parameter);
	}
	
	public boolean isParameterPresent(String parameterName){
		return request.isParameterPresent(parameterName);
	}
	
	public String getString(String key){
		return dictionary.getString(key);
	}
	
	public String getStringEz(String shortKey){
		return dictionary.getString(getClass().getSimpleName() + shortKey);
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
	
	protected void sendMessage(String messageToSend){
		sendMessage(messageToSend, (Object[])null);
	}
	
	protected void sendMessage(String messageToSend, Object... replacements){
		
		getTextContext()
				.sendMessage(String.format(messageToSend, replacements))
				.complete();
		getBuffer().push(getTextContext().getLatestMessageId(),
				BUFFER_LASTMSGID);
		
	}
	
	protected void sendPrivateMessage(String messageToSend){
		sendPrivateMessage(messageToSend, (Object[])null);
	}
	
	protected void sendPrivateMessage(String messageToSend,
			Object... replacements){
		
		PrivateChannel channel = getEvent().getAuthor().openPrivateChannel()
				.complete();
		if(getEvent().getAuthor().hasPrivateChannel()){
			channel.sendMessage(String.format(messageToSend, replacements))
					.complete();
		}
		else{
			sendMessage(getString("CommandUserHasNoPrivateChannel"));
		}
		
	}
	
	protected String createInfoMessage(String messageToSend, boolean isOneLiner){
		return createInfoMessage(messageToSend, isOneLiner, (Object[])null);
	}
	
	protected String createInfoMessage(String messageToSend,
			boolean isOneLiner, Object... replacements){
		
		String infoChars = "\\~\\~";
		
		String separator;
		
		if(isOneLiner)
			separator = " ";
		else
			separator = "\n";
		
		return infoChars + separator
				+ String.format(messageToSend, replacements) + separator
				+ infoChars;
		
	}
	
	protected void sendInfoMessage(String messageToSend, boolean isOneLiner){
		sendInfoMessage(messageToSend, isOneLiner, (Object[])null);
	}
	
	protected void sendInfoMessage(String messageToSend, boolean isOneLiner,
			Object... replacements){
		sendMessage(createInfoMessage(messageToSend, isOneLiner, replacements));
	}
	
	protected void sendInfoMessage(String messageToSend){
		sendInfoMessage(messageToSend, (Object[])null);
	}
	
	protected void sendInfoMessage(String messageToSend, Object... replacements){
		sendInfoMessage(messageToSend, true, replacements);
	}
	
	protected void sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		sendInfoPrivateMessage(messageToSend, isOneLiner, (Object[])null);
	}
	
	protected void sendInfoPrivateMessage(String messageToSend){
		sendInfoPrivateMessage(messageToSend, (Object[])null);
	}
	
	protected void sendInfoPrivateMessage(String messageToSend,
			Object... replacements){
		sendInfoPrivateMessage(messageToSend, true, replacements);
	}
	
	protected void sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner, Object... replacements){
		sendPrivateMessage(createInfoMessage(messageToSend, isOneLiner,
				replacements));
	}
	
	protected void groupAndSendMessages(String[] messages){
		groupAndSendMessages(messages, (Object[])null);
	}
	
	protected void groupAndSendMessages(String[] messages,
			Object... replacements){
		
		StringBuilder messageToSend = new StringBuilder();
		
		for(int i = 0; i < messages.length; i++){
			messageToSend.append(messages[i]);
			if(i < messages.length - 1){
				messageToSend.append("\n");
			}
		}
		
		sendMessage(messageToSend.toString(), replacements);
		
	}
	
	protected void groupAndSendMessages(ArrayList<String> messages){
		groupAndSendMessages(messages.toArray(new String[messages.size()]));
	}
	
	protected void groupAndSendMessages(ArrayList<String> messages,
			Object... replacements){
		groupAndSendMessages(messages.toArray(new String[messages.size()]),
				replacements);
	}
	
	protected void editMessage(String messageToEdit, String messageId){
		getTextContext().editMessageById(messageId, messageToEdit).complete();
	}
	
	public void connect(){
		
		VoiceChannel vc = null;
		//		GuildManager gm = null;
		
		for(VoiceChannel channel : event.getGuild().getVoiceChannels()){
			
			vc = channel;
			
			for(Member usr : vc.getMembers()){
				
				if(usr.getEffectiveName().equals(event.getAuthor().getName())){
					
					//					gm = new GuildManager(event.getGuild());
					
					//					ChannelManager cm = vc.getManager();
					
					AudioManager man = event.getGuild().getAudioManager();
					
					man.openAudioConnection(vc);
					break;
					
				}
				
			}
			
		}
		
	}
	
	public void disconnect(){
		
		String message = getString("CommandCannotDisconnectOrNotConnected");
		
		for(VoiceChannel channel : event.getGuild().getVoiceChannels()){
			
			for(Member usr : channel.getMembers()){
				
				if(usr.getEffectiveName().equalsIgnoreCase("bot")){
					
					AudioManager man = event.getGuild().getAudioManager();
					
					man.closeAudioConnection();
					
					message = getString("CommandDisconnectDisconnected");
					
					break;
					
				}
				
			}
			
		}
		
		sendMessage(message);
		
	}
	
	/**
	 * Method to go around the technicalities that emerges from creating methods
	 * using varargs as parameters.<br>
	 * You can use this to replace strings in
	 * messages sent - for example :<br>
	 * 
	 * <pre>
	 * new BotError(this, getStringEz(&quot;StringToSend&quot;), useThis(
	 * 		buildVCommand(command1), buildVCommand(command2)));
	 * </pre>
	 * 
	 * @param replacements
	 *            Objects to replace the string with.
	 * @return An object (<code>Object[]</code>) that is compliant to the
	 *         general localisations methods.
	 */
	public static Object[] useThis(Object... replacements){
		return replacements;
	}
	
}
