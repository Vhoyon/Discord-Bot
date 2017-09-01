package framework;

import java.util.ArrayList;

import framework.Request.Parameter;
import ressources.Commands;
import ressources.Ressources;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class Command implements Commands, Ressources {
	
	private String commandName;
	private String content;
	private Buffer buffer;
	private MessageReceivedEvent event;
	private String guildID;
	private Request request;
	
	public String getCommandName(){
		return commandName;
	}
	
	public void setCommandName(String commandName){
		this.commandName = commandName;
	}
	
	protected String getContent(){
		return content;
	}
	
	protected String[] getSplitContent(){
		
		if(content != null)
			return content.split(" ");
		else
			return null;
		
	}
	
	protected String[] getSplitContentMaxed(int maxSize){
		
		if(content != null)
			return content.split(" ", maxSize);
		else
			return null;
		
	}
	
	public void setContent(String content){
		this.content = content;
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
	
	public Parameter getParameter(String parameterName){
		return this.request.getParameter(parameterName);
	}
	
	public boolean isParameterPresent(Parameter parameter){
		return request.isParameterPresent(parameter);
	}
	
	public boolean isParameterPresent(String parameterName){
		return request.isParameterPresent(parameterName);
	}
	
	public abstract void action();
	
	public boolean stopAction(){
		return false;
	}
	
	protected void sendMessage(String messageToSend){
		
		getTextContext().sendMessage(messageToSend).complete();
		
	}
	
	protected void sendPrivateMessage(String messageToSend){
		
		PrivateChannel channel = getEvent().getAuthor().openPrivateChannel()
				.complete();
		if(getEvent().getAuthor().hasPrivateChannel()){
			channel.sendMessage(messageToSend).complete();
		}
		else{
			sendMessage("User has no private channel, cancelling.");
		}
		
	}
	
	protected void sendInfoMessage(String messageToSend, boolean isOneLiner){
		
		String infoChars = "\\~\\~";
		
		String separator;
		
		if(isOneLiner)
			separator = " ";
		else
			separator = "\n";
		
		sendMessage(infoChars + separator + messageToSend + separator
				+ infoChars);
		
	}
	
	protected void sendInfoMessage(String messageToSend){
		sendInfoMessage(messageToSend, true);
	}
	
	protected void groupAndSendMessages(String[] messages){
		
		StringBuilder messageToSend = new StringBuilder();
		
		for(int i = 0; i < messages.length; i++){
			messageToSend.append(messages[i]);
			if(i < messages.length - 1){
				messageToSend.append("\n");
			}
		}
		
		sendMessage(messageToSend.toString());
		
	}
	
	protected void groupAndSendMessages(ArrayList<String> messages){
		
		groupAndSendMessages(messages.toArray(new String[messages.size()]));
		
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
		
		String message = "The bot can not be disconnected if it is not in a voice channel.";
		
		for(VoiceChannel channel : event.getGuild().getVoiceChannels()){
			
			for(Member usr : channel.getMembers()){
				
				if(usr.getEffectiveName().equalsIgnoreCase("bot")){
					
					AudioManager man = event.getGuild().getAudioManager();
					
					man.closeAudioConnection();
					
					message = "The bot has disconnected";
					
					break;
					
				}
				
			}
			
		}
		
		sendMessage(message);
		
	}
	
}
