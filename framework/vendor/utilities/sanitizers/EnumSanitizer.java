package vendor.utilities.sanitizers;

import vendor.exceptions.BadFormatException;
import vendor.modules.Environment;

import java.util.ArrayList;
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
	
	static ArrayList<String> formatEnvironment(String envKey)
			throws BadFormatException{
		return extractEnumFromString(Environment.getVar(envKey));
	}
	
	static ArrayList<String> extractEnumFromString(String stringValue)
			throws BadFormatException{
		return extractEnumFromString(stringValue, '|');
	}
	
	static ArrayList<String> extractEnumFromString(String stringValue,
			char separator) throws BadFormatException{
		
		String pSep = String.format("\\%s", separator);
		
		// Verify that environment is of format "[...]| [...] | [...]" while allowing single choice enums.
		// Please see https://regex101.com/r/FrVwfk for an interactive testing session for this regex.
		// ~ Resetting stringValue here is not necessary, but this will make it future-proof ~
		stringValue = TextRegexSanitizer.sanitizeValue(stringValue, "[^\\n"
				+ pSep + "]*[^\\r\\n\\t\\f\\v " + pSep + "][^\\n" + pSep
				+ "]*(" + pSep + "[^\\n" + pSep + "]*[^\\r\\n\\t\\f\\v " + pSep
				+ "][^\\n" + pSep + "]*[^\\n \\t" + pSep + "]*)*");
		
		String[] possibleValues = stringValue.trim().split(
				"\\s*(?<!\\\\)" + pSep + "\\s*");
		
		ArrayList<String> values = new ArrayList<>();
		
		for(String possibleValue : possibleValues){
			values.add(possibleValue.replaceAll("\\\\" + pSep + "", "|"));
		}
		
		// Remove duplicate while keeping the order of the values
		LinkedHashSet<String> hs = new LinkedHashSet<>();
		hs.addAll(values);
		values.clear();
		values.addAll(hs);
		
		return values;
		
	}
	
}
