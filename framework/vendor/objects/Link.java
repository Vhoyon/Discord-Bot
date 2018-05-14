package vendor.objects;

import vendor.interfaces.LinkableCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	private LinkableCommand commandInstance;
	
	public Link(Class<? extends LinkableCommand> command){
		this.classToLink = command;
		
		try{
			getInstance();
		}
		catch(Exception e){}
	}
	
	public LinkableCommand getInstance() throws Exception{
		if(this.commandInstance == null)
			this.commandInstance = getClassToLink().newInstance();
		
		return this.commandInstance;
	}
	
	public boolean hasCall(String call){
		
		if(call != null && call.length() != 0){
			
			if(getCalls() instanceof String[]){
				
				String[] calls = (String[])getCalls();
				
				for(String definedCall : calls)
					if(definedCall.equals(call))
						return true;
				
			}
			else{
				
				String linkCall = getCalls().toString();
				
				return call.equals(linkCall);
				
			}
			
		}
		
		return false;
		
	}
	
	public Object getCalls(){
		try{
			return getInstance().getCalls();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public String getDefaultCall(){
		try{
			return getInstance().getDefaultCall();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public Class<? extends LinkableCommand> getClassToLink(){
		return this.classToLink;
	}
	
}
