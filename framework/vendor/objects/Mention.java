package vendor.objects;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public class Mention {
	
	private MessageEventDigger digger;
	
	private User mentionnedUser;
	
	public Mention(String userId, MessageEventDigger digger){
		this.digger = digger;
		
		this.mentionnedUser = digger.getEvent().getJDA().getUserById(userId);
	}
	
	public boolean isMentionningSelf(){
		return this.getUser().equals(this.digger.getUser());
	}
	
	public User getUser(){
		return this.mentionnedUser;
	}
	
	public Member getAsMember(){
		return this.digger.getGuild().getMember(this.getUser());
	}
}
