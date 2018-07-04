package vendor.utilities.settings;

public class TextNotNullField extends TextField {
	
	public TextNotNullField(String name, String env, String defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		String safeValue = super.sanitizeValue(value)
				
		if(safeValue.length() == 0){
			throw new IllegalArgumentException("Value cannot be empty!");
		}
		
		return safeValue;
	}
	
}
