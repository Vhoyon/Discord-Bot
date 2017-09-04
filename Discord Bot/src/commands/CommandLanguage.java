package commands;

import errorHandling.BotError;
import framework.Command;
import framework.Dictionary;

public class CommandLanguage extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			sendMessage(getEzString("NullContent"), buildVCommand(LANGUAGE
					+ " [" + getEzString("NullContentUsage") + "]"));
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
				
				new BotError(this, getEzString("NoTranslation"));
				
			}
			else{
				
				Dictionary dict = (Dictionary)getBuffer().get(BUFFER_LANG);
				
				String langChangeMessage = dict
						.getString("CommandLanguageChangingLanguage");
				
				dict.setLanguage(lang, country);
				getBuffer().push(dict, BUFFER_LANG);
				
				sendInfoMessage(langChangeMessage,
						dict.getString("LanguageName"));
				
			}
			
		}
		
	}
}
