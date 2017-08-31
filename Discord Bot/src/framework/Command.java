package framework;

import ressources.Commands;
import ressources.Ressources;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class Command implements Commands, Ressources {
	
	private String content;
	private Buffer buffer;
	private MessageReceivedEvent event;
	private String guildID;
	
	protected String getContent(){
		return content;
	}
	
	protected String[] getSplitContent(){
		return content.split(" ");
	}
	
	protected String[] getSplitContentMaxed(int maxSize){
		return content.split(" ", maxSize);
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
	
	public abstract void action();
	
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
