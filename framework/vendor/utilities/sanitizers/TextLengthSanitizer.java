package vendor.utilities.sanitizers;

public interface TextLengthSanitizer {
	
	// default String sanitizeValue(Object value, int minLength, int maxLength)
	// 		throws IllegalArgumentException{
				
	// }
	
	static String sanitizeValue(Object value, int minLength, int maxLength)
			throws IllegalArgumentException{
				
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		int stringLength = stringValue.length();
		
		if(stringLength < minLength){
			throw new IllegalArgumentException(
					"This setting's value needs to have at least "
							+ minLength + " characters!");
		}
		else if(stringLength > maxLength){
			throw new IllegalArgumentException(
					"This setting's value cannot have more than "
							+ maxLength + " characters!");
		}
		
		return stringValue;
		
	}
	
}
