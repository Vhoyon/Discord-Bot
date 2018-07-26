package vendor.utilities.settings;

public class TextRegexField extends TextField {
	
	private String regexToMatch;
	private boolean isInverted;
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch){
		this(name, env, defaultValue, regexToMatch, false);
	}
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch, boolean isInverted){
		this(name, env, defaultValue, regexToMatch, isInverted, true);
	}
	
	public TextRegexField(String name, String env, String defaultValue,
			String regexToMatch, boolean isInverted, boolean shouldBox){
		super(name, env, defaultValue);
		
		if(!shouldBox || regexToMatch.matches("^\\^.*\\$$")){
			this.regexToMatch = regexToMatch;
		}
		else{
			this.regexToMatch = "^" + regexToMatch + "$";
		}
		
		this.isInverted = isInverted;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = super.sanitizeValue(value);
		
		if(stringValue.matches(this.regexToMatch) == this.isInverted){
			throw new IllegalArgumentException(
					"Value does not match the required pattern!");
		}
		
		return stringValue;
	}
	
}
