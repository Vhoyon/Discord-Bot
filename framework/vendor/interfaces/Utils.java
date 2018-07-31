package vendor.interfaces;

import java.util.regex.Pattern;

import vendor.Framework;
import vendor.modules.Environment;

public interface Utils {
	
	default String formatS(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	default String format(String stringToFormat, Object... replacements){
		
		String protectedString = Pattern.compile("[()\\[\\]%.+?^$\\\\|]")
				.matcher(stringToFormat).replaceAll("\\\\$0");
		
		String noLeadingZeroString = protectedString.replaceAll("\\{0+", "\\{");
		
		String convertedString = noLeadingZeroString
				.replaceAll("\\{([1-9][0-9]*)\\}", "\\%$1\\$s")
				.replaceAll("\\{\\^(0*[1-9][0-9]*)\\}", "\\{$1\\}");
		
		return formatS(convertedString, replacements);
		
	}
	
	default boolean isDebugging(){
		return (boolean)env("DEBUG", false) || Framework.isDebugging();
	}
	
	default <EnvVar> EnvVar env(String key, Object defaultValue){
		return Environment.getVar(key, defaultValue);
	}
	
	default <EnvVar> EnvVar env(String key){
		return env(key, null);
	}
	
	default boolean hasEnv(String key){
		return Environment.hasVar(key);
	}

	static String buildKey(String baseKey, String... additionalKeys){
		StringBuilder keyBuilder = new StringBuilder(baseKey);

		for (String additionalKey : additionalKeys)
			keyBuilder.append("_").append(additionalKey);

		return keyBuilder.toString();
	}
	
}
