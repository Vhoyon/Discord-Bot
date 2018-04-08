package vendor.abstracts;

import vendor.objects.Dictionary;

public abstract class Translatable {
	
	private Dictionary dict;
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	public Dictionary getDictionary(){
		return this.dict;
	}
	
	public String lang(boolean isFullString, String key){
		return isFullString ? this.getDictionary().getDirectString(key)
				: lang(key);
	}
	
	public String lang(boolean isFullString, String key, Object... replacements){
		return isFullString ? this.getDictionary().getDirectString(key,
				replacements) : lang(key, replacements);
	}
	
	/**
	 * Legacy method to directly get resources with the key supplied without
	 * testing for class possibility. Also applies a formatting to replace
	 * variables in the lang resource that has been returned.
	 * <p>
	 * <b>PLEASE NOTE</b> : This does not give much of a performance boost as it
	 * uses the same methods internally - it does however skips a ressource
	 * check, which is the <i>only</i> reason why this method is not deprecated.
	 * 
	 * @param key
	 *            The key to search the resource lang files for.
	 * @param replacements
	 *            Replacements values for String formatting (change variables in
	 *            the Strings).
	 * @return The language String found in the resources with the variables
	 *         replaced, or <code>null</code> if there is absolutely no string
	 *         found in the resources.
	 * @see {@link #langDirect(String key)}
	 */
	public String langDirect(String key, Object... replacements){
		return getDictionary().getDirectString(key, replacements);
	}
	
	/**
	 * Legacy method to directly get resources with the key supplied without
	 * testing for class possibility.
	 * <p>
	 * <b>PLEASE NOTE</b> : This does not give much of a performance boost as it
	 * uses the same methods internally - it does however skips a ressource
	 * check, which is the <i>only</i> reason why this method is not deprecated.
	 * 
	 * @param key
	 *            The key to search the resource lang files for.
	 * @return The language String found in the resources, or <code>null</code>
	 *         if there is absolutely no string found in the resources.
	 * @see {@link #langDirect(String key, Object... replacements)}
	 */
	public String langDirect(String key){
		return getDictionary().getDirectString(key);
	}
	
	public String lang(String key){
		return getDictionary().getString(key, getClass().getSimpleName());
	}
	
	public String lang(String key, Object... replacements){
		return getDictionary().getString(key, getClass().getSimpleName(),
				replacements);
	}
	
}
