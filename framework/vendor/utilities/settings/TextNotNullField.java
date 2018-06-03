package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class TextNotNullField extends TextField {
	
	public TextNotNullField(String name, String env, String defaultValue,
			Dictionary dict){
		super(name, env, defaultValue, dict);
	}
	
	@Override
	protected String sanitizeValue(String value)
			throws IllegalArgumentException{
		if(value == null || value.length() == 0){
			throw new IllegalArgumentException("Value cannot be empty!");
		}
		
		return super.sanitizeValue(value);
	}
	
}
