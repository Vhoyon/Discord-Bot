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
	
	public String HELLO = "hello";
	public String HELP = "help";
	public String CONNECT = "connect";
	public String DISCONNECT = "disconnect";
	public String CLEAR = "clear";
	public String SPAM = "spam";
	public String STOP = "stop";
	public String GAME = "game";
	public String GAME_ADD = "game_add";
	public String GAME_REMOVE = "game_remove";
	public String GAME_ROLL = "game_roll";
	public String GAME_ROLL_ALT = "roll";
	public String GAME_LIST = "game_list";
	public String TEST = "test";
	public String CONFIRM = "confirm";
	public String TERMINATE = "terminate";
	public String CANCEL = "cancel";
	
}
