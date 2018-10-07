package io.github.vhoyon.bot.utilities.abstracts;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Object that extends {@link java.util.ArrayList} and adds the ability to
 * directly get the pool array as a simple array.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class Pool<E> extends ArrayList<E> {
	
	/**
	 * Creates a new Pool object with all the pollObjects added in the parameter
	 * {@code poolObjects}.
	 *
	 * @param poolObjects
	 *            Array of generics that will be treated as the initial list.
	 * @since v0.4.0
	 */
	public Pool(E[] poolObjects){
		super(new ArrayList<>(Arrays.asList(poolObjects)));
	}
	
	/**
	 * Gets a simple array containing all of this pool's objects.
	 *
	 * @return An array containing all of this pool's objects.
	 * @since v0.4.0
	 */
	@SuppressWarnings("unchecked")
	public E[] getPoolArray(){
		return (E[])this.toArray(new Object[this.size()]);
	}
	
}
