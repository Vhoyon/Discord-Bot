package vendor.utilities.settings;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

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
		
		try{
			// Test if Regex provided is valid
			Pattern.compile(regexToMatch);
			
			if(!shouldBox || regexToMatch.matches("^\\^.*\\$$")){
				this.regexToMatch = regexToMatch;
			}
			else{
				this.regexToMatch = "^" + regexToMatch + "$";
			}
		}
		catch(PatternSyntaxException e){
			
			Logger.log(e);
			Logger.log("\nFix your regex quickly! In the meanwhile, any string will be accepted for the setting \"" + getName() + "\"...", LogType.WARNING);
			
			this.regexToMatch = null;
			
		}
		
		this.isInverted = isInverted;
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		String stringValue = super.sanitizeValue(value);
		
		if(this.regexToMatch != null && stringValue.matches(this.regexToMatch) == this.isInverted){
			throw new IllegalArgumentException(
					"Value does not match the required pattern!");
		}
		
		return stringValue;
	}
	
}
