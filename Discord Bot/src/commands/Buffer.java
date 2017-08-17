package commands;

import java.util.ArrayList;

public class Buffer {
	
	private ArrayList<Object> buffer = new ArrayList<>();
	private ArrayList<String> associatedNames = new ArrayList<>();
	
	public Buffer(){}
	
	public boolean push(Object object, String associatedName){
		
		boolean isNewObject = true;
		
		int index = -1;
		
		if(associatedName != null)
			index = associatedNames.lastIndexOf(associatedName);
		
		if(index == -1){
			
			buffer.add(object);
			associatedNames.add(associatedName);
			
		}
		else{
			
			buffer.set(index, object);
			
			isNewObject = false;
			
		}
		
		return isNewObject;
		
	}
	
	public boolean push(Object object){
		
		return push(object, null);
		
	}
	
	public Object get(int index){
		
		return buffer.get(index);
		
	}
	
	public Object get(String associatedName){
		
		return buffer.get(associatedNames.lastIndexOf(associatedName));
		
	}
	
	public boolean remove(int index){
		
		boolean success = true;
		
		try{
			
			buffer.remove(index);
			associatedNames.remove(index);
			
		}
		catch(Exception e){
			success = false;
		}
		
		return success;
		
	}
	
	public boolean remove(String associatedName){
		
		boolean success = true;
		
		try{
			
			int index = associatedNames.lastIndexOf(associatedName);
			
			buffer.remove(index);
			associatedNames.remove(index);
			
		}
		catch(Exception e){
			success = false;
		}
		
		return success;
		
	}
	
}
