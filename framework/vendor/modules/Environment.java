package vendor.modules;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import vendor.Framework;
import vendor.abstracts.Module;
import errorHandling.exceptions.BadFileContentException;

public class Environment extends Module {
	
	private static HashMap<String, String> envVars;
	
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
				
				if(line.length() > 0 && !line.startsWith("#")){
					
					String[] keyValue = line.split("=", 2);
					
					if(keyValue.length != 2){
						throw new BadFileContentException(
								"A variable is not well formatted on line "
										+ i
										+ ". Please follow the \"KEY=value\" format.");
					}
					else{
						
						if(keyValue[1].length() == 0){
							System.out.println("The variable keyed \""
									+ keyValue[0] + "\" at line " + i
									+ " is empty, is that supposed to be..?");
						}
						
					}
					
					envVars.put(keyValue[0].toLowerCase(), keyValue[1]);
					
				}
				
			}
			
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
	
}
