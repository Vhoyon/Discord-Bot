package io.github.vhoyon.bot.utilities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 * Handles individual tracks codec parameters.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */
public class AudioHandler implements AudioSendHandler {
	
	private final AudioPlayer audioPlayer;
	private AudioFrame lastFrame;
	
	public AudioHandler(AudioPlayer audioPlayer){
		this.audioPlayer = audioPlayer;
	}
	
	@Override
	public boolean canProvide(){
		if(lastFrame == null)
			lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}
	
	@Override
	public byte[] provide20MsAudio(){
		byte[] data = canProvide() ? lastFrame.getData() : null;
		lastFrame = null;
		
		return data;
	}
	
	@Override
	public boolean isOpus(){
		return true;
	}
	
}
