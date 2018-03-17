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
	
	public String getString(String key, Object... replacements){
		return format(this.getString(key), replacements);
	}
	
	public String getString(String key){
		
		if(key == null){
			throw new IllegalArgumentException(
					"The \"key\" parameter cannot be null!");
		}
		
		String string;
		
		try{
			try{
				
				string = resources.getString(key);
				
				if(string.length() == 0)
					throw new NullPointerException();
				
			}
			catch(MissingResourceException e){
				string = getDefaultLanguageResources().getString(key);
				
				if(isDebugging())
					Logger.log("Key \""
							+ key
							+ "\" is missing in the resource file for the language \""
							+ locale.getLanguage() + "_" + locale.getCountry()
							+ "\".");
			}
			catch(NullPointerException e){
				
				string = getDefaultLanguageResources().getString(key);
				
				if(isDebugging())
					Logger.log("Key \""
							+ key
							+ "\" is empty in the resource file for the language \""
							+ locale.getLanguage() + "_" + locale.getCountry()
							+ "\".");
				
			}
		}
		catch(MissingResourceException e){
			
			string = null;
			
			Logger.log("Key \"" + key
					+ "\" is not in the default resource file - what's up with that?");
			
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
