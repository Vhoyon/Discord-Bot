package commands;

import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;
import errorHandling.BotError;
import vendor.objects.ParametersHelp;

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
	public void action(){
		
		if(!hasContent()){
			new BotError(this, lang("ErrorUsage",
					buildVCommand(getDefaultCall() + " [game name]"),
					buildVCommand(getDefaultCall() + " "
							+ buildParameter("all"))), false);
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
				
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
	public Object getCalls(){
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
			new ParametersHelp("Removes all games from the list.", "a", "all")
		};
	}
	
}
