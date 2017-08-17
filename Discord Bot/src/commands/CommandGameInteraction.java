package commands;

import java.util.ArrayList;
import java.util.Random;

public class CommandGameInteraction extends Command {
	
	public enum CommandGameType{
		INITIAL, ADD, REMOVE, ROLL, LIST
	}
	
	CommandGameType commandType;
	GamePool games;
	
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
			
			games = new GamePool();
			
			Random ran = new Random();
			
			int num = ran.nextInt(jeux.length);
			
			sendMessage(jeux[num]);
			
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
		
	}
	
	private void list(){
		ArrayList<String> list = games.getJeux();
		for (int i = 0; i < list.size(); i++) {
			sendMessage(list.get(i));
		}
	}
	
	private void error(){
		
	}
	
}
