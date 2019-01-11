package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.utilities.abstracts.GameInteractionCommands;
import io.github.vhoyon.bot.utilities.specifics.GamePool;

import java.util.ArrayList;

/**
 * Command that lists the games that are in the game pool and sends a message
 * with said list.
 * 
 * @version 1.0
 * @since v0.5.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandGameList extends GameInteractionCommands {
	
	@Override
	public void actions(){
		
		GamePool gamepool = null;
		
		try{
			gamepool = getMemory(BUFFER_GAMEPOOL);
		}
		catch(NullPointerException e){}
		
		if(gamepool == null){
			sendMessage(lang("NoGamesInPoolMessage"));
		}
		else{
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(lang("TitleOfList"));
			
			for(int i = 0; i < gamepool.size(); i++)
				messages.add((i + 1) + ". `" + gamepool.get(i) + "`");
			
			groupAndSendMessages(messages);
			
		}
		
	}
	
	@Override
	public String getCall(){
		return GAME_LIST;
	}
	
	@Override
	public String getCommandDescription(){
		return "Display a list of all the games that you have in the games list";
	}
	
}
