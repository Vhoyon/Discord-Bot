package vendor.utilities.settings;

import vendor.utilities.sanitizers.TextNotEmptySanitizer;

public class TextNotEmptyField extends TextField {
	
	public TextNotEmptyField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextNotEmptySanitizer.sanitizeValue(value);
	}
	
}
