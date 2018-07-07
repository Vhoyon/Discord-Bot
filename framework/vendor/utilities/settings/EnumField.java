package vendor.utilities.settings;

import java.util.ArrayList;

public class EnumField extends TextField {
	
	protected ArrayList<String> values;
	
	public EnumField(String name, String env, Object defaultValue,
			Object... otherValues){
		super(name, env, defaultValue.toString().toLowerCase());
		
		this.values = this.getValuesArrayList(defaultValue, otherValues);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		
		String stringValue = super.sanitizeValue(value).toLowerCase();
		
		if(!this.values.contains(stringValue)){
			throw new IllegalArgumentException("The value " + stringValue
					+ " is not a choice for this setting!");
		}
		
		return stringValue;
		
	}
	
	public ArrayList<String> getPossibleValues(){
		return this.values;
	}
	
	@Override
	protected String formatEnvironment(String envValue){
		String[] possibleValues = envValue.split("\\|");
		
		String envDefaultValue = possibleValues[0];
		
		this.values = this.getValuesArrayList(envDefaultValue, possibleValues);
		
		return envDefaultValue;
	}
	
	private ArrayList<String> getValuesArrayList(Object defaultValue,
			Object... otherValues){
		
		ArrayList<String> newValues = new ArrayList<>();
		
		newValues.add(defaultValue.toString().toLowerCase());
		
		for(Object otherValue : otherValues){
			if(!defaultValue.equals(otherValue)){
				try{
					newValues.add(otherValue.toString().toLowerCase());
				}
				catch(NullPointerException e){}
			}
		}
		
		return newValues;
		
	}
	
}
