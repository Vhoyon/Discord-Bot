package ressources;

import languages.Language;

/**
 * 
 * @author Stephano
 *
 *         Contient les constantes du bot
 *
 */
public interface Ressources {
	
	String PREFIX = "!!";
	
	String BUFFER_CONFIRMATION = "confirmation";
	String BUFFER_GAMEPOOL = "GamePool";
	String BUFFER_SPAM = "SPAM_CONDITION";
	String BUFFER_LANG = "LANGUAGE";
	
	Language[] languages =
	{
		new Language("en", "US", "anglais", "eng", "english", "en"),
		new Language("fr", "CA", "francais", "fran�ais", "fra", "french", "fr"),
//		new Language("fr", "Te", "test")
	};
	
}
