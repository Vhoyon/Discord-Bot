package commands;

import java.util.List;

import utilities.Command;
import utilities.specifics.CommandConfirmed;
import errorHandling.BotError;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 * Classe qui permet d'effacer tout les messages dans le chat ou on l'invoque.
 * 
 * @author Stephano
 */
public class CommandClear extends Command {
	
	@Override
	public void action(){
		
		new CommandConfirmed(this){
			
			@Override
			public String getConfMessage(){
				return lang("ConfirmMessage");
			}
			
			@Override
			public void confirmed(){
				
				try{
					
					fullCleanReworked();
					
				}
				catch(PermissionException e){
					sendMessage(lang("NoPermission"));
				}
				
			}
			
		};
		
	}
	
	private void fullCleanReworked() throws PermissionException{
		
		MessageHistory history = getTextContext().getHistory();
		
		boolean isEmpty;
		
		boolean deletingIndividually = false;
		
		do{
			
			List<Message> messages = history.retrievePast(100).complete();
			
			if(!(isEmpty = messages.isEmpty())){
				
				if(!deletingIndividually){
					
					try{
						getTextContext().deleteMessages(messages).complete();
					}
					catch(IllegalArgumentException e){
						deletingIndividually = true;
					}
					
				}
				
				if(deletingIndividually){
					
					for(Message message : messages){
						
						message.delete().queue();
						
					}
					
				}
				
			}
			
		}while(!isEmpty && isAlive());
		
		if(isAlive())
			sendMessage("Cleared everything!");
		
	}
	
	/**
	 * @deprecated
	 * @throws PermissionException
	 */
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
			new BotError(this, lang("CannotBulkDeleteOld"));
		
	}
	
	@Override
	public boolean stopAction(){
		kill();
		
		return true;
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			CLEAR
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "Clear all the messages that in the text channel you execute the command!";
	}
	
}
