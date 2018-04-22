package vendor.exceptions;

public class JDANotSetException extends Exception {
	public JDANotSetException(){
		super("The JDA is not set!");
	}
}
