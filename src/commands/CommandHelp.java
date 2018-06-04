package commands;

import errorHandling.BotError;
import utilities.BotCommand;
import vendor.exceptions.CommandNotFoundException;
import vendor.modules.Logger;

/**
 * Classe qui envois un message a l'utilisateur qui demande de l'aide avec une
 * liste complète des commandes.
 * 
 * @author Stephano
 */

public class CommandHelp extends BotCommand {
	
	@Override
	public void action(){
		
		String content = getContent();
		
		if(content == null){
			
			String fullHelpString = getRouter().getCommandsRepo()
					.getFullHelpString("Available commands :");
			
			sendPrivateMessage(fullHelpString);
			sendInfoMessage(lang("HelpSentMessage"));
			
		}
		else if(content.matches("-h")){
			String fullHelpString = getRouter().getCommandsRepo()
				.getFullHelpString("Available commands :");
			
			sendMessage(fullHelpString);
		}
		else{
			
			try{
				
				BotCommand commandToExplain = (BotCommand)getRouter()
						.getCommandsRepo().getContainer().findCommand(content);
				
				commandToExplain.putStateFromCommand(this);
				
				StringBuilder builder = new StringBuilder();
				
				String helpString = commandToExplain.getHelp(
						"*Available parameters :*", "Usage : "
								+ commandToExplain.getUsage() + ".", null);
				
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
	public Object getCalls(){
		return HELP;
	}
	
	@Override
	public String getCommandDescription(){
		return "Well you just used this command.So ;)";
	}
}
