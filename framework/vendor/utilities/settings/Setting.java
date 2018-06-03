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
	
	public boolean save(String settingName, Object value, Object context)
			throws IllegalArgumentException{
		
		if(!hasField(settingName)){
			return false;
		}
		
		AbstractField field = getField(settingName);
		
		field.setValue(value, context);
		
		return true;
		
	}
	
	public boolean hasField(String name){
		return fields.containsKey(name);
	}
	
	public AbstractField getField(String name){
		return fields.get(name);
	}
	
}
