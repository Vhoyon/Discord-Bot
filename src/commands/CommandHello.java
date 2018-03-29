package commands;

public class CommandHello extends SimpleTextCommand {

	@Override
	public String getTextToSend(){
		return lang("HelloResponse", getUsername());
	}
	
	@Override
	public Boolean isTextInfoOneLiner(){
		return true;
	}
	
}
