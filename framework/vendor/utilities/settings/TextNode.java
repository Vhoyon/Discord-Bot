package vendor.utilities.settings;

import vendor.objects.Dictionary;

public class TextNode extends AbstractNode {
	
	public TextNode(String name, String env, String defaultValue,
			Dictionary dict){
		super(name, env, defaultValue, dict);
	}
	
	@Override
	protected Class<?> getType(){
		return String.class;
	}
	
	@Override
	protected Object sanitizeValue(Object value) throws IllegalArgumentException{
		return value.toString();
	}
	
}
