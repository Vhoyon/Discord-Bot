package commands;

import utilities.abstracts.SimpleTextCommand;

public class CommandTerminate extends SimpleTextCommand {
	
	@Override
	public String getTextToSend(){
		return lang("TERMINATE");
	}
	
}
