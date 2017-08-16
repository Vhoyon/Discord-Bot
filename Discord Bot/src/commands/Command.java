package commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
	
	private String content = null;
	private MessageReceivedEvent event;
	
	public Command(){}
	
	protected String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public void setContext(MessageReceivedEvent event){
		this.event = event;
	}
	
	protected TextChannel getTextContext(){
		return event.getTextChannel();
	}
	
	protected VoiceChannelInteraction getVoiceContext(){
		return new VoiceChannelInteraction(event);
	}
	
	public abstract void action();
	
	protected void sendMessage(String messageToSend){
		
		getTextContext().sendMessage(messageToSend).complete();
		
	}
	
}
