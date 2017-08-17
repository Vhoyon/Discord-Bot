package commands;

import java.util.ArrayList;
import java.util.Random;

import ressources.Ressources;

public class CommandGameInteraction extends Command {
	
	public enum CommandGameType{
		INITIAL, ADD, REMOVE, ROLL, LIST
	}
	
	CommandGameType commandType;
	
	public CommandGameInteraction(CommandGameType commandGameType){
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
			
		}
		
	}
	
	private void add(){
		
		if(getContent() == null){
			error();
		}
		else{
			
			// TODO Add to GamePool Object
			
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
		
		int num = ran.nextInt(gamepool.getJeux().size());
		
		sendMessage(gamepool.getJeux().get(num));
		
	}
	
	private void list(){
		
		ArrayList<String> list = ((GamePool)getBuffer().get("GamePool"))
				.getJeux();
		for(int i = 0; i < list.size(); i++){
			sendMessage(list.get(i) + "\n");
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
