package commands;

import java.util.Random;

public class Game extends Command {
	
	@Override
	public void action(){
		
		String[] jeux = getContent().split(",");
		
		Random ran = new Random();
		
		int num = ran.nextInt(jeux.length);
		
		sendMessage(jeux[num]);
		
	}
	
}
