package vendor.exceptions;

public class NoParameterContentException extends Exception {
	public NoParameterContentException(String commandName){
		super("No content has been set for the command " + commandName + ".");
	}
}
