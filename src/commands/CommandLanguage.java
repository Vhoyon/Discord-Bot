package commands;

import utilities.Command;
import utilities.Language;
import vendor.objects.Dictionary;
import errorHandling.BotError;

public class CommandLanguage extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
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
				
				sendInfoMessage(String.format(langChangeMessage,
						changedDictionary.getDirectString("LanguageName")));
				
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