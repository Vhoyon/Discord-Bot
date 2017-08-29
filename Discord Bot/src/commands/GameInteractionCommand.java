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
			
			GamePool gamepool = new GamePool(jeux);
			
			getBuffer().push(gamepool, "GamePool");
			
			sendMessage("Added games, whatever they were.");
			
		}
		
	}
	
	private void add(){
		
		if(getContent() == null){
			error();
		}
		else{
			
		}
		
	}
	
	private void remove(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			// TODO Remove from GamePool Object
			
		}
		
	}
	
	private void roll(){
		
		Random ran = new Random();
		
		GamePool gamepool = (GamePool)getBuffer().get("GamePool");
		
		int num = ran.nextInt(gamepool.size());
		
		sendMessage(gamepool.get(num));
		
	}
	
	private void list(){
		
		GamePool gamepool = (GamePool)getBuffer().get("GamePool");
		
		if(gamepool == null){
			sendMessage("No games in your pool mate!");
		}
		else{
			
			for(int i = 0; i < gamepool.size(); i++){
				sendMessage(gamepool.get(i) + "\n");
			}
			
		}
		
	}
	
	private void error(){
		
		String message;
		
		switch(commandType){
		case INITIAL:
			message = "Usage : **"
					+ Ressources.PREFIX
					+ "game [game 1],[game 2],[game 3],[...]** .\nSeparate games using commas.";
			break;
		case ADD:
			message = "Usage : **" + Ressources.PREFIX
					+ "game_add [game name]** .";
			break;
		case REMOVE:
			message = "Usage : **" + Ressources.PREFIX
					+ "game_remove [game name]** OR **" + Ressources.PREFIX
					+ "game_remove [game index]** .";
			break;
		default:
			message = "An unsuspected error happened. What have you done.";
			break;
		}
		
		sendMessage(message);
		
	}
	
}
