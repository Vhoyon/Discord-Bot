package vendor.objects;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

public class Dictionary implements Utils {
	
	private static final String DEFAULT_LANG = "en";
	private static final String DEFAULT_COUNTRY = "US";
	
	private static final String DEFAULT_DIRECTORY = "lang";
	
	private ResourceBundle resources;
	private Locale locale;
	
	private String directory;
	
	public Dictionary(){
		this.locale = new Locale(DEFAULT_LANG, DEFAULT_COUNTRY);
		this.directory = DEFAULT_DIRECTORY;
		this.resources = getDefaultLanguageResources();
	}
	
	private String getDirectory(){
		return this.directory;
	}
	
	private void setDirectory(String directory){
		if(!getDirectory().equals(directory))
			this.directory = directory;
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
		
		key = handleKey(key);
		
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
					Logger.log(
							"Key \""
									+ key
									+ "\" is missing in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
			catch(NullPointerException e){
				
				string = tryGetString(getDefaultLanguageResources(), key,
						possiblePrefix);
				
				if(isDebugging())
					Logger.log(
							"Key \""
									+ key
									+ "\" is empty in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
		}
		catch(MissingResourceException e){
			
			Logger.log("Key \"" + key
					+ "\" is not in any resource file - what's up with that?",
					LogType.WARNING);
			
		}
		
		return string;
		
	}
	
	private String handleKey(String key){
		
		if(!key.contains(".")){
			
			setDirectory(DEFAULT_DIRECTORY);
			
			return key;
			
		}
		else{
			
			StringBuilder builder = new StringBuilder();
			
			String[] structure = key.split("\\.");
			
			for(int i = 0; i < structure.length - 1; i++){
				
				builder.append(structure[i]);
				
				if(i < structure.length - 1 - 1){
					builder.append(".");
				}
				
			}
			
			setDirectory(builder.toString());
			
			return structure[structure.length - 1];
			
		}
		
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
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources(DEFAULT_LANG, DEFAULT_COUNTRY);
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		return getLanguageResources(new Locale(lang, country));
	}
	
	private ResourceBundle getLanguageResources(Locale locale){
		return ResourceBundle.getBundle(getDirectory() + ".strings", locale);
	}
	
}
