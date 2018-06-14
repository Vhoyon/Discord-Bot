package vendor.utilities.settings;

import vendor.objects.Dictionary;

import java.util.HashMap;
import java.util.function.Consumer;

public class Setting {
	
	private HashMap<String, SettingField<Object>> fields;
	
	public Setting(SettingField<Object>... fields){
		this(new Dictionary(), fields);
	}
	
	public Setting(Dictionary dict, SettingField<Object>... fields){
		
		this.fields = new HashMap<>();
		
		for(SettingField<Object> field : fields){
			field.setDictionary(dict);
			
			this.fields.put(field.getName(), field);
		}
		
	}
	
	public boolean save(String settingName, Object value, Consumer<Object> onChange)
			throws IllegalArgumentException{
		
		if(!hasField(settingName)){
			return false;
		}
		
		SettingField<Object> field = getField(settingName);
		
		field.setValue(value, onChange);
		
		return true;
		
	}
	
	public boolean hasField(String name){
		return fields.containsKey(name);
	}
	
	public SettingField<Object> getField(String name){
		return fields.get(name);
	}
	
}
