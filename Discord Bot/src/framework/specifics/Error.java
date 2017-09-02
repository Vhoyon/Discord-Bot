package framework.specifics;

import framework.Command;

public class Error {
	
	private Command commandInError;
	private String errorMessage;
	
	public Error(Command commandInError, String errorMessage){
		
		this.commandInError = commandInError;
		this.errorMessage = errorMessage;
		
		this.errorAction();
		
	}
	
	private void errorAction(){
		
		
		
	}
	
}
