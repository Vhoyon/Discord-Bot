package utilities;

import java.util.ArrayList;

public class Buffer {
	
	private class BufferObject {
		
		private Object object;
		private String associatedName;
		private String guildID;
		
		public BufferObject(String associatedName, String guildId){
			this(null, associatedName, guildId);
		}
		
		public BufferObject(Object object, String associatedName, String guildId){
			this.object = object;
			this.associatedName = associatedName;
			this.guildID = guildId;
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
			
			if(this.getGuildID() != null && obj instanceof BufferObject){
				
				BufferObject bufrObj = (BufferObject)obj;
				
				if(getGuildID().equals(bufrObj.getGuildID())){
					
					if(getName() != null)
						isEqual = getName().equals(bufrObj.getName());
					else
						isEqual = getObject().equals(bufrObj.getObject());
					
				}
				
			}
			
			return isEqual;
			
		}
		
	}
	
	private static Buffer buffer;
	
	private ArrayList<BufferObject> memory;
	
	private Buffer(){
		memory = new ArrayList<>();
	}
	
	public static Buffer get(){
		if(buffer == null)
			buffer = new Buffer();
		
		return buffer;
	}
	
	public boolean push(Object object, String associatedName, String guildId){
		
		boolean isNewObject = true;
		
		int index = -1;
		
		if(associatedName != null)
			index = memory.indexOf(new BufferObject(associatedName, guildId));
		
		if(index == -1){
			
			memory.add(new BufferObject(object, associatedName, guildId));
			
		}
		else{
			
			BufferObject objectFound = memory.get(index);
			
			objectFound.setObject(object);
			
			isNewObject = false;
			
		}
		
		return isNewObject;
		
	}
	
	public Object get(int index) throws IndexOutOfBoundsException{
		return memory.get(index).getObject();
	}
	
	public Object get(String associatedName, String guildId)
			throws NullPointerException{
		
		try{
			return get(memory
					.indexOf(new BufferObject(associatedName, guildId)));
		}
		
		catch(ArrayIndexOutOfBoundsException e){
			throw new NullPointerException(
					"No object with the associated name \"" + associatedName
							+ "\" found in the buffer for the Guild ID \""
							+ guildId + "\".");
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
	
	public boolean remove(String associatedName, String guildId){
		return remove(memory.indexOf(new BufferObject(associatedName, guildId)));
	}
	
}
