package commands;

import java.util.ArrayList;

import errorHandling.BotError;
import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;

public class CommandGameInitial extends GameInteractionCommands {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			new BotError(this, lang("ErrorUsage",
					buildVCommand(getDefaultCall()
							+ " [game 1],[game 2],[game 3],[...]")), false);
		}
		else{
			
			String[] games = getContent().split(",");
			
			for(int i = 0; i < games.length; i++)
				games[i] = games[i].trim();
			
			GamePool gamepool = new GamePool(games);
			
			remember(gamepool, BUFFER_GAMEPOOL);
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(lang("GamesReadyToBeRolledMessage"));
			
			for(int i = 1; i <= gamepool.size(); i++)
				messages.add(i + ". `" + gamepool.get(i - 1) + "`");
			
			groupAndSendMessages(messages);
			
			sendInfoMessage(lang("ViewListAgainMessage",
					buildVCommand(GAME_LIST)));
			
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			GAME
		};
	}
	
}
