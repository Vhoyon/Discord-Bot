package commands;

import java.util.Random;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Game {

	public Game(MessageReceivedEvent event, String messageRecu) {
	  String[] jeux = messageRecu.split(",");
	 Random ran = new Random();
	 int num = ran.nextInt(jeux.length);
	 event.getTextChannel().sendMessage(jeux[num]).complete();
	}

}
