package errorHandling.exceptions;

public class BadContentException extends Exception {
	public BadContentException(){
		super();
	}
	
	public BadContentException(String explanation){
		super(explanation);
	}
}
