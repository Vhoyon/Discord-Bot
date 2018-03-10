package abstracts;

public abstract class FrameworkModule {
	
	public abstract void build() throws Exception;
	
	public String getLoadingErrorMessage(Exception e){
		String moduleFailMessage = "The module " + this.getClass() + " couldn't load.";
		
		return moduleFailMessage + "\n\n" + e.getMessage();
	}
	
}
