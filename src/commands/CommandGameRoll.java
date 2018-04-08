package commands;

import java.util.Random;

import utilities.abstracts.GameInteractionCommands;
import utilities.specifics.GamePool;
import errorHandling.BotError;

public class CommandGameRoll extends GameInteractionCommands {
	
	@Override
	public void action(){
		
		try{
			
			GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
			
			int wantedRoll = 1;
			
			if(getContent() != null)
				try{
					wantedRoll = Integer.parseInt(getContent());
				}
				catch(NumberFormatException e){}
			
			Random ran = new Random();
			int num;
			
			if(wantedRoll < 1)
				new BotError(lang("NumberIsNotValid"));
			else if(wantedRoll == 1){
				
				num = ran.nextInt(gamepool.size());
				
				sendMessage(lang("RolledGameMessage", gamepool.get(num)));
				
			}
			else{
				
				for(int i = 1; i <= wantedRoll; i++){
					
					num = ran.nextInt(gamepool.size());
					
					sendMessage(lang("RolledMultipleGamesMessage", i,
							gamepool.get(num)));
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendInfoMessage(
					lang("ErrorPoolEmpty", buildVCommand(GAME
							+ " [game 1],[game 2],[...]")), false);
		}
		catch(IllegalArgumentException e){
			sendInfoMessage(
					lang("ErrorUsageMessage", buildVCommand(GAME_ROLL),
							buildVCommand(GAME_ROLL_ALT),
							buildVCommand(GAME_ROLL + " [positive number]"),
							buildVCommand(GAME_ROLL_ALT + " [positive number]")),
					false);
		}
		
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			GAME_ROLL, GAME_ROLL_ALT
		};
	}
	
}
