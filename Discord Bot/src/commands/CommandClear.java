package commands;

import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 * 
 * @author Stephano
 *
 *         Classe qui permet d'éffacer tout les messages dans le chat ou on
 *         l'invoque
 *
 */
public class CommandClear extends Command {
	
	@Override
	public void action(){
		
		try{
			
			fullClean();
			
		}
		catch(PermissionException e){
			sendMessage("I do not have the permissions for that!");
		}
		
	}
	
	private void fullClean(){
		
		boolean vide = false;
		MessageHistory history = getTextContext().getHistory();
		do{
			
			List<Message> messages = history.retrievePast(100).complete();
			
			if(!messages.isEmpty()){
				
				getTextContext().deleteMessages(messages).complete();
				
			}
			else{
				vide = true;
			}
			
		}while(!vide);
		
	}
	
}
