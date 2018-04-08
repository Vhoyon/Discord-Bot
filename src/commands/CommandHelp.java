package commands;

import errorHandling.BotError;
import utilities.Command;
import vendor.exceptions.CommandNotFoundException;
import vendor.modules.Logger;

/**
 * Classe qui envois un message a l'utilisateur qui demande de l'aide avec une
 * liste complète des commandes.
 * 
 * @author Stephano
 */

public class CommandHelp extends Command {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		if(content == null){
			
			String test = getRouter().getCommandsRepo().getFullHelpString();
			
			sendPrivateMessage(test);
			sendInfoMessage(lang("HelpSentMessage"));
			
		}
		else{
			
			try{
				
				Command commandToExplain = (Command)getRouter()
						.getCommandsRepo().getContainer().findCommand(content);
				
				StringBuilder builder = new StringBuilder();
				
				String helpString = commandToExplain.getHelp(
						"*Available parameters :*", "Usage : "
								+ buildVCommand(content) + ".", null);
				
				builder.append(helpString);
				
				sendMessage(builder.toString());
				
			}
			catch(CommandNotFoundException e){
				new BotError(this, "The command " + buildVCommand(content)
						+ "does not exist!");
			}
			catch(Exception e){
				Logger.log(e);
			}
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			HELP
		};
	}
}
