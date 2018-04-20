package commands;

import utilities.abstracts.SimpleTextCommand;

public class CommandTerminate extends SimpleTextCommand {
	
	@Override
	public String getTextToSend(){
		return lang("TERMINATE");
	}
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			TERMINATE
		};
	}

	@Override
	public String getCommandDescription() {
		return "Terminate the bot";
	}
}
