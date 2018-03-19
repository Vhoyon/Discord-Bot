package commands;

import java.util.ArrayList;
import java.util.Random;

import errorHandling.BotError;
import utilities.Command;
import utilities.specifics.GamePool;
import utilities.specifics.Request;

public class GameInteractionCommand extends Command {
	
	public enum CommandType{
		INITIAL, ADD, REMOVE, ROLL, LIST
	}
	
	CommandType commandType;
	
	public GameInteractionCommand(CommandType commandGameType){
		commandType = commandGameType;
	}
	
	@Override
	public void action(){
		
		switch(commandType){
		case INITIAL:
			initial();
			break;
		case ADD:
			add();
			break;
		case REMOVE:
			remove();
			break;
		case ROLL:
			roll();
			break;
		case LIST:
			list();
			break;
		}
		
	}
	
	private void initial(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			String[] jeux = getContent().split(",");
			
			for(int i = 0; i < jeux.length; i++)
				jeux[i] = jeux[i].trim();
			
			GamePool gamepool = new GamePool(jeux);
			
			remember(gamepool, BUFFER_GAMEPOOL);
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(lang("GamesReadyToBeRolledMessage"));
			
			for(int i = 1; i <= gamepool.size(); i++)
				messages.add(i + ". `" + gamepool.get(i - 1) + "`");
			
			groupAndSendMessages(messages);
			
			sendInfoMessage(lang("InitialViewListAgainMessage",
					buildVCommand(GAME_LIST)));
			
		}
		
	}
	
	private void add(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
				
				gamepool.add(getContent());
				
				sendMessage(lang("AddedGameSuccessMessage", getContent()));
				
			}
			catch(NullPointerException e){
				
				sendInfoMessage(lang("AddErrorNoPoolCreated",
						buildVCommand(GAME + " [game 1],[game 2],[...]")));
				
			}
			
		}
		
	}
	
	private void remove(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
				
				if(isParameterPresent("all")){
					
					do{
						gamepool.remove(0);
					}while(!gamepool.isEmpty());
					
				}
				else{
					
					if(gamepool.remove(getContent()))
						sendMessage(lang("RemovedGameSuccessMessage",
								getContent()));
					else
						new BotError(this, lang("RemoveErrorNoSuchGame",
								buildVCommand(GAME_LIST)));
					
				}
				
				if(gamepool.isEmpty()){
					
					forget(BUFFER_GAMEPOOL);
					sendMessage(lang("RemoveIsNowEmpty"));
					
				}
				
			}
			catch(NullPointerException e){
				sendInfoMessage(lang("RemoveErrorEmptyPool"));
			}
			
		}
		
	}
	
	private void roll(){
		
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
				new BotError(lang("RollNumberIsNotValid"));
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
					lang("RollErrorPoolEmpty", buildVCommand(GAME
							+ " [game 1],[game 2],[...]")), false);
		}
		catch(IllegalArgumentException e){
			sendInfoMessage(
					lang("RollErrorUsageMessage", buildVCommand(GAME_ROLL),
							buildVCommand(GAME_ROLL_ALT),
							buildVCommand(GAME_ROLL + " [positive number]"),
							buildVCommand(GAME_ROLL_ALT + " [positive number]")),
					false);
		}
		
	}
	
	private void list(){
		
		GamePool gamepool = null;
		
		try{
			gamepool = (GamePool)getMemory(BUFFER_GAMEPOOL);
		}
		catch(NullPointerException e){}
		
		if(gamepool == null){
			sendMessage(lang("ListNoGamesInPoolMessage"));
		}
		else{
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(lang("ListTitleOfList"));
			
			for(int i = 0; i < gamepool.size(); i++)
				messages.add((i + 1) + ". `" + gamepool.get(i) + "`");
			
			groupAndSendMessages(messages);
			
		}
		
	}
	
	private void error(){
		
		String message;
		
		switch(commandType){
		case INITIAL:
			message = lang("ErrorInitialUsage", buildVCommand(GAME
					+ " [game 1],[game 2],[game 3],[...]"));
			break;
		case ADD:
			message = lang("ErrorAddUsage", buildVCommand(GAME_ADD
					+ " [game name]"));
			break;
		case REMOVE:
			message = lang("ErrorRemoveUsage", buildVCommand(GAME_REMOVE
					+ " [game name]"), buildVCommand(GAME_REMOVE + " "
					+ Request.Parameter.PREFIX + "all"));
			break;
		default:
			message = lang("ErrorUndefined");
			break;
		}
		
		sendInfoMessage(message, false);
		
	}
}
