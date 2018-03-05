package ressources;

public interface Utils {
	
	public static String format(String stringToFormat, Object... replacements){
		return String.format(stringToFormat, replacements);
	}
	
}
