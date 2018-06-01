package vendor.utilities.settings;

import java.util.HashMap;

public class Setting {
	
	private HashMap<String, AbstractNode> nodes;
	
	public Setting(AbstractNode... nodes){
		
		this.nodes = new HashMap<>();
		
		for (AbstractNode node : nodes){
			this.nodes.put(node.getName(), node);
		}
		
	}
	
}
