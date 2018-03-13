package commands;

import java.util.List;

import utilities.specifics.CommandConfirmed;
import errorHandling.BotError;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 * 
 * @author Stephano
 *
 *         Classe qui permet d'effacer tout les messages dans le chat ou on
 *         l'invoque
 *
 */
public class CommandClear extends CommandConfirmed {
	
	@Override
	public String getConfMessage(){
		return getStringEz("ConfirmMessage");
	}
	
	@Override
	public void confirmed(){
		
		try{
			
			fullClean();
			
		}
		catch(PermissionException e){
			sendMessage(getStringEz("NoPermission"));
		}
		
	}
	
	private void fullClean() throws PermissionException{
		
		boolean hadErrors = false;
		
		boolean vide = false;
		MessageHistory history = getTextContext().getHistory();
		do{
			
			List<Message> messages = history.retrievePast(100).complete();
			
			if(!messages.isEmpty()){
				
				try{
					getTextContext().deleteMessages(messages).complete();
				}
				catch(IllegalArgumentException e){
					hadErrors = true;
				}
				
			}
			else{
				vide = true;
			}
			
		}while(!vide);
		
		if(hadErrors)
			new BotError(this, getStringEz("CannotBulkDeleteOld"));
		
	}
	
}
