package framework;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Dictionary {
	
	private ResourceBundle resources;
	
	public Dictionary(){
		this.resources = getDefaultLanguageResources();
	}
	
	public void setLanguage(String lang, String country){
		this.resources = getLanguageResources(lang, country);
	}
	
	public String getString(String key){
		
		String string;
		
		try{
			string = resources.getString(key);
		}
		catch(MissingResourceException e){
			string = getDefaultLanguageResources().getString(key);
			System.out.println("lol");
		}
		
		return string;
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		
		Locale locale = new Locale(lang, country);
		
		return ResourceBundle.getBundle("languages.DiscordBot", locale);
		
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return ResourceBundle.getBundle("languages.DiscordBot");
	}
	
}
