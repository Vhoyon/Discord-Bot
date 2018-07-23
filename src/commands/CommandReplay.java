package commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;

import javax.sound.midi.Track;

public class CommandReplay extends MusicCommands {
	@Override
	public void action(){
		
		if(getGuild() == null)
			return;
		
		connectIfNotPlaying();
		
		String trackSource = (String)getMemory("LATEST_SONG");
		
		MusicManager.get().loadTrack(this, trackSource);
	}
	
	@Override
	public Object getCalls(){
		return MUSIC_REPLAY;
	}
}
