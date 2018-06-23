package vendor.utilities.formatting;

public interface DiscordFormatter {
	
	/**
	 * Format the text as a code line.
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>`</b>.
	 */
	default String code(String text){
		return "`" + text + "`";
	}
	
	/**
	 * Format the text as bold.
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>**</b>.
	 */
	default String bold(String text){
		return "**" + text + "**";
	}
	
	/**
	 * Format the text as italic.
	 * 
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>_</b>.
	 */
	default String ital(String text){
		return "_" + text + "_";
	}
	
	/**
	 * Format the text as underscored.
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>__</b>.
	 */
	default String unde(String text){
		return "__" + text + "__";
	}
	
	/**
	 * Format the text as strikethrough.
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>~~</b>.
	 */
	default String strk(String text){
		return "~~" + text + "~~";
	}
	
	/**
	 * Format the text as a block of code (with no specified language).
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>```</b> without any language specified.
	 */
	default String bloc(String text){
		return bloc(text, "");
	}
	
	/**
	 * Format the text as a block of code for a specified language.
	 *
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>```</b> with the language specified in the parameter
	 *         <i>lang</i>.
	 */
	default String bloc(String text, String lang){
		return "```" + lang + "\n" + text + "\n```";
	}
	
}
