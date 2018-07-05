package vendor.utilities.settings;

public class CharField extends TextNotEmptyField {
	
	public CharField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected char sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = super.sanitizeValue(value);
		
		if(stringValue.length() != 1){
			throw new IllegalArgumentException("Only one character is expected!");
		}
		
		return stringValue.charAt(0);
	}
	
}
