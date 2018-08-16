package vendor.utilities.sanitizers;

public interface IntegerSanitizer {
	
	static int sanitizeValue(Object value, int minValue, int maxValue)
			throws IllegalArgumentException{
		return IntegerSanitizer.sanitizeValue(object, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}
	
	static int sanitizeValue(Object value, int minValue, int maxValue)
			throws IllegalArgumentException{
		
		int castedValue;
		
		try{
			
			if (value instanceof String) {
				castedValue = Integer.valueOf((String)value);
			}
			else {
				castedValue = (Integer)value;
			}
			
		}
		catch(Exception e){
			throw new IllegalArgumentException("Value is not a number!");
		}
		
		if(castedValue < minValue){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is lower than the minimum required (" + minValue
					+ ")!");
		}
		else if(castedValue > maxValue){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is higher than the maximum permitted (" + maxValue
					+ ")!");
		}
		
		return castedValue;
		
	}
	
}
