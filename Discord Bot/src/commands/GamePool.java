package commands;

import java.util.ArrayList;

public class GamePool {

	private ArrayList<String> jeux = new ArrayList<>();
	
	public GamePool() {
		// TODO Auto-generated constructor stub
	}
	
	public GamePool(String[] jeux) {
		for (int i = 0; i < jeux.length; i++) {
			add(jeux[i]);
		}
	}

	private void add(String nom) {
		jeux.add(nom);
	}

}
