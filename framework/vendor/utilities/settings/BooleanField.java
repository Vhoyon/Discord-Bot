package vendor.utilities.settings;

public class BooleanField extends SettingField<Boolean> {
	
	public BooleanField(String name, String env, Boolean defaultValue){
		super(name, env, defaultValue);
	}
	
	@Override
	protected Boolean sanitizeValue(Boolean value)
			throws IllegalArgumentException{
		boolean castedValue;
		
		try{
			castedValue = (boolean)value;
		}
		catch(ClassCastException e){
			throw new IllegalArgumentException(
					"Value cannot be something else than \"true\" or \"false\"!");
		}
		
		return castedValue;
	}
	
}
