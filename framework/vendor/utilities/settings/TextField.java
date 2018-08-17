package vendor.utilities.settings;

import vendor.utilities.sanitizers.TextSanitizer;

public class TextField extends SettingField<String> {
	
	public TextField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return TextSanitizer.sanitizeValue(value);
	}
	
}
