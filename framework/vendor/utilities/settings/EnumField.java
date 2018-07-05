package vendor.utilities.settings;

import java.util.ArrayList;
import java.util.Arrays;

public class EnumField extends TextField {
	
	private boolean isSorted;
	private ArrayList<String> values;
	
	public EnumField(String name, String env, boolean isSorted,
		Object defaultValue, Object... otherValues){
		super(name, env, defaultValue.toString().toLowerCase());
		
		this.isSorted = isSorted;
		
		this.values = this.getValuesArrayList(defaultValue, possibleValues);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		
		String stringValue = super.sanitizeValue(value).toLowerCase();
		
		if(!this.values.contains(stringValue)){
			throw new IllegalArgumentException("The value " + stringValue + " is not a choice for this setting!");
		}
		
		return stringValue;
		
	}
	
	@Override
	protected String formatEnvironment(String envValue){
		String[] possibleValues = envValue.split("\\|");
		
		String envDefaultValue = possibleValues[0]
		
		this.values = this.getValuesArrayList(envDefaultValue, possibleValues);
		
		return envDefaultValue;
	}
	
	private ArrayList<String> getValuesArrayList(Object defaultValue, Object... otherValues){
		
		ArrayList<String> newValues = new ArrayList<>();
		
		newValues.add(defaultValue.toString().toLowerCase());
		
		for(Object otherValue : otherValues){
			if(!defaultValue.equals(otherValue)){
				try{
					newValues.add(othervalue.toString().toLowerCase());
				}
				catch(NullPointerException e){}
			}
		}
		
		return newValues;
		
	}
	
}
