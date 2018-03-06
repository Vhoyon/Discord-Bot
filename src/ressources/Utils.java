package ressources;

public interface Utils {
	
	public default String format(String stringToFormat, Object... replacements){
		return Utils.formatThis(stringToFormat, replacements);
	}
	
	public static String formatThis(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
	public static boolean isDebugging(){
		return System.getProperty("debug-mode") != null;
	}
	
}
