package vendor.objects;

import vendor.abstracts.Translatable;
import vendor.interfaces.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request extends Translatable implements Utils {
	
	public class Parameter {
		
		private String parameterName;
		private String parameterContent;
		
		private int position;
		
		protected Parameter(){}
		
		public Parameter(String parameter){
			if(parameter.matches(getParametersPrefixProtected() + "{1,2}.+")){
				
				int paramDeclaratorLength = parameter
						.matches(getParametersPrefixProtected() + "{2}.+") ? 2
						: 1;
				
				this.parameterName = parameter.substring(paramDeclaratorLength);
				
			}
			else{
				this.parameterName = parameter;
			}
		}
		
		protected Parameter(String paramName, int position){
			this(paramName);
			
			this.setPosition(position);
		}
		
		public Parameter(String paramName, String paramContent){
			this(paramName);
			
			this.setParameterContent(paramContent);
		}
		
		protected Parameter(String paramName, String paramContent, int position){
			this(paramName, paramContent);
			
			this.setPosition(position);
		}
		
		public String getName(){
			return parameterName;
		}
		
		public String getContent(){
			if(parameterContent == null)
				return null;
			else
				return parameterContent;
		}
		
		protected void setParameterContent(String parameterContent){
			this.parameterContent = parameterContent.replaceAll("\"", "");
		}
		
		public int getPosition(){
			return this.position;
		}
		
		protected void setPosition(int position){
			this.position = position;
		}
		
		@Override
		public boolean equals(Object obj){
			
			boolean isEqual = false;
			
			if(obj instanceof Parameter){
				
				Parameter parameterToCompare = (Parameter)obj;
				
				isEqual = getName().equals(parameterToCompare.getName());
				
			}
			
			return isEqual;
		}
		
		@Override
		public String toString(){
			return this.getContent();
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
	private HashMap<Parameter, ArrayList<String>> parametersLinks;
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
				if(possibleParam.matches(getParametersPrefixProtected()
						+ "{1,2}[^\\s]+")){
					
					int paramStartPos;
					int paramEndPos;
					
					paramStartPos = getContent().indexOf(possibleParam);
					paramEndPos = paramStartPos + possibleParam.length();
					
					if(possibleParam.matches(getParametersPrefixProtected()
							+ "{2}[^\\s]+")){
						// Doubled param prefix means that all letters count as one param
						
						Parameter newParam = new Parameter(possibleParam, i);
						
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
										.matches(getParametersPrefixProtected()
												+ "{1,2}[^\\s]+")){
									
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
							
							parameters.put(newParam.getName(), newParam);
							
						}
						
					}
					else{
						// Single param prefix means that all letters counts as a different param
						
						String[] singleParams = possibleParam.substring(1,
								possibleParam.length()).split("");
						
						for(int j = 0; j < singleParams.length && canRoll; j++){
							
							Parameter newParam = new Parameter(singleParams[j], i + j);
							
							if(parameters.containsValue(newParam)){
								
								if(!duplicateParams.contains(newParam))
									duplicateParams.add(newParam);
								
							}
							else{
								
								if(j == singleParams.length - 1){
									
									try{
										String possibleParamContent = possibleParams
												.get(i + 1);
										
										// If the following String isn't another param, set
										// said String as the content for the current param.
										if(!possibleParamContent
												.matches(getParametersPrefixProtected()
														+ "{1,2}[^\\s]+")){
											
											newParam.setParameterContent(possibleParamContent);
											
											i++;
											
											paramEndPos = getContent().indexOf(
													possibleParamContent)
													+ possibleParamContent
															.length();
											
										}
										
									}
									catch(IndexOutOfBoundsException e){
										canRoll = false;
									}
									
								}
								
								parameters.put(newParam.getName(), newParam);
								
							}
							
						}
						
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
		return this.getCommandNoFormat().substring(getCommandPrefix().length());
	}
	
	public String getCommandNoFormat(){
		return this.command;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public String getContent(){
		return this.content;
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
		return this.parameters;
	}
	
	public HashMap<Parameter, ArrayList<String>> getParametersLinks(){
		return this.parametersLinks;
	}
	
	public Parameter getParameter(String... parameterNames){
		
		if(!hasParameters()){
			return null;
		}
		
		if(parameterNames == null || parameterNames.length == 0)
			throw new IllegalArgumentException(
					"The parametersName parameter cannot be null / empty!");
		
		for(String parameterName : parameterNames){
			
			if(getParametersLinks() != null){
				
				for(Map.Entry<Parameter, ArrayList<String>> entry : getParametersLinks()
						.entrySet()){
					
					if(entry.getValue().contains(parameterName))
						return entry.getKey();
					
				}
				
			}
			
			Parameter paramFound = getParameters().get(parameterName);
			
			if(paramFound != null)
				return paramFound;
			
		}
		
		return null;
		
	}
	
	public char getParametersPrefix(){
		return this.parametersPrefix;
	}
	
	private String getParametersPrefixProtected(){
		return Pattern.quote(String.valueOf(getParametersPrefix()));
	}
	
	public String getError(){
		return this.error;
	}
	
	public boolean hasError(){
		return this.getError() != null;
	}
	
	// public boolean hasParameter(String parameterName){
	// 	if(getParameters() == null)
	// 		return false;
	
	// 	return this.getParameters().containsKey(parameterName);
	// }
	
	public boolean hasParameter(String... parameterNames){
		return getParameter(parameterNames) != null;
	}
	
	public boolean hasParameters(){
		return getParameters() != null;
	}
	
	public void onParameterPresent(String parameterName,
			Consumer<Parameter> onParamPresent){
		onParameterPresent(parameterName, onParamPresent, null);
	}
	
	public void onParameterPresent(String parameterName,
			Consumer<Parameter> onParamPresent, Runnable onParamNotPresent){
		
		Parameter param = null;
		
		param = getParameter(parameterName);
		
		if(param != null){
			onParamPresent.accept(param);
		}
		else if(onParamNotPresent != null){
			onParamNotPresent.run();
		}
		
	}
	
	// public boolean addParameter(String paramName){
	// 	return this.getParameters().put(paramName, new Parameter(paramName)) == null;
	// }
	
	// public boolean addParameter(String paramName, String paramContent){
	// 	return this.getParameters().put(paramName,
	// 			new Parameter(paramName, paramContent)) == null;
	// }
	
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
				
				message.append("`").append(duplicateParams.get(i).getName())
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
	
	public void setParamLinkMap(ArrayList<ArrayList<String>> map){
		
		if(getParameters() != null)
			getParameters().forEach((key, param) -> {
				
				for(ArrayList<String> paramsGroup : map){
					
					if(paramsGroup.contains(key)){
						
						if(this.parametersLinks == null){
							this.parametersLinks = new HashMap<>();
						}
						
						this.parametersLinks.put(param, paramsGroup);
						break;
						
					}
					
				}
				
			});
		
	}
	
}
