package commands;

import java.util.ArrayList;

public class GamePool {
	
	private ArrayList<String> jeux = new ArrayList<>();
	
	public GamePool(){}
	
	public GamePool(String[] jeux){
		for(int i = 0; i < jeux.length; i++){
			this.jeux.add(jeux[i]);
		}
	}
	
	private void add(String nom){
		jeux.add(nom);
	}
	
	public ArrayList<String> getJeux(){
		return jeux;
	}
	
}
