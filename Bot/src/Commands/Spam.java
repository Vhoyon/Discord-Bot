package Commands;

import javax.swing.plaf.SliderUI;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Spam {
	public Spam(MessageReceivedEvent event) {
		long time = System.currentTimeMillis();
		for(int i = 0; i<150;i++){
			event.getTextChannel().sendMessage("a").complete();
			
		}
		System.out.println(System.currentTimeMillis() - time);
	}
}
