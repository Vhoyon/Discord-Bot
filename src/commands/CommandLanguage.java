package commands;

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
			
			boolean canChangeLanguage = true;
			
			String lang = null;
			String country = null;
			
			switch(getContent().toLowerCase()){
			case "francais":
			case "français":
			case "fra":
			case "french":
			case "fr":
				lang = "fr";
				country = "CA";
				break;
			case "anglais":
			case "eng":
			case "english":
			case "en":
				lang = "en";
				country = "US";
				break;
			default:
				canChangeLanguage = false;
				break;
			}
			
			if(!canChangeLanguage){
				
				new BotError(this, getStringEz("NoTranslation"), false);
				
			}
			else{
				
				String langChangeMessage = getStringEz("ChangingLanguage");
				
				Dictionary changedDictionary = getDictionary();
				
				changedDictionary.setLanguage(lang, country);
				getBuffer().push(changedDictionary, BUFFER_LANG);
				
				sendInfoMessage(langChangeMessage,
						changedDictionary.getString("LanguageName"));
				
			}
			
		}
		
	}
}
