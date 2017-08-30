package commands;

import java.util.Random;

import framework.Command;
import framework.GamePool;
import ressources.Ressources;

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
			
			sendMessage("The following games are now ready to be rolled :");
			
			for(int i = 1; i <= gamepool.size(); i++)
				sendMessage(i + ". `" + gamepool.get(i - 1) + "`");
			
			sendMessage("Enter the command " + buildVCommand(GAME_LIST)
					+ " to view this list again.");
			
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
				
				sendMessage("The game `" + getContent()
						+ "` has been added to the pool!");
				
			}
			catch(NullPointerException e){
				
				sendMessage("Your game pool is not yet created.\nYou can create a game pool using "
						+ buildVCommand(GAME + " [game 1],[game 2],[...]")
						+ ".");
				
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
				
				String content = getContent();
				
				if(!content.equals("-all")){
					
					gamepool.remove(getContent());
					
					sendMessage("The game `" + getContent()
							+ "` has been removed from the game pool!");
					
				}
				else{
					
					do{
						gamepool.remove(0);
					}while(!gamepool.isEmpty());
					
				}
				
				if(gamepool.isEmpty()){
					
					getBuffer().remove(Ressources.BUFFER_GAMEPOOL);
					sendMessage("The game pool is now empty!");
					
				}
				
			}
			catch(NullPointerException e){
				sendMessage("You cannot remove a game from an empty game pool!");
			}
			
		}
		
	}
	
	private void roll(){
		
		try{
			
			GamePool gamepool = (GamePool)getBuffer().get(
					Ressources.BUFFER_GAMEPOOL);
			
			int wantedRoll = 1;
			
			if(getContent() != null)
				wantedRoll = Integer.parseInt(getContent());
			
			Random ran = new Random();
			int num;
			
			if(wantedRoll < 1)
				Integer.parseInt("Forcing Error");
			else if(wantedRoll == 1){
				
				num = ran.nextInt(gamepool.size());
				
				sendMessage("The random game selected is : `"
						+ gamepool.get(num) + "`!");
				
			}
			else{
				
				for(int i = 1; i <= wantedRoll; i++){
					
					num = ran.nextInt(gamepool.size());
					
					sendMessage(i + " game selected is : `" + gamepool.get(num)
							+ "`.");
					
				}
				
			}
			
		}
		catch(NullPointerException e){
			sendMessage("The game pool is empty!\nCreate a game pool using "
					+ buildVCommand(GAME + " [game 1],[game 2],[...]") + "!");
		}
		catch(NumberFormatException e){
			sendMessage("Usage :\n" + buildVCommand(GAME_ROLL) + " OR "
					+ buildVCommand(GAME_ROLL_ALT)
					+ " : Roll the dice once to get a random game.\n"
					+ buildVCommand(GAME_ROLL + " [positive number]") + " OR "
					+ buildVCommand(GAME_ROLL_ALT + " [positive number]")
					+ " : Roll a random game for the inputted number of time.");
		}
		
	}
	
	private void list(){
		
		GamePool gamepool = null;
		
		try{
			gamepool = (GamePool)getBuffer().get(Ressources.BUFFER_GAMEPOOL);
		}
		catch(NullPointerException e){}
		
		if(gamepool == null){
			sendMessage("No games in your pool mate!");
		}
		else{
			
			for(int i = 0; i < gamepool.size(); i++){
				sendMessage((i + 1) + ". `" + gamepool.get(i) + "`\n");
			}
			
		}
		
	}
	
	private void error(){
		
		String message;
		
		switch(commandType){
		case INITIAL:
			message = "Usage : "
					+ buildVCommand(GAME + " [game 1],[game 2],[game 3],[...]")
					+ ".\nSeparate games using commas.";
			break;
		case ADD:
			message = "Usage : " + buildVCommand(GAME_ADD + " [game name]")
					+ ".";
			break;
		case REMOVE:
			message = "Usage : `" + buildVCommand(GAME_REMOVE + " [game name]")
					+ ".\nYou could also input "
					+ buildVCommand(GAME_REMOVE + " -all")
					+ " to remove all the games in the current pool.";
			break;
		default:
			message = "An unsuspected error happened. What have you done.";
			break;
		}
		
		sendMessage(message);
		
	}
	
}
