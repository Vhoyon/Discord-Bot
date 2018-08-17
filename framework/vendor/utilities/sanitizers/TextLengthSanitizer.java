package vendor.utilities.sanitizers;

public interface TextLengthSanitizer {
	
	static String sanitizeValueMin(Object value, int minLength){
		return TextLengthSanitizer.sanitizeValue(value, minLength,
				Integer.MAX_VALUE);
	}
	
	static String sanitizeValueMax(Object value, int maxLength){
		return TextLengthSanitizer.sanitizeValue(value, Integer.MIN_VALUE,
				maxLength);
	}
	
	static String sanitizeValue(Object value, int minLength, int maxLength)
			throws IllegalArgumentException{
		
		String stringValue = TextSanitizer.sanitizeValue(value);
		
		int stringLength = stringValue.length();
		
		if(minLength != Integer.MIN_VALUE && stringLength < minLength){
			throw new IllegalArgumentException(
					"This setting's value needs to have at least " + minLength
							+ " characters!");
		}
		else if(maxLength != Integer.MAX_VALUE && stringLength > maxLength){
			throw new IllegalArgumentException(
					"This setting's value cannot have more than " + maxLength
							+ " characters!");
		}
		
		return stringValue;
		
	}
	
}
