package vendor.utilities.sanitizers;

public interface CharSanitizer {
	
	static char sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(stringValue.length() != 1){
			throw new IllegalArgumentException(
					"Only one character is expected!");
		}
		
		return stringValue.charAt(0);
	}
	
}
