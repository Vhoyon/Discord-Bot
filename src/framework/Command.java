package framework;

import java.util.ArrayList;

import errorHandling.exceptions.*;
import framework.specifics.Request;
import framework.specifics.Request.Parameter;
import ressources.*;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class Command implements Commands, Ressources, Emojis {
	
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
	
	public boolean isParameterPresent(String parameterName){
		return getRequest().isParameterPresent(parameterName);
	}
	
	public String getString(String key){
		return getDictionary().getString(key);
	}
	
	public String getStringEz(String shortKey){
		return getDictionary().getString(getClass().getSimpleName() + shortKey);
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
	
	protected String sendMessage(String messageToSend){
		return sendMessage(messageToSend, (Object[])null);
	}
	
	protected String sendMessage(String messageToSend, Object... replacements){
		return getTextContext()
				.sendMessage(String.format(messageToSend, replacements))
				.complete().getId();
	}
	
	protected String sendPrivateMessage(String messageToSend){
		return sendPrivateMessage(messageToSend, (Object[])null);
	}
	
	protected String sendPrivateMessage(String messageToSend,
			Object... replacements){
		
		PrivateChannel channel = getEvent().getAuthor().openPrivateChannel()
				.complete();
		
		if(getEvent().getAuthor().hasPrivateChannel()){
			return channel
					.sendMessage(String.format(messageToSend, replacements))
					.complete().getId();
		}
		else{
			return sendMessage(getString("CommandUserHasNoPrivateChannel"));
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
	
	protected String sendInfoMessage(String messageToSend, boolean isOneLiner){
		return sendInfoMessage(messageToSend, isOneLiner, (Object[])null);
	}
	
	protected String sendInfoMessage(String messageToSend, boolean isOneLiner,
			Object... replacements){
		return sendMessage(createInfoMessage(messageToSend, isOneLiner,
				replacements));
	}
	
	protected String sendInfoMessage(String messageToSend){
		return sendInfoMessage(messageToSend, (Object[])null);
	}
	
	protected String sendInfoMessage(String messageToSend,
			Object... replacements){
		return sendInfoMessage(messageToSend, true, replacements);
	}
	
	protected String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		return sendInfoPrivateMessage(messageToSend, isOneLiner, (Object[])null);
	}
	
	protected String sendInfoPrivateMessage(String messageToSend){
		return sendInfoPrivateMessage(messageToSend, (Object[])null);
	}
	
	protected String sendInfoPrivateMessage(String messageToSend,
			Object... replacements){
		return sendInfoPrivateMessage(messageToSend, true, replacements);
	}
	
	protected String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner, Object... replacements){
		return sendPrivateMessage(createInfoMessage(messageToSend, isOneLiner,
				replacements));
	}
	
	protected String groupAndSendMessages(String[] messages){
		return groupAndSendMessages(messages, (Object[])null);
	}
	
	protected String groupAndSendMessages(String[] messages,
			Object... replacements){
		
		StringBuilder messageToSend = new StringBuilder();
		
		for(int i = 0; i < messages.length; i++){
			messageToSend.append(messages[i]);
			if(i < messages.length - 1){
				messageToSend.append("\n");
			}
		}
		
		return sendMessage(messageToSend.toString(), replacements);
		
	}
	
	protected String groupAndSendMessages(ArrayList<String> messages){
		return groupAndSendMessages(messages
				.toArray(new String[messages.size()]));
	}
	
	protected String groupAndSendMessages(ArrayList<String> messages,
			Object... replacements){
		return groupAndSendMessages(
				messages.toArray(new String[messages.size()]), replacements);
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
				
				if(usr.getEffectiveName().equals(BOT_NAME)){
					
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
