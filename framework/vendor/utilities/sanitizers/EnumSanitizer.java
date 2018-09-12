package vendor.utilities.sanitizers;

import vendor.exceptions.BadFormatException;
import vendor.modules.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

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
		return formatEnvironmentValue(Environment.getVar(envKey));
	}
	
	static ArrayList<String> formatEnvironmentValue(String envValue)
			throws BadFormatException{
		
		// Verify that environment is of format "[...]| [...] | [...]" while allowing single choice enums
		// Resetting envValue here is not necessary, but this will make it future-proof
		envValue = TextRegexSanitizer
				.sanitizeValue(
						envValue,
						"[^\\n|]*[^\\r\\n\\t\\f\\v |][^\\n|]*(\\|[^\\n|]*[^\\r\\n\\t\\f\\v |][^\\n|]*[^\\n \\t|]*)*");
		
		String[] possibleValues = envValue.trim().split("\\s*\\|\\s*");
		
		ArrayList<String> values = new ArrayList<>(
				Arrays.asList(possibleValues));
		
		// Remove duplicate while keeping the order of the values
		LinkedHashSet<String> hs = new LinkedHashSet<>();
		hs.addAll(values);
		values.clear();
		values.addAll(hs);
		
		return values;
		
	}
	
}
