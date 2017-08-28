package framework;

import java.util.ArrayList;

public class Buffer {
	
	private class BufferObject {
		
		private Object object;
		private String associatedName;
		private String guildID;
		
		public BufferObject(String associatedName){
			this(null, associatedName);
		}
		
		public BufferObject(Object object, String associatedName){
			this.object = object;
			this.associatedName = associatedName;
			this.guildID = currentGuildID;
		}
		
		public Object getObject(){
			return object;
		}
		
		public void setObject(Object object){
			this.object = object;
		}
		
		public String getName(){
			return this.associatedName;
		}
		
		public String getGuildID(){
			return guildID;
		}
		
		@Override
		public boolean equals(Object obj){
			
			boolean isEqual = false;
			
			if(obj instanceof BufferObject){
				
				BufferObject bufrObj = (BufferObject)obj;
				
				if(currentGuildID.equals(bufrObj.getGuildID())){
					
					if(getName() != null)
						isEqual = getName().equals(bufrObj.getName());
					else
						isEqual = getObject().equals(bufrObj.getObject());
					
				}
				
			}
			
			return isEqual;
			
		}
		
	}
	
	private ArrayList<BufferObject> memory;
	private String currentGuildID;
	
	public Buffer(){
		memory = new ArrayList<>();
	}
	
	public void setGuildID(String guildID){
		this.currentGuildID = guildID;
	}
	
	public boolean push(Object object, String associatedName){
		
		boolean isNewObject = true;
		
		int index = -1;
		
		if(associatedName != null)
			index = memory.indexOf(new BufferObject(associatedName));
		
		if(index == -1){
			
			memory.add(new BufferObject(object, associatedName));
			
		}
		else{
			
			BufferObject objectFound = memory.get(index);
			
			objectFound.setObject(object);
			
			isNewObject = false;
			
		}
		
		return isNewObject;
		
	}
	
	public boolean push(Object object){
		
		return push(object, null);
		
	}
	
	public Object get(int index) throws IndexOutOfBoundsException{
		
		return memory.get(index).getObject();
		
	}
	
	public Object get(String associatedName) throws IndexOutOfBoundsException{
		
		return get(memory.indexOf(new BufferObject(associatedName)));
		
	}
	
	public boolean remove(int index) throws IndexOutOfBoundsException{
		
		boolean success = true;
		
		try{
			
			memory.remove(index);
			
		}
		catch(Exception e){
			success = false;
		}
		
		return success;
		
	}
	
	public boolean remove(String associatedName) throws IndexOutOfBoundsException{
		
		return remove(memory.indexOf(new BufferObject(associatedName)));
		
	}
	
}
