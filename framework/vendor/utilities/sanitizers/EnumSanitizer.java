package vendor.utilities.sanitizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import vendor.exceptions.BadFormatException;
import vendor.modules.Environment;

public interface EnumSanitizer {
	
	static String sanitizeValue(Object value, ArrayList<String> values)
			throws BadFormatException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(!values.contains(stringValue)){
			throw new BadFormatException("The value " + stringValue
					+ " is not a choice for this setting!", 1);
		}
		
		return stringValue;
		
	}
	
	static ArrayList<String> formatEnvironment(String envKey){
		return formatEnvironmentValue(Environment.getVar(envKey, null));
	}
	
	static ArrayList<String> formatEnvironmentValue(String envValue){
		
		if(envValue == null)
			return null;
		
		String[] possibleValues = envValue.trim().split("\\s*\\|\\s*");
		
		ArrayList<String> values = new ArrayList<>(Arrays.asList(possibleValues));
		
		// Remove duplicate while keeping the order of the values
		LinkedHashSet<String> hs = new LinkedHashSet<>();
		hs.addAll(values);
		values.clear();
		values.addAll(hs);
		
		return values;
		
	}
	
}
