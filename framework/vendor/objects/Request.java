package vendor.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vendor.abstracts.Translatable;
import vendor.exceptions.NoContentException;
import vendor.interfaces.Utils;

public class Request extends Translatable implements Utils {
	
	public class Parameter {
		
		private String parameter;
		private String parameterContent;
		
		protected Parameter(){}
		
		public Parameter(String parameter){
			if(parameter.matches(getParametersPrefix() + "{1,2}.+")){
				
				int paramDeclaratorLength = parameter.matches(getParametersPrefix() + "{1}.+") ? 1 : 2;
				
				this.parameter = parameter.substring(paramDeclaratorLength);
				
			}
			else{
				this.parameter = parameter;
			}
		}
		
		public Parameter(String paramName, String paramContent){
			this(paramName);
			
			this.setParameterContent(paramContent);
		}
		
		public String getParameter(){
			return parameter;
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
	
	public final static String DEFAULT_COMMAND_PREFIX = "!";
	public final static char DEFAULT_PARAMETER_PREFIX = '-';
	
	private final static String DEFAULT_LANG_DIRECTORY = "/vendor.lang.strings";
	
	private String command;
	private String content;
	
	private String langDirectory;
	
	private String commandPrefix;
	
	private HashMap<String, Parameter> parameters;
	private char parametersPrefix;
	
	private String error;
	
	public Request(String receivedMessage, Dictionary dictionary){
		this(receivedMessage, dictionary, DEFAULT_PARAMETER_PREFIX);
	}
	
	public Request(String receivedMessage, Dictionary dictionary,
			char parametersPrefix){
		this(receivedMessage, dictionary, DEFAULT_COMMAND_PREFIX,
				parametersPrefix);
	}
	
	public Request(String receivedMessage, Dictionary dictionary,
			String commandPrefix, char parametersPrefix){
		this(receivedMessage, dictionary, commandPrefix, parametersPrefix,
				DEFAULT_LANG_DIRECTORY);
	}
	
	public Request(String receivedMessage, Dictionary dictionary,
			String commandPrefix, char parametersPrefix, String langDirectory){
		
		this.setDictionary(dictionary);
		this.langDirectory = langDirectory;
		
		this.commandPrefix = commandPrefix;
		
		this.parametersPrefix = parametersPrefix;
		
		String[] messageSplit = splitCommandAndContent(receivedMessage);
		
		setCommand(messageSplit[0]);
		setContent(messageSplit[1]);
		
		if(getContent() != null){
			
			// Test if content contains parameters.
			// The params must be right after command for it to trigger.
			//				if(content.matches("(" + Parameter.PREFIX + ".+)+")){
			
			parameters = new HashMap<>();
			
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
				if(possibleParam.matches(getParametersPrefix() + "{1,2}[^\\s]+")){
					
					Parameter newParam = new Parameter(possibleParam);
					
					int paramStartPos;
					int paramEndPos;
					
					paramStartPos = getContent().indexOf(possibleParam);
					paramEndPos = paramStartPos + possibleParam.length();
					
					if(parameters.containsValue(newParam)){
						
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
									.matches(getParametersPrefix() + "{1,2}[^\\s]+")){
								
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
						
						parameters.put(newParam.getParameter(), newParam);
						
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
		return command.substring(getCommandPrefix().length());
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
	
	public String getCommandPrefix(){
		return this.commandPrefix;
	}
	
	public HashMap<String, Parameter> getParameters(){
		return parameters;
	}
	
	public Parameter getParameter(String... parameterNames)
			throws NoContentException{
		
		if(parameterNames == null || parameterNames.length == 0)
			throw new IllegalArgumentException(
					"The parametersName parameter cannot be null / empty!");
		
		try{
			
			for(String parameterName : parameterNames){
				
				Parameter paramFound = getParameters().get(parameterName);
				
				if(paramFound != null)
					return paramFound;
				
			}
			
		}
		catch(NullPointerException e){
			throw new NoContentException(getCommand());
		}
		
		throw new NoContentException(getCommand());
		
	}
	
	public char getParametersPrefix(){
		return parametersPrefix;
	}
	
	public String getError(){
		return this.error;
	}
	
	public boolean hasError(){
		return error != null;
	}
	
	public boolean hasParameter(String parameterName){
		if (getParameters() == null)
			return false;
		
		return getParameters().containsKey(parameterName);
	}
	
	public boolean hasParameter(String... parameterNames){
		
		for(String parameterName : parameterNames)
			if(this.hasParameter(parameterName))
				return true;
		
		return false;
		
	}
	
	public boolean addParameter(String paramName){
		return this.getParameters().put(paramName, new Parameter(paramName)) == null;
	}
	
	public boolean addParameter(String paramName, String paramContent){
		return this.getParameters().put(paramName,
				new Parameter(paramName, paramContent)) == null;
	}
	
	private String[] splitCommandAndContent(String command){
		
		// Remove leading / trailing spaces, as well as shrinking consecutives
		// whitespace.
		// Also, this adds a space at the end to force the split to be at least
		// of size 2, which means there will always be a command and some
		// content.
		String[] splitted = command.trim().replaceAll("\\s+", " ").concat(" ")
				.split(" ", 2);
		
		splitted[0] = splitted[0].toLowerCase();
		
		if(splitted[1].length() != 0){
			splitted[1] = splitted[1].trim();
		}
		
		return splitted;
		
	}
	
	private void handleDuplicateParameter(ArrayList<Parameter> duplicateParams){
		
		if(duplicateParams.size() != 0){
			
			String pluralTester;
			
			if(duplicateParams.size() == 1)
				pluralTester = langDirect(langDirectory
						+ ".RequestParamStartSingle");
			else
				pluralTester = langDirect(langDirectory
						+ ".RequestParamStartMultiple");
			
			StringBuilder message = new StringBuilder();
			
			message.append(pluralTester)
					.append(" ")
					.append(langDirect(langDirectory
							+ ".RequestParamStartFollowing")).append(" ");
			
			for(int i = 0; i < duplicateParams.size(); i++){
				
				if(duplicateParams.size() != 1)
					message.append("\n").append(i + 1).append(". ");
				
				message.append("`")
						.append(duplicateParams.get(i).getParameter())
						.append("`");
				
			}
			
			if(duplicateParams.size() == 1)
				pluralTester = langDirect(langDirectory
						+ ".RequestEndMessageSingle");
			else
				pluralTester = langDirect(langDirectory
						+ ".RequestEndMessageMultiple");
			
			message.append("\n*")
					.append(langDirect(langDirectory + ".RequestEndMessage",
							pluralTester)).append("*");
			
			this.error = message.toString();
			
		}
		
	}
	
}
