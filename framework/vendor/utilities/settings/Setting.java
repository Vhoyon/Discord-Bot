package vendor.utilities.settings;

import java.util.HashMap;

public class Setting {
	
	private HashMap<String, AbstractField> fields;
	
	public Setting(AbstractField... fields){
		
		this.fields = new HashMap<>();
		
		for(AbstractField field : fields){
			this.fields.put(field.getName(), field);
		}
		
	}
	
}
