package vendor.utilities.sanitizers;

import java.util.ArrayList;

import vendor.exceptions.BadFormatException;

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
	
}
