package vendor.utilities.settings;

public class TextLengthField extends TextNotEmptyField {
	
	private int minLength;
	private int maxLength;
	
	public TextLengthField(String name, String env, String defaultValue, int minLength, int maxLength){
		super(name, env, defaultValue);
		
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = super.sanitizeValue(value);
		
		int stringLength = stringValue.length();
				
		if(stringLength < this.minLength){
			throw new IllegalArgumentException("This setting's value needs to have at least " + this.minLength + " characters!");
		}
		else if(stringLength > this.maxLength){
			throw new IllegalArgumentException("This setting's value cannot have more than " + this.maxLength + " characters!");
		}
		
		return stringValue;
	}
	
}
