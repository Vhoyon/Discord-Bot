package io.github.vhoyon.bot.utilities.interfaces;

import io.github.vhoyon.vramework.objects.Language;
import io.github.vhoyon.vramework.util.settings.*;

/**
 * Interface that hold the Resources for our bot, such as the prefixes, the
 * buffer storage names, the languages definitions and the setting definitions.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano
 */
public interface Resources {
	
	String PREFIX = "\\";
	char OPTION_PREFIX = '-';
	
	String BUFFER_GAMEPOOL = "GAMEPOOL";
	String BUFFER_SPAM = "SPAM_CONDITION";
	String BUFFER_LANG = "LANGUAGE";
	String BUFFER_SETTINGS = "SETTINGS";
	
	Language[] languages =
	{
		new Language("en", "US", "anglais", "eng", "english", "en"),
		new Language("fr", "CA", "francais", "français", "fra", "french", "fr"),
	};
	
	Setting[] SETTINGS =
	{
		new TextNotEmptyField("prefix", "PREFIX", PREFIX),
		new CharField("option_prefix", "OPTION_PREFIX", OPTION_PREFIX),
		new TextNotEmptyField("nickname", "NICKNAME", "Vhoyon"),
		new BooleanField("confirm_stop", "SHOULD_CONFIRM_STOP", true),
		new IntegerField("volume", "DEFAULT_VOLUME", 60, 0, 100),
		new IntegerField("empty_drop_delay", "MUSIC_EMPTY_DISCONNECT_DELAY",
				30000, 0, Integer.MAX_VALUE),
		new IntegerField("alone_drop_delay", "MUSIC_ALONE_DISCONNECT_DELAY",
				15000, 0, Integer.MAX_VALUE),
	};
	
}
