package vendor.utilities.sanitizers;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public interface TextRegexSanitizer {
	
	static String sanitizeValue(Object value, String regexToMatch)
			throws IllegalArgumentException, PatternSyntaxException{
		return TextRegexSanitizer.sanitizeValue(value, regexToMatch, false);
	}
	
	static String sanitizeValue(Object value, String regexToMatch,
			boolean isInverted)
			throws IllegalArgumentException, PatternSyntaxException{
		return TextRegexSanitizer.sanitizeValue(value, regexToMatch, isInverted, true);
	}
	
	static String sanitizeValue(Object value, String regexToMatch,
			boolean isInverted, boolean shouldBox)
			throws IllegalArgumentException, PatternSyntaxException{
		return TextRegexSanitizer.sanitizeValue(value, regexToMatch, isInverted, shouldBox, true);
	}
	
	static String sanitizeValue(Object value, String regexToMatch,
			boolean isInverted, boolean shouldBox, boolean shouldCheckPattern)
			throws IllegalArgumentException, PatternSyntaxException{
				
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(regexToMatch != null){
			
			if(shouldCheckPattern){
				// Test if Regex provided is valid
				Pattern.compile(regexToMatch);
			}
			
			if(shouldBox && !regexToMatch.matches("^\\^.*\\$$")){
				regexToMatch = "^" + regexToMatch + "$";
			}
			
			// Test regex and invert if we need to
			if(stringValue.matches(regexToMatch) == isInverted){
				throw new IllegalArgumentException(
						"Value does not match the required pattern!");
			}
			
		}
		
		return stringValue;
		
	}
	
}
