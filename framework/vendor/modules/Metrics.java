package vendor.modules;

import vendor.abstracts.Module;

import java.util.Date;

public class Metrics extends Module {
	
	private static Date clock;
	
	@Override
	public void build() throws Exception{
		clock = null;
	}
	
	public static void startClock(){
		clock = new Date();
	}
	
	public static void stopClock(){
		clock = null;
	}
	
	public static long getUptime(){
		if(clock == null){
			return 0;
		}
		
		Date now = new Date();
		
		return now.getTime() - clock.getTime();
	}
	
}
