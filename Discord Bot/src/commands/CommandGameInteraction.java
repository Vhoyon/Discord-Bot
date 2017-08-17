package commands;

import java.util.Random;

public class CommandGameInteraction extends Command {
	
	public enum CommandGameType{
		INITIAL, ADD, REMOVE, ROLL
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
		}
		
	}
	
	private void initial(){
		
		String[] jeux = getContent().split(",");
		
		Random ran = new Random();
		
		int num = ran.nextInt(jeux.length);
		
		sendMessage(jeux[num]);
		
	}
	
	private void add(){
		
		sendMessage("no");
		
	}
	
	private void remove(){
		
		
		
	}
	
	private void roll(){
		
		
		
	}
	
}
