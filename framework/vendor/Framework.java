package vendor;

import vendor.abstracts.Module;

import java.awt.*;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;

public class Framework {
	
	private static boolean IS_RUNNING_FROM_TERMINAL;
	private static String RUNNABLE_SYSTEM_PATH;
	private static Date BUILD_STARTED_AT;
	
	private Framework(){}
	
	public static boolean isRunningFromTerminal(){
		return IS_RUNNING_FROM_TERMINAL;
	}
	
	public static String runnableSystemPath(){
		return RUNNABLE_SYSTEM_PATH;
	}
	
	public static Date buildStartedAt(){
		return BUILD_STARTED_AT;
	}
	
	private static String[] modules =
	{
		"Environment", "Logger", "Metrics"
	};
	
	public static void build() throws Exception{
		
		Framework.setupGlobalVariables();
		
		StringBuilder errors = new StringBuilder();
		
		for(String moduleName : modules){
			
			try{
				String formattedModuleName = moduleName.replaceAll("/", ".");
				
				Class<?> moduleClass = Class.forName("vendor.modules."
						+ formattedModuleName);
				
				if(Module.class.isAssignableFrom(moduleClass)){
					
					Module module = (Module)moduleClass.newInstance();
					
					try{
						module.build();
					}
					catch(Exception e){
						errors.append(module.getLoadingErrorMessage(e)).append(
								"\n\n");
					}
					
				}
				
			}
			catch(ClassNotFoundException | InstantiationException
					| IllegalAccessException e){
				errors.append("Module \"").append(e.getMessage())
						.append("\" not found.").append("\n");
			}
			
		}
		
		if(errors.length() != 0){
			throw new Exception(errors.toString());
		}
		
	}
	
	private static void setupGlobalVariables(){
		
		IS_RUNNING_FROM_TERMINAL = getIsRunningFromTerminal();
		RUNNABLE_SYSTEM_PATH = getRunnableFolderPath();
		BUILD_STARTED_AT = getBuildStartedAt();
		
	}
	
	private static boolean getIsRunningFromTerminal(){
		Console console = System.console();
		
		return !(console == null && !GraphicsEnvironment.isHeadless());
	}
	
	private static String getRunnableFolderPath(){
		
		String systemPath = Framework.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		
		try{
			systemPath = URLDecoder.decode(systemPath, "UTF-8");
		}
		catch(UnsupportedEncodingException e){}
		
		File runner = new File(systemPath);
		
		String decoratedSystemPath = runner.getParent() + File.separator;
		
		return decoratedSystemPath;
		
	}
	
	private static Date getBuildStartedAt(){
		return new Date();
	}
	
}
