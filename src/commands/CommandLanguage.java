package commands;

import utilities.BotCommand;
import vendor.objects.Dictionary;
import vendor.objects.Language;
import errorHandling.BotError;

public class CommandLanguage extends BotCommand {
	
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