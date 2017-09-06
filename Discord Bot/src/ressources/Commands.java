package ressources;

public interface Commands {
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter.
	 */
	public default String buildCommand(String command){
		return Ressources.PREFIX + command;
	}
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter, surrounded by two
	 *         "<b>`</b>" tick, meaning the visual will be like code in Discord.
	 */
	public default String buildVCommand(String command){
		return "`" + buildCommand(command) + "`";
	}
	
	String HELLO = "hello";
	String HELP = "help";
	String CONNECT = "connect";
	String DISCONNECT = "disconnect";
	String CLEAR = "clear";
	String SPAM = "spam";
	String STOP = "stop";
	String GAME = "game";
	String GAME_ADD = "game_add";
	String GAME_REMOVE = "game_remove";
	String GAME_ROLL = "game_roll";
	String GAME_ROLL_ALT = "roll";
	String GAME_LIST = "game_list";
	String TEST = "test";
	String CONFIRM = "confirm";
	String TERMINATE = "terminate";
	String CANCEL = "cancel";
	String LANGUAGE = "language";
	String LANG = "lang";
	String TIMER = "timer";
	
}
