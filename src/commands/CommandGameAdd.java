package commands;

import errorHandling.BotError;
import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;

public class CommandGameAdd extends GameInteractionCommands {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			new BotError(this, lang("ErrorUsage",
					buildVCommand(getDefaultCall() + " [game name]")));
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
				
				gamepool.add(getContent());
				
				sendMessage(lang("AddedGameSuccessMessage", getContent()));
				
			}
			catch(NullPointerException e){
				
				sendInfoMessage(lang("ErrorNoPoolCreated", buildVCommand(GAME
						+ " [game 1],[game 2],[...]")));
				
			}
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			GAME_ADD
		};
	}
	
}
