package ressources;

import vendor.modules.Environment;

public interface Utils {
	
	public default String format(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	public default boolean isDebugging(){
		return env("DEBUG", false);
	}
	
	public default <EnvVar> EnvVar env(String key, Object defaultValue){
		return Environment.getEnvVar(key, defaultValue);
	}
	
	public default <EnvVar> EnvVar env(String key){
		return env(key, null);
	}
	
}
