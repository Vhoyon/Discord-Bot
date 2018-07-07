package vendor.utilities.settings;

public class CharField extends SettingField<Character> {
	
	public CharField(String name, String env, char defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Character sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = value.toString();
		
		if(stringValue.length() != 1){
			throw new IllegalArgumentException(
					"Only one character is expected!");
		}
		
		return stringValue.charAt(0);
	}
	
}
