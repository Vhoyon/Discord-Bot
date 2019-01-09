package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.utilities.abstracts.SimpleTextCommand;

/**
 * Command that tells the user a hello back at him. :3
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see SimpleTextCommand
 */
public class CommandHello extends SimpleTextCommand {
	
	@Override
	public String getTextToSend(){
		return lang("HelloResponse", getUserName());
	}
	
	@Override
	public Boolean isTextInfoOneLiner(){
		return true;
	}
	
	@Override
	public String getCall(){
		return HELLO;
	}
	
	@Override
	public String getCommandDescription(){
		return "Being polite is my top priority!";
	}
	
}
