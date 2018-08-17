package vendor.utilities.sanitizers;

public interface TextNotEmptySanitizer {
	
	static String sanitizeValue(Object value) throws IllegalArgumentException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		if(stringValue.length() == 0){
			throw new IllegalArgumentException("Value cannot be empty!");
		}
		
		return stringValue;
		
	}
	
}
