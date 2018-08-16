package vendor.utilities.sanitizers;

public interface TextSanitizer {
	
	static String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return value.toString();
	}
	
}
