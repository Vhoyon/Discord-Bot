package utilities.interfaces;

import vendor.objects.Language;
import vendor.utilities.settings.SettingField;
import vendor.utilities.settings.TextNotNullField;

/**
 * Contient les constantes du bot
 * 
 * @author Stephano
 */
public interface Resources {
	
	String PREFIX = "!!";
	String PARAMETER_PREFIX = "--";
	
	String BUFFER_GAMEPOOL = "GAMEPOOL";
	String BUFFER_SPAM = "SPAM_CONDITION";
	String BUFFER_LANG = "LANGUAGE";
	String BUFFER_SETTINGS = "SETTINGS";
	
	Language[] languages =
	{
		new Language("en", "US", "anglais", "eng", "english", "en"),
		new Language("fr", "CA", "francais", "fran�ais", "fra", "french", "fr"),
	//		new Language("fr", "Te", "test")
	};
	
	SettingField[] SETTINGS =
	{
		new TextNotNullField("prefix", "PREFIX", PREFIX)
	};
	
}
