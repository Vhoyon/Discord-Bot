package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class BooleanField extends AbstractField<Boolean> {
	
	public BooleanField(String name, String env, Boolean defaultValue,
			Dictionary dict){
		super(name, env, defaultValue, dict);
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
