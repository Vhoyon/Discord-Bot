package vendor.interfaces;

public interface Stoppable {
	
	default boolean stopMiddleware(){
		return true;
	}
	
}
