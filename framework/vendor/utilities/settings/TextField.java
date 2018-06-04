package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class TextField extends SettingField<String> {
	
	public TextField(String name, String env, String defaultValue,
			Dictionary dict){
		super(name, env, defaultValue, dict);
	}
	
	@Override
	protected String sanitizeValue(String value)
			throws IllegalArgumentException{
		return value;
	}
	
}
