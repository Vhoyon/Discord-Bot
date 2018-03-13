package commands;

import java.util.ArrayList;
import java.util.Random;

import errorHandling.BotError;
import utilities.Command;
import utilities.interfaces.Ressources;
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
			
			getBuffer().push(gamepool, Ressources.BUFFER_GAMEPOOL);
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(getStringEz("GamesReadyToBeRolledMessage"));
			
			for(int i = 1; i <= gamepool.size(); i++)
				messages.add(i + ". `" + gamepool.get(i - 1) + "`");
			
			groupAndSendMessages(messages);
			
			sendInfoMessage(getStringEz("InitialViewListAgainMessage",
					buildVCommand(GAME_LIST)));
			
		}
		
	}
	
	private void add(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			try{
				
				GamePool gamepool = (GamePool)getBuffer().get(
						Ressources.BUFFER_GAMEPOOL);
				
				gamepool.add(getContent());
				
				sendMessage(getStringEz("AddedGameSuccessMessage", getContent()));
				
			}
			catch(NullPointerException e){
				
				sendInfoMessage(getStringEz("AddErrorNoPoolCreated",
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
				
				GamePool gamepool = (GamePool)getBuffer().get(
						Ressources.BUFFER_GAMEPOOL);
				
				if(isParameterPresent("all")){
					
					do{
						gamepool.remove(0);
					}while(!gamepool.isEmpty());
					
				}
				else{
					
					if(gamepool.remove(getContent()))
						sendMessage(getStringEz("RemovedGameSuccessMessage",
								getContent()));
					else
						new BotError(this, getStringEz("RemoveErrorNoSuchGame",
								buildVCommand(GAME_LIST)));
					
				}
				
				if(gamepool.isEmpty()){
					
					getBuffer().remove(Ressources.BUFFER_GAMEPOOL);
					sendMessage(getStringEz("RemoveIsNowEmpty"));
					
				}
				
			}
			catch(NullPointerException e){
				sendInfoMessage(getStringEz("RemoveErrorEmptyPool"));
			}
			
		}
		
	}
	
	private void roll(){
		
		try{
			
			GamePool gamepool = (GamePool)getBuffer().get(
					Ressources.BUFFER_GAMEPOOL);
			
			int wantedRoll = 1;
			
			if(getContent() != null)
				try{
					wantedRoll = Integer.parseInt(getContent());
				}
				catch(NumberFormatException e){}
			
			Random ran = new Random();
			int num;
			
			if(wantedRoll < 1)
				new BotError(getStringEz("RollNumberIsNotValid"));
			else if(wantedRoll == 1){
				
				num = ran.nextInt(gamepool.size());
				
				sendMessage(getStringEz("RolledGameMessage", gamepool.get(num)));
				
			}
			else{
				
				for(int i = 1; i <= wantedRoll; i++){
					
					num = ran.nextInt(gamepool.size());
					
					sendMessage(getStringEz("RolledMultipleGamesMessage", i,
							gamepool.get(num)));
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendInfoMessage(
					getStringEz("RollErrorPoolEmpty", buildVCommand(GAME
							+ " [game 1],[game 2],[...]")), false);
		}
		catch(IllegalArgumentException e){
			sendInfoMessage(
					getStringEz("RollErrorUsageMessage",
							buildVCommand(GAME_ROLL),
							buildVCommand(GAME_ROLL_ALT),
							buildVCommand(GAME_ROLL + " [positive number]"),
							buildVCommand(GAME_ROLL_ALT + " [positive number]")),
					false);
		}
		
	}
	
	private void list(){
		
		GamePool gamepool = null;
		
		try{
			gamepool = (GamePool)getBuffer().get(Ressources.BUFFER_GAMEPOOL);
		}
		catch(NullPointerException e){}
		
		if(gamepool == null){
			sendMessage(getStringEz("ListNoGamesInPoolMessage"));
		}
		else{
			
			ArrayList<String> messages = new ArrayList<>();
			
			messages.add(getStringEz("ListTitleOfList"));
			
			for(int i = 0; i < gamepool.size(); i++)
				messages.add((i + 1) + ". `" + gamepool.get(i) + "`");
			
			groupAndSendMessages(messages);
			
		}
		
	}
	
	private void error(){
		
		String message;
		
		switch(commandType){
		case INITIAL:
			message = getStringEz("ErrorInitialUsage", buildVCommand(GAME
					+ " [game 1],[game 2],[game 3],[...]"));
			break;
		case ADD:
			message = getStringEz("ErrorAddUsage", buildVCommand(GAME_ADD
					+ " [game name]"));
			break;
		case REMOVE:
			message = getStringEz("ErrorRemoveUsage", buildVCommand(GAME_REMOVE
					+ " [game name]"), buildVCommand(GAME_REMOVE + " "
					+ Request.Parameter.PREFIX + "all"));
			break;
		default:
			message = getStringEz("ErrorUndefined");
			break;
		}
		
		sendInfoMessage(message, false);
		
	}
}
