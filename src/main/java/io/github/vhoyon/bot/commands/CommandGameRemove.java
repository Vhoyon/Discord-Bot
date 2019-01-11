package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.GameInteractionCommands;
import io.github.vhoyon.bot.utilities.specifics.GamePool;
import io.github.vhoyon.vramework.objects.ParametersHelp;

/**
 * Command that removes a game from the game pool using its name as a search
 * mechanism.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandGameRemove extends GameInteractionCommands {
	
	@Override
	public void actions(){
		
		if(!hasContent()){
			new BotError(this, lang("ErrorUsage", buildVCommand(getCall()
					+ " [game name]"), buildVCommand(getCall() + " "
					+ buildParameter("all"))), false);
		}
		else{
			
			try{
				
				GamePool gamepool = getMemory(BUFFER_GAMEPOOL);
				
				if(hasParameter("all")){
					gamepool.clear();
				}
				else{
					
					if(gamepool.remove(getContent()))
						sendMessage(lang("RemovedGameSuccessMessage",
								code(getContent())));
					else
						new BotError(this, lang("ErrorNoSuchGame",
								buildVCommand(GAME_LIST)));
					
				}
				
				if(gamepool.isEmpty()){
					
					forget(BUFFER_GAMEPOOL);
					sendMessage(lang("IsNowEmpty"));
					
				}
				
			}
			catch(NullPointerException e){
				sendInfoMessage(lang("ErrorEmptyPool"));
			}
			
		}
		
	}
	
	@Override
	public String getCall(){
		return GAME_REMOVE;
	}
	
	@Override
	public String getCommandDescription(){
		return "Remove a game form the game list";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp("Removes all games from the list.", "a", "all"),
		};
	}
	
}
