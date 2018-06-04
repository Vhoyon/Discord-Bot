package vendor.utilities.settings;

import vendor.abstracts.Translatable;
import vendor.exceptions.BadFormatException;
import vendor.modules.Environment;
import vendor.objects.Dictionary;

public abstract class SettingField<E> extends Translatable {
	
	protected E value;
	private E defaultValue;
	
	private String name;
	private String env;
	
	public SettingField(String name, String env, E defaultValue, Dictionary dict){
		
		this.name = name;
		this.defaultValue = defaultValue;
		this.env = env;
		
		this.setDictionary(dict);
		
	}
	
	public SettingField(String name, String env, E defaultValue){
		this(name, env, defaultValue, new Dictionary());
	}
	
	public E getValue() throws BadFormatException{
		if(value != null){
			return value;
		}
		
		E envFound = null;
		
		try{
			
			envFound = (E)Environment.getVar(env, defaultValue);
			
		}
		catch(ClassCastException e){
			throw new BadFormatException(
					"Environment variable is not formatted correctly for its data type!");
		}
		
		return envFound;
	}
	
	public void setValue(E value, Object context)
			throws IllegalArgumentException{
		this.value = this.sanitizeValue(value);
		
		onChange(this.value, context);
	}
	
	public String getName(){
		return this.name;
	}
	
	protected abstract E sanitizeValue(E value) throws IllegalArgumentException;
	
	public void onChange(Object value, Object context){}
	
}
