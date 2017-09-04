package framework;

import java.util.Locale;
import java.util.ResourceBundle;

public class Dictionary {
	
	private ResourceBundle resources;
	private Locale locale;
	
	public Dictionary(){
		this.resources = getDefaultLanguageResources();
	}
	
	public void setLanguage(String lang, String country){
		this.resources = getLanguageResources(lang, country);
	}
	
	public String getString(String key){
		
		String string;
		
		string = resources.getString(key);
		
		return string;
		
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		
		locale = new Locale(lang, country);
		
		return ResourceBundle.getBundle("languages.DiscordBot", locale);
		
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources("en", "US");
	}
	
}
