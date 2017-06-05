import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class MessageListener extends ListenerAdapter{
	
	 @Override
	    public void onMessageReceived(MessageReceivedEvent event)
	    {
	       String messageRecu = event.getMessage().getContent();
	       TextChannel textChannel =  event.getTextChannel();
	        if(messageRecu.contains("!")){
	        	if(messageRecu.equalsIgnoreCase("!hello")){
	        		textChannel.sendMessage("hello " + event.getAuthor().getName()).complete();
	        	}else if(messageRecu.equalsIgnoreCase("!help")){
	        		
	        		event.getAuthor().openPrivateChannel();
	        		if(event.getAuthor().hasPrivateChannel()){
	        			event.getAuthor().getPrivateChannel().sendMessage("Help is here").complete();
	        		}
	        	}
	        }
	    }
	 
	 

}
