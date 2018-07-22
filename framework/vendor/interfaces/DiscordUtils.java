package vendor.interfaces;

public interface DiscordUtils {
	
	default boolean isStringMention(String string){
		return string.matches("^<@[0-9]{18}>$");
	}
	
	default String getIdFromStringMention(String stringMention){
		return stringMention.replaceAll("<@([0-9]{18})>", "$1");
	}
	
}
