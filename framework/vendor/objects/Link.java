package vendor.objects;

import vendor.interfaces.LinkableCommand;

public class Link {
	
	private Class<? extends LinkableCommand> classToLink;
	private String[] calls;
	
	public Link(Class<? extends LinkableCommand> command, String... calls){
		this.classToLink = command;
		
		this.calls = calls;
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
