package vendor.interfaces;

public interface Console {
	
	public void onStart() throws Exception;
	
	public void onStop() throws Exception;
	
	public void onInitialized();
	
	public void initialize();
	
	public String getInput(String message);
	
}
