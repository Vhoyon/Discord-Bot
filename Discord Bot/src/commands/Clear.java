package commands;

import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

/**
 * 
 * @author Stephano
 *
 *         Classe qui permet d'éffacer tout les messages dans le chat ou on
 *         l'invoque
 *
 */
public class Clear extends Command {
	
	@Override
	public void action(){
		
		boolean vide = false;
		MessageHistory hist = getTextContext().getHistory();
		do{
			
			List<Message> messages = hist.retrievePast(100).complete();
			
			if(!messages.isEmpty()){
				
				getTextContext().deleteMessages(messages).complete();
				
			}
			else{
				vide = true;
			}
			
		}while(!vide);
		
	}
	
}
