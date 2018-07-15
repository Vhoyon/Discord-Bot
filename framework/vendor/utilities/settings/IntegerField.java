package vendor.utilities.settings;

public class IntegerField extends SettingField<Integer> {
	
	private int min;
	private int max;
	
	public IntegerField(String name, String env, int defaultValue){
		this(name, env, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public IntegerField(String name, String env, int defaultValue,
			int minValue, int maxValue){
		super(name, env, defaultValue);
		
		this.min = minValue;
		this.max = maxValue;
	}
	
	@Override
	protected Integer sanitizeValue(Object value)
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
		
		if(castedValue < this.min){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is lower than the minimum required (" + this.min
					+ ")!");
		}
		else if(castedValue > this.max){
			throw new IllegalArgumentException("Value (" + castedValue
					+ ") is higher than the maximum required (" + this.max
					+ ")!");
		}
		
		return castedValue;
		
	}
	
}
