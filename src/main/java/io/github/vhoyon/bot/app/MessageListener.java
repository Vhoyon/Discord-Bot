package io.github.vhoyon.bot.app;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.vhoyon.bot.utilities.interfaces.Resources;
import io.github.vhoyon.bot.utilities.music.MusicManager;
import io.github.vhoyon.vramework.abstracts.AbstractCommandRouter;
import io.github.vhoyon.vramework.abstracts.AbstractMessageListener;
import io.github.vhoyon.vramework.abstracts.CommandsLinker;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.objects.CommandsRepository;
import io.github.vhoyon.vramework.objects.EventDigger;
import io.github.vhoyon.vramework.util.KeyBuilder;
import io.github.vhoyon.vramework.util.TimerManager;
import io.github.vhoyon.vramework.util.settings.SettingRepository;
import io.github.vhoyon.vramework.util.settings.SettingRepositoryRepository;

/**
 * This class implements the logic used by the
 * {@link io.github.vhoyon.vramework.abstracts.AbstractMessageListener
 * AbstractMessageListener} class to create the right
 * {@link io.github.vhoyon.vramework.abstracts.CommandsLinker
 * CommandsLinker} (our {@link BotCommandsLinker}) and create the appropriate
 * {@link io.github.vhoyon.vramework.abstracts.AbstractCommandRouter
 * AbstractCommandRouter} (our {@link CommandRouter}).
 * 
 * @version 1.0
 * @since 0.1.0
 * @author Stephano
 * @see io.github.vhoyon.vramework.abstracts.AbstractMessageListener
 */
public class MessageListener extends AbstractMessageListener implements
		Resources {
	
	@Override
	protected CommandsLinker createCommandLinker(){
		return new BotCommandsLinker();
	}
	
	@Override
	protected AbstractCommandRouter createRouter(MessageReceivedEvent event,
			Buffer buffer, CommandsRepository commandsRepo){
		return new CommandRouter(event, buffer, commandsRepo);
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
		
		if(!EventDigger.isUserBot(event.getMember())){
			triggerUserLeftEvent(event.getGuild());
		}
		
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event){
		
		if(!EventDigger.isUserBot(event.getMember())){
			
			Guild guild = event.getGuild();
			
			if(!stopTimerIfChannelIsSameOfBot(event.getChannelJoined(), guild)){
				
				triggerUserLeftEvent(guild);
				
			}
			
		}
		
	}
	
	private void triggerUserLeftEvent(Guild guild){
		
		if(MusicManager.get().hasPlayer(guild)
				&& !EventDigger.doesConnectedChannelHasHumansLeft(guild)){
			
			String userLeftGuildKey = KeyBuilder.buildGuildObjectKey(guild,
					"USER_LEFT");
			
			TimerManager.stopTimer("noPlayerDisconnect");
			
			Buffer buffer = Buffer.get();
			
			buffer.push(guild, userLeftGuildKey);
			
			SettingRepository settings = SettingRepositoryRepository
					.getSettingRepository(guild, SETTINGS);
			
			int aloneDropDelay = settings.getSettingValue("alone_drop_delay");
			
			TimerManager.schedule("noPlayerDisconnect", aloneDropDelay, () -> {
				
				if(!EventDigger.doesConnectedChannelHasHumansLeft(guild)){
					MusicManager.get().emptyPlayer(guild);
				}
				
			}, () -> buffer.remove(userLeftGuildKey));
			
		}
		
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
		
		if(!EventDigger.isUserBot(event.getMember())){
			
			stopTimerIfChannelIsSameOfBot(event.getChannelJoined(),
					event.getGuild());
			
		}
		
	}
	
	private boolean stopTimerIfChannelIsSameOfBot(VoiceChannel channel,
			Guild guild){
		
		if(channel.equals(guild.getAudioManager().getConnectedChannel())){
			
			TimerManager.stopTimer("noPlayerDisconnect");
			
			return true;
			
		}
		
		return false;
		
	}
	
}
