package vendor.utilities.settings;

public class TextNotEmptyField extends TextField {
	
	public TextNotEmptyField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = super.sanitizeValue(value);
		
		if(stringValue.length() == 0){
			throw new IllegalArgumentException("Value cannot be empty!");
		}
		
		return stringValue;
	}
	
}
