package commands;

import errorHandling.BotError;
import utilities.abstracts.MusicCommands;
import utilities.music.MusicManager;
import utilities.music.MusicPlayer;

public class CommandMusicLoop extends MusicCommands {


    @Override
    public void action() {


        if (getContent() == null) {

            if (!isPlaying()) {
                new BotError(this, lang("CommandMusicLoopNotPlaying"));
            } else {

//                remember(true, "LOOP_ONE");
//                remember(true, "LOOP_ONCE");
                if (this.hasMemory("MUSIC_LOOP") && (boolean) this.getMemory("MUSIC_LOOP")) {
                    forget("MUSIC_LOOP");
                } else {

                    remember(true, "MUSIC_LOOP");
                }
            }


        }

    }


    @Override
    public String[] getCalls() {
        return new String[]
                {
                        MUSIC_LOOP
                };
    }

    @Override
    public boolean stopAction() {
        return super.stopAction();
    }

    @Override
    public String getCommandDescription() {
        return "Skip the song that is currently playing";
    }
}
