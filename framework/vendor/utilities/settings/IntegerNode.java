package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class IntegerNode extends AbstractNode {
	
	private int min;
	private int max;
	
	public IntegerNode(String name, String env, int defaultValue,
					   Dictionary dict){
		super(name, env, defaultValue, dict);
	}
	
	@Override
	protected Class<?> getType(){
		return Integer.class;
	}
	
	@Override
	protected Object sanitizeValue(Object value) throws IllegalArgumentException{
		
		int castedValue;
		
		try{
			castedValue = (int)value;
		}
		catch (ClassCastException e){
			throw new IllegalArgumentException("Value is not a number");
		}
		
		return castedValue;
		
	}
	
}
