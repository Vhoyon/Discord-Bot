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
		
		boolean isNewObject = memory.containsKey(objectKey);
		
		memory.put(objectKey, object);
		
		return isNewObject;
		
	}
	
	public Object get(String associatedName, String key)
			throws NullPointerException{
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		boolean hasObject = memory.containsKey(objectKey);
		
		if(!hasObject){
			throw new NullPointerException(
					"No object with the associated name \"" + associatedName
							+ "\" found in the buffer for the Command ID \""
							+ key + "\".");
		}
		else{
			
			Object memoryObject = memory.get(objectKey);
			
			return memoryObject;
			
		}
		
	}
	
	public boolean remove(String associatedName, String key){
		
		String objectKey = Utils.buildKey(key, associatedName);
		
		boolean hasRemovedObject = memory.containsKey(objectKey);

		memory.remove(objectKey);
		
		return hasRemovedObject;
		
	}
	
	public void emptyMemory(){
		memory = new HashMap<>();
	}
	
}
