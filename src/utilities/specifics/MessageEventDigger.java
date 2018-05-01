package utilities.specifics;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import vendor.interfaces.Utils;

public class MessageEventDigger {
    
    private MessageReceivedEvent event;

    public MessageEventDigger(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return this.event;
    }

    public String getGuildKey(){
        return Utils.buildKey(event.getGuild().getId());
    }
    
    public String getChannelKey(){
        return Utils.buildKey(getGuildId(), getChannelId());
    }
    
    public String getUserKey(){
        return Utils.buildKey(getUserName(), getUserId());
    }
    
    public String getCommandKey(String commandName){
        return Utils.buildKey(getChannelKey(), commandName);
    }
    
    public Guild getGuild(){
        return event.getGuild();
    }
    
    public String getGuildId(){
        return getGuild().getId();
    }
    
    public TextChannel getChannel(){
        return event.getTextChannel();
    }
    
    public String getChannelId(){
        return getChannel().getId();
    }
    
    public User getUser(){
        return event.getAuthor();
    }
    
    public String getUserId(){
        return getUser().getId();
    }
    
    public String getUserName(){
        return getUser().getName();
    }
    
}
