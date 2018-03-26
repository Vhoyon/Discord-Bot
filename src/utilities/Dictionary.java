package utilities;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import utilities.interfaces.Utils;
import vendor.modules.Logger;

public class Dictionary implements Utils {
	
	private String defaultLang = "en";
	private String defaultCountry = "US";
	
	private ResourceBundle resources;
	private Locale locale;
	
	public Dictionary(){
		
		locale = new Locale(defaultLang, defaultCountry);
		this.resources = getDefaultLanguageResources();
		
	}
	
	public void setLanguage(String lang, String country){
		
		if(isDebugging())
			ResourceBundle.clearCache();
		
		locale = new Locale(lang, country);
		this.resources = getLanguageResources(locale);
		
	}
	
	public String getDirectString(String key, Object... replacements){
		return getString(key, null, replacements);
	}
	
	public String getDirectString(String key){
		return getString(key, null);
	}
	
	public String getString(String key, String possiblePrefix,
			Object... replacements){
		return format(this.getString(key, possiblePrefix), replacements);
	}
	
	public String getString(String key, String possiblePrefix){
		
		if(key == null){
			throw new IllegalArgumentException(
					"The \"key\" parameter cannot be null!");
		}
		
		String string = null;
		
		try{
			try{
				
				string = tryGetString(resources, key, possiblePrefix);
				
				if(string == null || string.length() == 0)
					throw new NullPointerException();
				
			}
			catch(MissingResourceException e){
				
				string = tryGetString(getDefaultLanguageResources(), key,
						possiblePrefix);
				
				if(isDebugging())
					Logger.log("Key \""
							+ key
							+ "\" is missing in the resource file for the language \""
							+ locale.getLanguage() + "_" + locale.getCountry()
							+ "\".");
				
			}
			catch(NullPointerException e){
				
				string = tryGetString(getDefaultLanguageResources(), key,
						possiblePrefix);
				
				if(isDebugging())
					Logger.log("Key \""
							+ key
							+ "\" is empty in the resource file for the language \""
							+ locale.getLanguage() + "_" + locale.getCountry()
							+ "\".");
				
			}
		}
		catch(MissingResourceException e){
			
			Logger.log("Key \""
					+ key
					+ "\" is not in any resource file - what's up with that?");
			
		}
		
		return string;
		
	}
	
	private String tryGetString(ResourceBundle resources, String key,
			String possiblePrefix) throws MissingResourceException{
		
		String string = null;
		
		try{
			string = resources.getString(key);
		}
		catch(MissingResourceException e){
			
			if(possiblePrefix == null || possiblePrefix.length() == 0)
				throw e;
			
			string = resources.getString(possiblePrefix + key);
			
		}
		
		return string;
		
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		return getLanguageResources(new Locale(lang, country));
	}
	
	private ResourceBundle getLanguageResources(Locale locale){
		return ResourceBundle.getBundle("lang.DiscordBot", locale);
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources(defaultLang, defaultCountry);
	}
	
}
