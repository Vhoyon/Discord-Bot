package vendor.objects;

import vendor.interfaces.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request implements Utils {
	
	public class Parameter {
		
		private String parameterName;
		private String parameterContent;
		
		private int position;
		private boolean acceptsContent;
		
		protected Parameter(){
			this.acceptsContent = true;
		}
		
		public Parameter(String parameter){
			this();
			
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
			
			this.setContent(paramContent);
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
		
		protected void setContent(String parameterContent){
			if(parameterContent == null)
				this.parameterContent = null;
			else
				this.parameterContent = parameterContent.replaceAll("\"", "");
		}
		
		public int getPosition(){
			return this.position;
		}
		
		protected void setPosition(int position){
			this.position = position;
		}
		
		public boolean doesAcceptContent(){
			return this.acceptsContent;
		}
		
		protected void setAcceptingContent(boolean acceptsContent){
			this.acceptsContent = acceptsContent;
			
			if(!acceptsContent && getContent() != null){
				setContent(null);
			}
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
	
	private String command;
	private String content;
	
	private String commandPrefix;
	
	private HashMap<String, Parameter> parameters;
	private HashMap<Parameter, ArrayList<String>> parametersLinks;
	private char parametersPrefix;
	
	private ArrayList<Parameter> duplicateParams;
	
	public Request(String receivedMessage){
		this(receivedMessage, DEFAULT_PARAMETER_PREFIX);
	}
	
	public Request(String receivedMessage, char parametersPrefix){
		this(receivedMessage, DEFAULT_COMMAND_PREFIX, parametersPrefix);
	}
	
	public Request(String receivedMessage, String commandPrefix,
			char parametersPrefix){
		
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
			
			duplicateParams = new ArrayList<>();
			
			boolean canRoll = true;
			
			for(int i = 0; i < possibleParams.size() && canRoll; i++){
				
				String possibleParam = possibleParams.get(i);
				
				int paramStartPos = -1;
				int paramEndPos = -1;
				
				if(possibleParam.equals(getParametersPrefix() + ""
						+ getParametersPrefix())){
					// If string is double parameter prefix, remove it and stop taking params
					
					paramStartPos = getContent().indexOf(possibleParam);
					paramEndPos = paramStartPos + possibleParam.length();
					
					canRoll = false;
					
				}
				else if(possibleParam.matches(getParametersPrefixProtected()
						+ "{1,2}[^\\s]+")){
					// If string is structured as a parameter, create it.
					
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
									
									newParam.setContent(possibleParamContent);
									
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
							
							Parameter newParam = new Parameter(singleParams[j],
									i + j);
							
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
											
											newParam.setContent(possibleParamContent);
											
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
					
				}
				
				if(paramStartPos != -1){
					String contentToRemove = getContent().substring(
							paramStartPos, paramEndPos);
					
					setContent(getContent().replaceFirst(contentToRemove, ""));
				}
				
			}
			
			if(getContent() != null)
				setContent(getContent().trim().replaceAll("\"", ""));
			
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
	
	public ArrayList<Parameter> getDuplicateParamList(){
		return this.duplicateParams;
	}
	
	public boolean hasError(){
		return this.getDuplicateParamList() != null
				&& this.getDuplicateParamList().size() != 0;
	}
	
	public String getDefaultErrorMessage(){
		
		if(!hasError()){
			return "This request has no errors!";
		}
		else{
			
			String pluralTester;
			
			if(this.getDuplicateParamList().size() == 1)
				pluralTester = "That parameter";
			else
				pluralTester = "Those parameters";
			
			StringBuilder message = new StringBuilder();
			
			message.append(pluralTester).append(
					" has been entered more than once : ");
			
			for(int i = 0; i < this.getDuplicateParamList().size(); i++){
				
				message.append("\n");
				
				if(this.getDuplicateParamList().size() != 1)
					message.append(i + 1).append(". ");
				else
					message.append("~ ");
				
				message.append(this.getDuplicateParamList().get(i).getName());
				
			}
			
			if(this.getDuplicateParamList().size() == 1)
				pluralTester = "that parameter";
			else
				pluralTester = "those parameters";
			
			message.append("\n")
					.append(format(
							"Only the first instance of {1} will be taken into consideration.",
							pluralTester));
			
			return message.toString();
			
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
	
	protected void setParameterContentLess(String paramName){
		Parameter paramFound = getParameter(paramName);
		
		if(paramFound == null){
			throw new NullPointerException("Parameter \"" + paramName
					+ "\"is not present or linked in this request.");
		}
		else{
			
			if(getContent() == null)
				setContent(paramFound.getContent());
			else
				setContent(getContent() + " " + paramFound.getContent());
			
			paramFound.setAcceptingContent(false);
			
		}
	}
	
	public void setParamsAsContentLess(
			ArrayList<String> paramsToTreatAsContentLess){
		this.setParamsAsContentLess(paramsToTreatAsContentLess
				.toArray(new String[0]));
	}
	
	public void setParamsAsContentLess(String[] paramsToTreatAsContentLess){
		
		for(String paramName : paramsToTreatAsContentLess){
			try{
				this.setParameterContentLess(paramName);
			}
			catch(NullPointerException e){}
		}
		
	}
	
}
