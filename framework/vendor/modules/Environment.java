package vendor.modules;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vendor.Framework;
import vendor.abstracts.Module;
import errorHandling.exceptions.BadFileContentException;

public class Environment extends Module {
	
	private static HashMap<String, String> envVars;
	
	private final String WARNINGS = "WARNINGS";
	private final String SPACED_KEY_ERRORS = "SPACED_KEY_ERRORS";
	private final String LINE_ERRORS = "LINE_ERRORS";
	
	public void build() throws Exception{
		
		try{
			
			InputStream inputStream = Framework.class
					.getResourceAsStream("/.env");
			
			InputStreamReader streamReader = new InputStreamReader(inputStream,
					StandardCharsets.UTF_8);
			
			BufferedReader reader = new BufferedReader(streamReader);
			
			envVars = new HashMap<>();
			
			String line;
			
			for(int i = 1; (line = reader.readLine()) != null; i++){
				
				// Remove comment from line if there's content in the line
				if(line.length() != 0){
					Matcher matcher = Pattern.compile("(\\s)*#.*")
							.matcher(line);
					int index = matcher.find() ? matcher.start() : -1;
					
					if(index != -1)
						line = line.substring(0, index);
				}
				
				if(line.length() != 0){
					
					String[] keyValue = line.trim().split("=", 2);
					
					if(keyValue.length != 2){
						
						addError(LINE_ERRORS, "Line " + i + ": \"" + line
								+ "\"");
						
					}
					else{
						
						if(keyValue[1].length() == 0){
							addWarning(WARNINGS, "The variable keyed \""
									+ keyValue[0] + "\" at line " + i
									+ " is empty, is that supposed to be..?");
						}
						
						if(keyValue[0].contains(" ")){
							
							addError(SPACED_KEY_ERRORS, "Line " + i + ": \""
									+ line + "\"");
							
						}
						
					}
					
					if(!hasErrors()){
						envVars.put(keyValue[0].toLowerCase(), keyValue[1]);
					}
					
				}
				
			}
			
			handleIssues();
			
		}
		catch(IOException | NullPointerException e){
			throw new FileNotFoundException(
					"The environment file \".env\" was not found or is unavailable at this time.");
		}
		
	}
	
	public static <EnvVar> EnvVar getVar(String key){
		return getVar(key, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <EnvVar> EnvVar getVar(String key, Object defaultValue){
		String value = envVars.get(key.toLowerCase());
		
		if(value == null || value.equals("")){
			return (EnvVar)defaultValue;
		}
		
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
			return (EnvVar)Boolean.valueOf(value);
		}
		else if(value.matches("^[0-9]+$")){
			
			try{
				Integer number = Integer.valueOf(value);
				
				return (EnvVar)number;
			}
			catch(NumberFormatException e){}
			
		}
		else if(value.matches("^[0-9]+(\\.[0-9]+)?$")){
			
			try{
				Double number = Double.valueOf(value);
				
				return (EnvVar)number;
			}
			catch(NumberFormatException e){}
			
		}
		
		return (EnvVar)(String)value;
	}
	
	@Override
	protected void handleIssuesLogic() throws Exception{
		
		StringBuilder builder = new StringBuilder();
		
		if(!hasErrors()){
			
			builder.append("WARNINGS!");
			
			ArrayList<String> warnings = getWarnings(WARNINGS);
			
			if(warnings != null){
				
				builder.append("\n");
				
				for(String warning : warnings){
					builder.append("\n").append(warning);
				}
				
			}
			
			System.err.println(builder.append("\n").toString());
			
		}
		else{
			
			builder.append("ERRORS!");
			
			ArrayList<String> spaceErrors = getErrors(SPACED_KEY_ERRORS);
			
			if(spaceErrors != null){
				
				builder.append("\n\nSome keys ("
						+ spaceErrors.size()
						+ ") has space(s) in them : Keys must NOT contain spaces.\n");
				
				for(String error : spaceErrors){
					builder.append("\n").append(error);
				}
				
			}
			
			ArrayList<String> lineErrors = getErrors(LINE_ERRORS);
			
			if(lineErrors != null){
				
				builder.append("\n\nSome variables ("
						+ lineErrors.size()
						+ ") are not well formatted. Please follow the \"KEY=value\" format.\n");
				
				for(String error : lineErrors){
					builder.append("\n").append(error);
				}
				
			}
			
			throw new BadFileContentException(builder.toString());
			
		}
		
	}
	
}
