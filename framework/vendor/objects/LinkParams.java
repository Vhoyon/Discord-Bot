package vendor.objects;

import java.util.ArrayList;

import vendor.interfaces.LinkableCommand;

public class LinkParams extends Link {
	
	private Object[] params;
	
	public LinkParams(Link link, Object... params){
		super(link.getClassToLink(), link.getCalls());
		
		this.params = params;
	}
	
	@Override
	public LinkableCommand initiate() throws Exception{
		
		@SuppressWarnings("rawtypes")
		ArrayList<Class> classes = new ArrayList<>();
		
		for(Object object : params){
			classes.add(object.getClass());
		}
		
		return getClassToLink().getDeclaredConstructor(
				(Class[])classes.toArray(new Class[classes.size()]))
				.newInstance(params);
		
	}
	
}
