package utilities.specifics;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilities.Dictionary;
import utilities.interfaces.Resources;
import errorHandling.AbstractBotError;
import errorHandling.BotError;
import errorHandling.exceptions.NoParameterContentException;

public class Request {
	
	public class Parameter {
		
		private String parameter;
		private String parameterContent;
		
		protected Parameter(){}
		
		public Parameter(String parameter){
			if(!parameter.matches(getParametersPrefix() + ".+"))
				this.parameter = getParametersPrefix() + parameter;
			else
				this.parameter = parameter;
		}
		
		public Parameter(String paramName, String paramContent){
			this(paramName);
			
			this.setParameterContent(paramContent);
		}
		
		public String getParameter(){
			return parameter.substring(getParametersPrefix().length());
		}
		
		public String getParameterContent(){
			if(parameterContent == null)
				return null;
			else
				return parameterContent;
		}
		
		protected void setParameterContent(String parameterContent){
			this.parameterContent = parameterContent.replaceAll("\"", "");
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
	private Dictionary dict;
	
	private ArrayList<Parameter> parameters;
	private String parametersPrefix;
	
	private AbstractBotError error;
	
	public Request(String receivedMessage, Dictionary dictionary){
		this(receivedMessage, dictionary, Resources.PARAMETER_PREFIX);
	}
	
	public Request(String receivedMessage, Dictionary dictionary,
			String parametersPrefix){
		
		this.dict = dictionary;
		
		this.parametersPrefix = parametersPrefix;
		
		String[] messageSplit = splitCommandAndContent(receivedMessage);
		
		setCommand(messageSplit[0]);
		setContent(messageSplit[1]);
		
		if(getContent() != null){
			
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
				if(possibleParam.matches(getParametersPrefix() + "[^\\s]+")){
					
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
							if(!possibleParamContent
									.matches(getParametersPrefix() + "[^\\s]+")){
								
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
				setContent(getContent().trim().replaceAll("\"", ""));
			
			handleDuplicateParameter(duplicateParams);
			
		}
		
	}
	
	public String getCommand(){
		return command.substring(Resources.PREFIX.length());
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
	
	public String getParametersPrefix(){
		return parametersPrefix;
	}
	
	public AbstractBotError getError(){
		return error;
	}
	
	public boolean hasErrors(){
		return error != null;
	}
	
	public boolean hasParameter(Parameter... parameters){
		
		for(Parameter parameter : parameters)
			if(this.hasParameter(parameter))
				return true;
		
		return false;
		
	}
	
	public boolean hasParameter(Parameter parameter){
		try{
			return this.getParameters().contains(parameter);
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	public boolean hasParameter(String parameterName){
		return hasParameter(new Parameter(parameterName));
	}
	
	public boolean hasParameter(String... parameterNames){
		
		ArrayList<Parameter> params = new ArrayList<>();
		
		for(String paramName : parameterNames){
			params.add(new Parameter(paramName));
		}
		
		return hasParameter(params.toArray(new Parameter[0]));
		
	}
	
	public boolean addParameter(String paramName){
		return this.getParameters().add(new Parameter(paramName));
	}
	
	public boolean addParameter(String paramName, String paramContent){
		return this.getParameters().add(new Parameter(paramName, paramContent));
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
				pluralTester = dict.getDirectString("RequestParamStartSingle");
			else
				pluralTester = dict
						.getDirectString("RequestParamStartMultiple");
			
			StringBuilder message = new StringBuilder(pluralTester + " "
					+ dict.getDirectString("RequestParamStartFollowing") + " ");
			
			for(int i = 0; i < duplicateParams.size(); i++){
				
				if(duplicateParams.size() != 1)
					message.append("\n" + (i + 1) + ". ");
				
				message.append("`" + duplicateParams.get(i).getParameter()
						+ "`");
				
			}
			
			if(duplicateParams.size() == 1)
				pluralTester = dict.getDirectString("RequestEndMessageSingle");
			else
				pluralTester = dict
						.getDirectString("RequestEndMessageMultiple");
			
			message.append("\n*"
					+ String.format(dict.getDirectString("RequestEndMessage"),
							pluralTester) + "*");
			
			this.error = new BotError(message.toString(), false);
			
		}
		
	}
	
}