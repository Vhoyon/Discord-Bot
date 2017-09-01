package framework;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
	
	public class Parameter {
		
		public final static String PREFIX = "--";
		
		private String parameter;
		private String parameterContent;
		
		private int paramStartPos;
		private int paramEndPos;
		
		public Parameter(String parameter){
			this.parameter = parameter;
		}
		
		public String getParameter(){
			return parameter.substring(PREFIX.length());
		}
		
		public String getParameterContent(){
			return parameterContent.replaceAll("\"", "");
		}
		
		public void setParameterContent(String parameterContent){
			this.parameterContent = parameterContent;
		}
		
		public int getParamStartPos(){
			return paramStartPos;
		}
		
		public void setParamStartPos(int paramStartPos){
			this.paramStartPos = paramStartPos;
		}
		
		public int getParamEndPos(){
			return paramEndPos;
		}
		
		public void setParamEndPos(int paramEndPos){
			this.paramEndPos = paramEndPos;
		}
		
	}
	
	private String command;
	private String content;
	private ArrayList<Parameter> parameters;
	
	public Request(String receivedMessage){
		
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
			
			boolean canRoll = true;
			
			for(int i = 0; i < possibleParams.size() && canRoll; i++){
				
				String possibleParam = possibleParams.get(i);
				
				// If string is structured as a parameter, create it.
				if(possibleParam.matches(Parameter.PREFIX + "[^\\s]+")){
					
					Parameter newParam = new Parameter(possibleParam);
					
					newParam.setParamStartPos(getContent().indexOf(
							possibleParam));
					
					try{
						
						String possibleParamContent = possibleParams.get(i + 1);
						
						// If the following String isn't another param, set
						// said String as the content for the current param.
						if(!possibleParamContent.matches(Parameter.PREFIX
								+ "[^\\s]+")){
							
							newParam.setParameterContent(possibleParamContent);
							
							i++;
							
							newParam.setParamEndPos(getContent().indexOf(
									possibleParamContent)
									+ possibleParamContent.length());
							
						}
						
					}
					catch(IndexOutOfBoundsException e){
						canRoll = false;
					}
					
					parameters.add(newParam);
					
					String contentToRemove = getContent().substring(
							newParam.getParamStartPos(),
							newParam.getParamEndPos());
					
					setContent(getContent().replace(contentToRemove, ""));
					
				}
				
			}
			
			setContent(getContent().trim());
			
		}
		
	}
	
	public String getCommand(){
		return command;
	}
	
	public void setCommand(String command){
		this.command = command.substring(Parameter.PREFIX.length());
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		
		if(content.length() == 0)
			this.content = null;
		else
			this.content = content;
		
	}
	
	public ArrayList<Parameter> getParameters(){
		return parameters;
	}
	
	private String[] splitCommandAndContent(String command){
		
		// Remove leading / trailing spaces (leading spaces are removed anyway)
		String[] splitted = command.trim().replaceAll("( )+", " ")
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
	
}