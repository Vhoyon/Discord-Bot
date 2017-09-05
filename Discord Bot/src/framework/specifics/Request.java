package framework.specifics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import errorHandling.AbstractBotError;
import errorHandling.BotError;
import errorHandling.exceptions.NoParameterContentException;
import framework.Dictionary;

public class Request {
	
	public class Parameter {
		
		public final static String PREFIX = "--";
		
		private String parameter;
		private String parameterContent;
		
		protected Parameter(){}
		
		public Parameter(String parameter){
			this.parameter = parameter;
		}
		
		public String getParameter(){
			return parameter.substring(PREFIX.length());
		}
		
		public String getParameterContent(){
			if(parameterContent == null)
				return null;
			else
				return parameterContent.replaceAll("\"", "");
		}
		
		protected void setParameterContent(String parameterContent){
			this.parameterContent = parameterContent;
		}
		
		@Override
		public boolean equals(Object obj){
			
			boolean isEqual = false;
			
			if(obj instanceof Parameter){
				
				Parameter parameterToCompare = (Parameter)obj;
				
				isEqual = getParameter().equals(
						parameterToCompare.getParameter());
				
			}
			
			return isEqual;
		}
		
		@Override
		public String toString(){
			return this.getParameterContent();
		}
		
	}
	
	private String command;
	private String content;
	private ArrayList<Parameter> parameters;
	private Dictionary dict;
	
	private AbstractBotError error;
	
	public Request(String receivedMessage, Dictionary dictionary){
		
		this.dict = dictionary;
		
		String[] messageSplit = splitCommandAndContent(receivedMessage);
		
		setCommand(messageSplit[0]);
		setContent(messageSplit[1]);
		
		if(content != null){
			
			// Test if content contains parameters.
			// The params must be right after command for it to trigger.
			//				if(content.matches("(" + Parameter.PREFIX + ".+)+")){
			
			parameters = new ArrayList<>();
			
			// Splits the content : Search for all spaces, except thoses
			// in double quotes and put all what's found in the
			// possibleParams ArrayList.
			// Necessary since .split() removes the wanted Strings.
			ArrayList<String> possibleParams = new ArrayList<>();
			Matcher matcher = Pattern.compile(
					"[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(content);
			while(matcher.find()){
				possibleParams.add(matcher.group());
			}
			ArrayList<Parameter> duplicateParams = new ArrayList<>();
			
			boolean canRoll = true;
			
			for(int i = 0; i < possibleParams.size() && canRoll; i++){
				
				String possibleParam = possibleParams.get(i);
				
				// If string is structured as a parameter, create it.
				if(possibleParam.matches(Parameter.PREFIX + "[^\\s]+")){
					
					Parameter newParam = new Parameter(possibleParam);
					
					int paramStartPos;
					int paramEndPos;
					
					paramStartPos = getContent().indexOf(possibleParam);
					paramEndPos = paramStartPos + possibleParam.length();
					
					if(parameters.contains(newParam)){
						
						if(!duplicateParams.contains(newParam))
							duplicateParams.add(newParam);
						
					}
					else{
						
						try{
							
							String possibleParamContent = possibleParams
									.get(i + 1);
							
							// If the following String isn't another param, set
							// said String as the content for the current param.
							if(!possibleParamContent.matches(Parameter.PREFIX
									+ "[^\\s]+")){
								
								newParam.setParameterContent(possibleParamContent);
								
								i++;
								
								paramEndPos = getContent().indexOf(
										possibleParamContent)
										+ possibleParamContent.length();
								
							}
							
						}
						catch(IndexOutOfBoundsException e){
							canRoll = false;
						}
						
						parameters.add(newParam);
						
					}
					
					String contentToRemove = getContent().substring(
							paramStartPos, paramEndPos);
					
					setContent(getContent().replaceFirst(contentToRemove, ""));
					
				}
				
			}
			
			if(getContent() != null)
				setContent(getContent().trim());
			
			handleDuplicateParameter(duplicateParams);
			
		}
		
	}
	
	public String getCommand(){
		return command.substring(Parameter.PREFIX.length());
	}
	
	public String getCommandNoFormat(){
		return command;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		
		if(content == null || content.length() == 0)
			this.content = null;
		else
			this.content = content;
		
	}
	
	public ArrayList<Parameter> getParameters(){
		return parameters;
	}
	
	public Parameter getParameter(String parameterName)
			throws NoParameterContentException{
		
		Parameter parameterFound = null;
		
		try{
			for(Parameter parameter : parameters){
				
				if(parameter.getParameter().equals(parameterName)){
					
					parameterFound = parameter;
					break;
					
				}
				
			}
		}
		catch(NullPointerException e){
			throw new NoParameterContentException(getCommand());
		}
		
		return parameterFound;
		
	}
	
	public AbstractBotError getErrorMessage(){
		return error;
	}
	
	public boolean isParameterPresent(Parameter parameter){
		return parameters.contains(parameter);
	}
	
	public boolean isParameterPresent(String parameterName){
		return isParameterPresent(new Parameter(parameterName));
	}
	
	private String[] splitCommandAndContent(String command){
		
		// Remove leading / trailing spaces (leading spaces are removed anyway)
		String[] splitted = command.trim().replaceAll("\\s+", " ")
				.split(" ", 2);
		
		if(splitted.length == 1){
			// TODO : Find better way you lazy basterd.
			String actualCommand = splitted[0];
			splitted = new String[2];
			splitted[0] = actualCommand;
		}
		
		splitted[0] = splitted[0].toLowerCase();
		
		return splitted;
		
	}
	
	private void handleDuplicateParameter(ArrayList<Parameter> duplicateParams){
		
		if(duplicateParams.size() != 0){
			
			String pluralTester;
			
			if(duplicateParams.size() == 1)
				pluralTester = dict.getString("RequestParamStartSingle");
			else
				pluralTester = dict.getString("RequestParamStartMultiple");
			
			StringBuilder message = new StringBuilder(pluralTester + " "
					+ dict.getString("RequestParamStartFollowing") + " ");
			
			for(int i = 0; i < duplicateParams.size(); i++){
				
				if(duplicateParams.size() != 1)
					message.append("\n" + (i + 1) + ". ");
				
				message.append("`" + duplicateParams.get(i).getParameter()
						+ "`");
				
			}
			
			if(duplicateParams.size() == 1)
				pluralTester = dict.getString("RequestEndMessageSingle");
			else
				pluralTester = dict.getString("RequestEndMessageMultiple");
			
			message.append("\n*"
					+ String.format(dict.getString("RequestEndMessage"),
							pluralTester) + "*");
			
			this.error = new BotError(message.toString(), false);
			
		}
		
	}
	
}