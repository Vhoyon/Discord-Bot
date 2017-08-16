package commands;

import net.dv8tion.jda.core.entities.TextChannel;

public abstract class Command {
	
	private String content;
	private TextChannel textChannel;
	
	public Command(String content){
		
		this.setContent(content);
		
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setTextChannel(TextChannel textChannel){
		this.textChannel = textChannel;
	}
	
	public abstract void action();
	
	protected void sendMessage(String messageToSend){
		
		textChannel.sendMessage(messageToSend);
		
	}
	
}
