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
		return classToLink.newInstance();
	}
	
	public boolean hasCall(String call){
		for(int i = 0; i < getCalls().length; i++){
			
			if(getCalls()[i].equals(call))
				return true;
			
		}
		
		return false;
	}
	
	public String[] getCalls(){
		return this.calls;
	}
	
}
