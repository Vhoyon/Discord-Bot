package vendor.utilities.settings;

import vendor.abstracts.Translatable;
import vendor.modules.Environment;
import vendor.objects.Dictionary;

public abstract class AbstractNode extends Translatable {
	
	protected Object value;
	private Object defaultValue;
	
	private String name;
	private String env;
	
	public AbstractNode(String name, String env, Object defaultValue,
			Dictionary dict){
		
		this.name = name;
		this.defaultValue = defaultValue;
		this.env = env;
		
		this.setDictionary(dict);
		
	}
	
	public AbstractNode(String name, String env, Object defaultValue){
		this(name, env, defaultValue, null);
	}
	
	protected abstract Class<?> getType();
	
	public Object getValue() throws RuntimeException{
		if(value != null){
			return value;
		}
		
		Object envFound = null;
		
		envFound = Environment.getVar(env, defaultValue);
		
		if (!envFound.getClass().equals(getType())){
			throw new RuntimeException("Type does not match!");
		}
		
		return envFound;
	}
	
	protected abstract Object sanitizeValue(Object value)
			throws IllegalArgumentException;
	
	public void setValue(Object value) throws IllegalArgumentException{
		this.value = this.sanitizeValue(value);
	}
	
	public <E> E getCastedValue(){
		return (E)getType().cast(getValue());
	}
	
	public String getName(){
		return this.name;
	}
	
}
