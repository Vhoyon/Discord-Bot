package commands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class Command {
	
	private String content;
	private Buffer buffer;
	private MessageReceivedEvent event;
	
	protected String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public Buffer getBuffer(){
		return buffer;
	}
	
	public void setBuffer(Buffer buffer){
		this.buffer = buffer;
	}
	
	public void setContext(MessageReceivedEvent event){
		this.event = event;
	}
	
	protected TextChannel getTextContext(){
		return event.getTextChannel();
	}
	
	public abstract void action();
	
	protected void sendMessage(String messageToSend){
		
		getTextContext().sendMessage(messageToSend).complete();
		
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
