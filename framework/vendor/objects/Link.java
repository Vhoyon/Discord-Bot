package vendor.objects;

import vendor.interfaces.LinkableCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	private String[] calls;
	
	public Link(Class<? extends LinkableCommand> command){
		this.classToLink = command;
		
		try{
			LinkableCommand commandInstance = initiate();
			
			this.calls = commandInstance.getCalls();
		}
		catch(Exception e){}
	}
	
	public LinkableCommand initiate() throws Exception{
		return getClassToLink().newInstance();
	}
	
	public boolean hasCall(String call){
		
		if(call != null && call.length() != 0){
			
			String[] definedCalls = getCalls();
			
			for(String definedCall : definedCalls)
				if(definedCall.equals(call))
					return true;
			
		}
		
		return false;
		
	}
	
	public String[] getCalls(){
		return this.calls;
	}
	
	public Class<? extends LinkableCommand> getClassToLink(){
		return classToLink;
	}
	
}
