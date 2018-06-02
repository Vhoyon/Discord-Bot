package vendor.utilities.settings;

import java.util.HashMap;

public class Setting {
	
	private HashMap<String, AbstractField> nodes;
	
	public Setting(AbstractField... nodes){
		
		this.nodes = new HashMap<>();
		
		for (AbstractField node : nodes){
			this.nodes.put(node.getName(), node);
		}
		
	}
	
}
