package ressources;

import vendor.Framework;

public interface Utils {
	
	public default String format(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	public default boolean isDebugging(){
		return envBool("DEBUG", false);
	}
	
	public default Object env(String key, Object defaultValue){
		return Framework.getEnvVar(key, defaultValue);
	}
	
	public default Object env(String key){
		return env(key, null);
	}
	
	public default boolean envBool(String key, Object defaultValue){
		Object value = env(key, defaultValue);
		
		return ((Boolean)value).booleanValue();
	}
	
	public default Object envBool(String key){
		return envBool(key, null);
	}
	
}
