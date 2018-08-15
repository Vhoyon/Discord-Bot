package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;
import utilities.BotCommand;
import utilities.specifics.CommandConfirmed;
import vendor.objects.ParametersHelp;

import java.util.List;

/**
 * Classe qui permet d'effacer tout les messages dans le chat ou on l'invoque.
 * 
 * @author Stephano
 */
public class CommandClear extends BotCommand {
	
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
					
					MessageHistory history = getTextContext().getHistory();
					
					boolean isEmpty;
					
					boolean deletingIndividually = false;
					
					boolean waitForDeletionToContinue = hasParameter("s");
					
					do{
						
						List<Message> messages = history.retrievePast(100)
								.complete();
						
						if(!(isEmpty = messages.isEmpty())){
							
							if(!deletingIndividually){
								
								try{
									
									if(!waitForDeletionToContinue){
										getTextContext().deleteMessages(
												messages).queue();
									}
									else{
										getTextContext().deleteMessages(
												messages).complete();
									}
									
								}
								catch(IllegalArgumentException e){
									deletingIndividually = true;
								}
								
							}
							else{
								
								for(Message message : messages){
									
									if(!waitForDeletionToContinue){
										message.delete().queue();
									}
									else{
										message.delete().complete();
									}
									
								}
								
							}
							
						}
						
					}while(!isEmpty && isAlive());
					
					if(isAlive())
						sendMessage("Cleared everything!");
					
				}
				catch(PermissionException e){
					sendMessage(lang("NoPermission"));
				}
				
			}
			
		};
		
	}
	
	@Override
	public boolean stopAction(){
		return true;
	}
	
	@Override
	public Object getCalls(){
		return CLEAR;
	}
	
	@Override
	public String getCommandDescription(){
		return "Clear all the messages that in the text channel you execute the command!";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"Waits that the message has been successfully deleted before deleting the others. Useful if you are not sure if you should delete all the messages as you can stop the command.",
					false, "s", "slow")
		};
	}
	
}
