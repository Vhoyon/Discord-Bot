package vendor.utilities.settings;

import vendor.abstracts.Translatable;
import vendor.exceptions.BadFormatException;
import vendor.modules.Environment;

import java.util.function.Consumer;

public abstract class SettingField<E> extends Translatable {
	
	protected E value;
	private E defaultValue;
	
	private String name;
	private String env;
	
	public SettingField(String name, String env, E defaultValue){
		
		this.name = name;
		this.defaultValue = defaultValue;
		this.env = env;
		
	}
	
	public E getValue() throws BadFormatException{
		if(value != null){
			return value;
		}
		
		try{
			E envValue = (E)Environment.getVar(envKey);
			
			if(envValue == null){
				value = defaultValue;
			}
			else{
				value = this.formatEnvironment(envValue);
			}
		}
		catch(ClassCastException e){
			throw new BadFormatException(
					"Environment variable is not formatted correctly for its data type!");
		}
			
		return value;
	}
	
	public final void setValue(E value) throws IllegalArgumentException{
		this.setValue(value, null);
	}
	
	public final void setValue(E value, Consumer<E> onChange)
			throws IllegalArgumentException{
		
		if(value == null){
			throw new IllegalArgumentException("Value cannot be null!");
		}
		
		this.value = this.sanitizeValue(value);
		
		if(onChange != null)
			onChange.accept(this.value);
		
	}
	
	public String getName(){
		return this.name;
	}
	
	protected abstract E sanitizeValue(Object value) throws IllegalArgumentException;
	
	protected E formatEnvironment(E envValue){
		return envValue;
	}
	
}
