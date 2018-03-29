package app;

import utilities.*;
import utilities.interfaces.Resources;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * 
 * @author Stephano
 *
 * <br>
 * <br>
 *         Cette classe extend <b>ListenerAdapter</b> recoit les commandes de
 *         l'utilisateur et appele les classes necessaires
 */
public class MessageListener extends ListenerAdapter implements Resources {
	
	private Buffer buffer;
	private CommandsRepository commandsRepo;
	
	public MessageListener(){
		buffer = Buffer.get();
		
		commandsRepo = new CommandsRepository(new CommandLinksBot());
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		String messageRecu = event.getMessage().getContentRaw();
		
		// Bots doesn't need attention...
		if(!event.getAuthor().isBot()){
			
			new CommandRouter(event, messageRecu, buffer, commandsRepo).start();
			
		}
		
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
		super.onGuildVoiceLeave(event);
		
		// No events from bots
		if(!event.getMember().getUser().isBot()){
			
			try{
				
				VoiceChannel playerVoiceChannel = (VoiceChannel)buffer.get(
						BUFFER_VOICE_CHANNEL, event.getGuild().getId());
				
				System.out.println(playerVoiceChannel);
				
				if(playerVoiceChannel.equals(event.getChannelLeft())){
					System.out.println("test leaves from same lel");
				}
				else{
					System.out.println("fuk");
				}
				
			}
			catch(NullPointerException e){
				System.out.println("hehe");
			}
			
		}
		
		
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event){
		super.onGuildVoiceMove(event);
		
		event.getChannelLeft();
		
		System.out.println("test moves between channels");
	}
	
}
