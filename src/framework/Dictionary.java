package framework;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Dictionary {
	
	private String defaultLang = "en";
	private String defaultCountry = "US";
	
	private ResourceBundle resources;
	private Locale locale;
	
	public Dictionary(){
		
		locale = new Locale(defaultLang, defaultCountry);
		this.resources = getDefaultLanguageResources();
		
	}
	
	public void setLanguage(String lang, String country){
		
		if("true".equals(System.getProperty("isDebugMode")))
			ResourceBundle.clearCache();
		
		locale = new Locale(lang, country);
		this.resources = getLanguageResources(locale);
		
	}
	
	public String getString(String key){
		
		String string;
		
		try{
			string = resources.getString(key);
		}
		catch(MissingResourceException e){
			string = getDefaultLanguageResources().getString(key);
			System.out.println("Key \"" + key
					+ "\" is missing in the resource file for the language \""
					+ locale.getLanguage() + "_" + locale.getCountry() + "\".");
		}
		
		return string;
		
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		return getLanguageResources(new Locale(lang, country));
	}
	
	private ResourceBundle getLanguageResources(Locale locale){
		return ResourceBundle.getBundle("languages.DiscordBot", locale);
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources(defaultLang, defaultCountry);
		
	}
	
}
