package vendor.utilities.settings;

public class BooleanField extends SettingField<Boolean> {
	
	public BooleanField(String name, String env, Boolean defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Boolean sanitizeValue(Object value)
			throws IllegalArgumentException{
		boolean castedValue;
		
		try{
			
			if (value instanceof String) {
				
				String stringValue = (String)value;
				
				if (!stringValue.matches("^(?i)true|false$")) {
					throw new Exception();
				}
				
				castedValue = Boolean.valueOf(stringValue);
				
			}
			else {
				
				castedValue = (boolean)value;
				
			}
			
		}
		catch(Exception e){
			throw new IllegalArgumentException(
					"Value cannot be something else than \"true\" or \"false\"!");
		}
		
		return castedValue;
	}
	
}
