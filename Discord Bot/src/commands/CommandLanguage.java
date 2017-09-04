package commands;

import errorHandling.BotError;
import framework.Command;
import framework.Dictionary;

public class CommandLanguage extends Command {
	
	@Override
	public void action(){
		
		if(getContent() == null){
			sendMessage("Usage : "
					+ buildVCommand(LANGUAGE + " [language name]") + ".");
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
				lang = "fr";
				country = "CA";
				break;
			case "anglais":
			case "eng":
			case "english":
				lang = "en";
				country = "US";
				break;
			default:
				canChangeLanguage = false;
				break;
			}
			
			if(!canChangeLanguage){
				
				new BotError(this,
						"The bot hasn't been translated to that language!");
				
			}
			else{
				
				Dictionary dict = (Dictionary)getBuffer().get(BUFFER_LANG);
				dict.setLanguage(lang, country);
				getBuffer().push(dict, BUFFER_LANG);
				
				sendInfoMessage("The language has been set to : `"
						+ dict.getString("LanguageName") + "`!");
				
			}
			
		}
		
	}
}
