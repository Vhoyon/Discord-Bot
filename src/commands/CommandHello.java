package commands;

import utilities.abstracts.SimpleTextCommand;

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
	public String[] getCalls(){
		return new String[]
		{
			HELLO
		};
	}

	@Override
	public String getCommandDescription() {
		return "Being polite is my top priority!";
	}

}
