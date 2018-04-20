package commands;

import java.util.ArrayList;

import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;

public class CommandGameList extends GameInteractionCommands {
	
	@Override
	public void action(){
		
		GamePool gamepool = null;
		
		try{
			gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
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
	public String[] getCalls(){
		return new String[]
		{
			GAME_LIST
		};
	}

	@Override
	public String getCommandDescription() {
		return "Display a list of all the games that you have in the games list";
	}
}
