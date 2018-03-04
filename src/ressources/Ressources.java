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
	
	String BOT_NAME = "Bot";
	
	String TOKEN_DISCORD = "MzIxMTQ1NDI0NTQ5MzgwMDk3.DBZxmA.CyEXFP8cQEkV5Qn8X-PKLPY4EhM";
	String TOKEN_YOUTUBE = "AIzaSyDfPTuiyimq09IK5umHfD4OY8OV5oya2mU";
	
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
