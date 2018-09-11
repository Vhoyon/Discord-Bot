package utilities.specifics;

import utilities.abstracts.Pool;

/**
 * Object that holds the values of each games added in the different
 * {@link utilities.abstracts.GameInteractionCommands} as a String.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class GamePool extends Pool<String> {
	
	/**
	 * Creates the pool of games using the games added in the parameter
	 * {@code games}.
	 * 
	 * @param games
	 *            The games to initialize the pool with.
	 * @since v0.4.0
	 */
	public GamePool(String... games){
		super(games);
	}
	
}
