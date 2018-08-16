package vendor.utilities.sanitizers;

import java.util.ArrayList;

public interface EnumSanitizer {
	
	static String sanitizeValue(Object value, ArrayList<String> values)
			throws IllegalArgumentException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(!values.contains(stringValue)){
			throw new IllegalArgumentException("The value " + stringValue
					+ " is not a choice for this setting!");
		}
		
		return stringValue;
		
	}
	
}
