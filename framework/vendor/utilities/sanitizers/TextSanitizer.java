package vendor.utilities.sanitizers;

public interface TextSanitizer {
	
	static String sanitizeValue(Object value)
			throws IllegalArgumentException{
		if(value == null)
				return "";
		
		return value.toString();
	}
	
}
