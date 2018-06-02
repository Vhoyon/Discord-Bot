package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class IntegerField extends AbstractField<Integer> {
	
	private int min;
	private int max;
	
	public IntegerField(String name, String env, int defaultValue,
			Dictionary dict){
		this(name, env, defaultValue, dict, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}
	
	public IntegerField(String name, String env, int defaultValue,
			Dictionary dict, int minValue, int maxValue){
		super(name, env, defaultValue, dict);
		
		this.min = minValue;
		this.max = maxValue;
	}
	
	@Override
	protected Integer sanitizeValue(Integer value)
			throws IllegalArgumentException{
		
		int castedValue;
		
		try{
			castedValue = (Integer)value;
		}
		catch(ClassCastException e){
			throw new IllegalArgumentException("Value is not a number!");
		}
		
		if(value < this.min){
			throw new IllegalArgumentException("Value (" + value
					+ ") is lower than the minimum required (" + this.min
					+ ")!");
		}
		else if(value > this.max){
			throw new IllegalArgumentException("Value (" + value
					+ ") is higher than the maximum required (" + this.min
					+ ")!");
		}
		
		return castedValue;
		
	}
	
}
