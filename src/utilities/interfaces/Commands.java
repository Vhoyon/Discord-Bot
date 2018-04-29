package utilities.interfaces;

public interface Commands {
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter.
	 */
	default String buildCommand(String command){
		return Resources.PREFIX + command;
	}
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter, surrounded by two
	 *         "<b>`</b>" tick, meaning the visual will be like code in Discord.
	 */
	default String buildVCommand(String command){
		return buildVText(buildCommand(command));
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	default String buildParameter(String parameter){
		return Resources.PARAMETER_PREFIX + parameter;
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	default String buildVParameter(String parameter){
		return buildVText(buildParameter(parameter));
	}
	
	/**
	 * @return A String that contains the <i>text</i> parameter enclosed with
	 *         <b>`</b>".
	 */
	default String buildVText(String text){
		return "`" + text + "`";
	}
	
	String TEST = "test";
	
	String HELLO = "hello";
	String HELP = "help";
	String CLEAR = "clear";
	String SPAM = "spam";
	String STOP = "stop";
	String GAME = "game";
	String GAME_ADD = "game_add";
	String GAME_REMOVE = "game_remove";
	String GAME_ROLL = "game_roll";
	String GAME_ROLL_ALT = "roll";
	String GAME_LIST = "game_list";
	String CONFIRM = "confirm";
	String TERMINATE = "terminate";
	String CANCEL = "cancel";
	String LANGUAGE = "language";
	String LANG = "lang";
	String TIMER = "timer";
	String MUSIC_PLAY = "play";
	String MUSIC_PAUSE = "pause";
	String MUSIC_SKIP = "skip";
	String MUSIC_LOOP = "loop";
	String MUSIC_SKIP_ALL1 = "skip_all";
	String MUSIC_SKIP_ALL2 = "skip-all";
	String MUSIC_SKIP_ALL3 = "skipall";
	String MUSIC_VOLUME = "volume";
	String MUSIC_LIST = "list";
	String MUSIC_DISCONNECT = "disconnect";
	
}
