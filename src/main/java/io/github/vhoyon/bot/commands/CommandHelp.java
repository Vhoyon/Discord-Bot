package io.github.vhoyon.bot.commands;

import io.github.ved.jrequester.Option;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.exceptions.CommandNotFoundException;
import io.github.vhoyon.vramework.modules.Logger;

/**
 * Command that send a message to the server by default with a help string
 * containing all of the commands available with the possibility to send it
 * privately, detailed or to get the help of another command by adding the
 * command's name as content.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano Mehawej
 */

public class CommandHelp extends BotCommand {
	
	@Override
	public void actions(){
		
		String content = getContent();
		
		if(content == null){
			
			boolean isFull = hasOption("f");
			
			String fullHelpString = getRouter().getCommandsRepo()
					.getFullHelpString("Available commands :", isFull);
			
			if(hasOption("p")){
				sendPrivateMessage(fullHelpString);
				sendInfoMessage(ital(lang("HelpSentMessage")));
			}
			else{
				sendMessage(fullHelpString);
			}
			
		}
		else{
			
			try{
				
				AbstractBotCommand commandToExplain = (AbstractBotCommand)getRouter()
						.getCommandsRepo().getContainer().findCommand(content);
				
				commandToExplain.putStateFromCommand(this);
				
				StringBuilder builder = new StringBuilder();
				
				String helpString = commandToExplain.getHelp("Usage : "
						+ commandToExplain.getUsage() + ".",
						ital("Available parameters :"), null);
				
				builder.append(helpString);
				
				if(hasOption("p")){
					sendPrivateMessage(builder.toString());
					sendInfoMessage(lang("HelpSentMessage"));
				}
				else{
					sendMessage(builder.toString());
				}
				
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
	public String getCall(){
		return HELP;
	}
	
	@Override
	public String getCommandDescription(){
		return "Well you just used this command.So ;)";
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(
					"Send the requested help string to your private channel.",
					false, "p", "private"),
			new Option("Shows the full descriptions of each commands inline.",
					false, "f", "full")
		};
	}
	
}
