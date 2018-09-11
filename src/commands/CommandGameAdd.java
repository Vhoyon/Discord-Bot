package commands;

import errorHandling.BotError;
import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;

/**
 * Command that adds a game (simple message string) to the game pool for this
 * TextChannel.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandGameAdd extends GameInteractionCommands {
	
	@Override
	public void action(){
		
		if(!hasContent()){
			new BotError(this, lang("ErrorUsage",
					buildVCommand(getDefaultCall() + " [game name]")));
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
				
				gamepool.add(getContent());
				
				sendMessage(lang("AddedGameSuccessMessage", code(getContent())));
				
			}
			catch(NullPointerException e){
				
				new BotError(this, lang("ErrorNoPoolCreated",
						buildVCommand(GAME + " [game 1],[game 2],[...]")),
						false);
				
			}
			
		}
		
	}
	
	@Override
	public Object getCalls(){
		return GAME_ADD;
	}
	
	@Override
	public String getCommandDescription(){
		return "Add a game to the list of game!";
	}
	
}
