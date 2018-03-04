package commands;

import languages.Language;
import errorHandling.BotError;
import framework.Command;
import framework.Dictionary;

public class CommandLanguage extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			sendMessage(getStringEz("NullContent"), buildVCommand(LANGUAGE
					+ " [" + getStringEz("NullContentUsage") + "]"),
					buildVCommand(LANG + " [" + getStringEz("NullContentUsage")
							+ "]"));
		}
		else{
			
			Language lang = getLanguage(getContent());
			
			if(lang == null){
				
				new BotError(this, getStringEz("NoTranslation"), false);
				
			}
			else{
				
				String langChangeMessage = getStringEz("ChangingLanguage");
				
				Dictionary changedDictionary = getDictionary();
				
				changedDictionary
						.setLanguage(lang.getLang(), lang.getCountry());
				
				remember(changedDictionary, BUFFER_LANG);
				
				sendInfoMessage(langChangeMessage,
						changedDictionary.getString("LanguageName"));
				
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
	
}