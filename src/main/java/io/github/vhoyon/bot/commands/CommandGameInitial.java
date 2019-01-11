package io.github.vhoyon.bot.commands;

import java.util.ArrayList;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.abstracts.GameInteractionCommands;
import io.github.vhoyon.bot.utilities.specifics.GamePool;

/**
 * Command that initializes the game pool and is required for other game
 * commands to be used.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandGameInitial extends GameInteractionCommands {
	
	@Override
	public void actions(){
		
		if(!hasContent()){
			new BotError(this, lang("ErrorUsage", buildVCommand(getActualCall()
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
	public String getCall(){
		return GAME;
	}
	
	@Override
	public String getCommandDescription(){
		return "Create a list of games that you usually play!";
	}
	
}
