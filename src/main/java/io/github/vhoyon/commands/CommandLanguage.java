package io.github.vhoyon.commands;

import io.github.vhoyon.utilities.BotCommand;
import io.github.vhoyon.errorHandling.BotError;
import vendor.objects.Dictionary;
import vendor.objects.Language;

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
	public void action(){
		
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
	public Object getCalls(){
		return new String[]
		{
			LANG, LANGUAGE
		};
	}
	
	@Override
	public String getCommandDescription(){
		return "Change the language of the bot";
	}
	
}