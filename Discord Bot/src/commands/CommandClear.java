package commands;

import java.util.List;

import framework.CommandConfirmed;
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
public class CommandClear extends CommandConfirmed {
	
	public CommandClear(){
		super("Are you sure you want to clear all messages?");
	}
	
	@Override
	public void confirmed(){
		
		try{
			
			fullClean();
			
		}
		catch(PermissionException e){
			sendMessage("I do not have the permissions for that!");
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
			sendInfoMessage("One or more messages couldn't be deleted. Cannot bulk delete message more than 2 weeks old. Please try again earlier.");
		
	}
	
}
