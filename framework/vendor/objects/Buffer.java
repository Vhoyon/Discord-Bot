package vendor.objects;

import vendor.interfaces.Utils;

import java.util.HashMap;

public class Buffer {
	
	private static Buffer buffer;
	
	private HashMap<String, Object> memory;
	
	private Buffer(){
		memory = new HashMap<>();
	}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	public boolean push(Object object, String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		boolean isNewObject = memory.put(objectKey, object) == null;
		
		return isNewObject;
		
	}
	
	public Object get(String associatedName, String key)
			throws NullPointerException{
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		Object memoryObject = memory.get(objectKey);
		
		if(memoryObject == null){
			throw new NullPointerException(
					"No object with the associated name \"" + associatedName
							+ "\" found in the buffer for the Command ID \""
							+ key + "\".");
		}
		else{
			return memoryObject;
		}
		
	}
	
	public boolean remove(String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		boolean hasRemovedObject = memory.remove(objectKey) != null;
		
		return hasRemovedObject;
		
	}
	
	public void emptyMemory(){
		memory = new HashMap<>();
	}
	
}
