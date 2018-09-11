package commands;

import utilities.abstracts.SimpleTextCommand;

/**
 * Easter egg command. We b0ts never terminates.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandTerminate extends SimpleTextCommand {
	
	@Override
	public String getTextToSend(){
		return lang("TERMINATE");
	}
	
	@Override
	public Object getCalls(){
		return TERMINATE;
	}
	
	@Override
	public String getCommandDescription(){
		return "Terminate the bot";
	}
	
}
