import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class MessageListener extends ListenerAdapter implements AudioManager{
	
	
	 @Override
	    public void onMessageReceived(MessageReceivedEvent event)
	    {
		   Message msg = event.getMessage();
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
	        	}else if(messageRecu.equalsIgnoreCase("!connect")){
	                VoiceChannel vc = null;
	                GuildManager gm = null;
	                for (VoiceChannel channel : event.getGuild().getVoiceChannels()) {
	                    vc = channel;
	                    for (Member usr: vc.getMembers()) {
	                        if (usr.getEffectiveName().equals(event.getAuthor().getName())){
	                            gm = new GuildManager(event.getGuild());
	                            ChannelManager cm = vc.getManager();
	                            break;
	                        }
	                    }
	                    
	                }
	                
	            }else if(messageRecu.contains("!play ") ){
	        		String playLink = messageRecu.substring(6);
	        		textChannel.sendMessage(playLink).complete();
	            
	        	}
	        	else if(messageRecu.equalsIgnoreCase("!clear") ){
	        		List<VoiceChannel> voiceChanel = event.getAuthor().getJDA().getVoiceChannels();
	        		for (VoiceChannel voiceChannel : voiceChanel) {
	        			List<Member> members = voiceChannel.getMembers();
	        			for (Member member : members) {
						}
					}
//	        		textChannel.deleteMessages((Collection<Message>) event.getTextChannel().getHistory().getRetrievedHistory().subList(0, textChannel.getHistory().size() + 1)).complete();
	            
	        	}
	        }
	    }

	@Override
	public void closeAudioConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getConnectTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public VoiceChannel getConnectedChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionListener getConnectionListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionStatus getConnectionStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guild getGuild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JDA getJDA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoiceChannel getQueuedAudioConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioReceiveHandler getReceiveHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioSendHandler getSendingHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAttemptingToConnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAutoReconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelfDeafened() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelfMuted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openAudioConnection(VoiceChannel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoReconnect(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConnectTimeout(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConnectionListener(ConnectionListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReceivingHandler(AudioReceiveHandler arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelfDeafened(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelfMuted(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSendingHandler(AudioSendHandler arg0) {
		// TODO Auto-generated method stub
		
	}
	 
	 

}
