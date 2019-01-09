package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.utilities.abstracts.SimpleTextCommand;
import io.github.vhoyon.vramework.interfaces.Hidden;

/**
 * Easter egg command. We b0ts never terminates.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandTerminate extends SimpleTextCommand implements Hidden {
	
	@Override
	public String getTextToSend(){
		return lang("TERMINATE");
	}
	
	@Override
	public String getCall(){
		return TERMINATE;
	}
	
	@Override
	public String getCommandDescription(){
		return "Terminate the bot";
	}
	
}
