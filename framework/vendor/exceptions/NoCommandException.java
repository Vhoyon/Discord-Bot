package vendor.exceptions;

public class NoCommandException extends Exception {
	public NoCommandException(){
		super("The message received is not a command.");
	}
}