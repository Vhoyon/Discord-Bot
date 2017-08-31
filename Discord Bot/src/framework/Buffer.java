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
			this.guildID = latestGuildID;
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
				
				if(latestGuildID.equals(bufrObj.getGuildID())){
					
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
	private String latestGuildID;
	
	public Buffer(){
		memory = new ArrayList<>();
	}
	
	public void setLatestGuildID(String guildID){
		this.latestGuildID = guildID;
	}
	
	public String getLatestGuildID(){
		return latestGuildID;
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
	
	public Object get(String associatedName) throws NullPointerException{
		
		try{
			return get(memory.indexOf(new BufferObject(associatedName)));
		}
		
		catch(ArrayIndexOutOfBoundsException e){
			throw new NullPointerException(
					"No object with the associated name \"" + associatedName
							+ "\" found in the buffer.");
		}
		
	}
	
	public boolean remove(int index){
		
		boolean success = true;
		
		try{
			memory.remove(index);
		}
		catch(IndexOutOfBoundsException e){
			success = false;
		}
		
		return success;
		
	}
	
	public boolean remove(String associatedName){
		
		return remove(memory.indexOf(new BufferObject(associatedName)));
		
	}
	
}
