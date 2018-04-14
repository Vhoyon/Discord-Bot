package vendor.interfaces;

import vendor.modules.Environment;

public interface Utils {
	
	default String format(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	default boolean isDebugging(){
		return env("DEBUG", false);
	}
	
	default <EnvVar> EnvVar env(String key, Object defaultValue){
		return Environment.getVar(key, defaultValue);
	}
	
	default <EnvVar> EnvVar env(String key){
		return env(key, null);
	}
	
}
