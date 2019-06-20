package io.github.vhoyon.bot.commands;

import java.util.Arrays;
import java.util.List;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.objects.Dictionary;
import io.github.vhoyon.vramework.objects.Language;

/**
 * Command that changes the languages of the bot (strings that uses the
 * {@link #lang(String)} method and its variants) for the TextChannel that this
 * command was called from.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandLanguage extends BotCommand {
	
	@Override
	public void actions(){
		
		if(!hasContent()){
			sendMessage(lang("NullContent", buildVCommand(LANGUAGE + " ["
					+ lang("NullContentUsage") + "]"), buildVCommand(LANG
					+ " [" + lang("NullContentUsage") + "]")));
		}
		else{
			
			Language lang = getLanguage(getContent());
			
			if(lang == null){
				
				new BotError(this, lang("NoTranslation"), false);
				
			}
			else{
				
				String langChangeMessage = lang("ChangingLanguage");
				
				Dictionary changedDictionary = getDictionary();
				
				changedDictionary
						.setLanguage(lang.getLang(), lang.getCountry());
				
				remember(changedDictionary, BUFFER_LANG);
				
				sendInfoMessage(format(langChangeMessage,
						code(changedDictionary.getDirectString("LanguageName"))));
				
			}
			
		}
		
	}
	
	private Language getLanguage(String langFriendlyName){
		
		for(Language lang : languages){
			
			if(lang.equals(langFriendlyName))
				return lang;
			
		}
		
		return null;
		
	}
	
	@Override
	public String getCall(){
		return LANG;
	}
	
	@Override
	public List<String> getAliases(){
		return Arrays.asList(LANGUAGE);
	}
	
	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
	
}