package vendor.utilities.settings;

public class TextNotNullField extends TextField {
	
	public TextNotNullField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
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
