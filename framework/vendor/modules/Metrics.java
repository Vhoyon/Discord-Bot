package vendor.modules;

import net.dv8tion.jda.core.JDA;
import vendor.abstracts.Module;
import vendor.exceptions.JDANotSetException;

import java.util.Date;

public class Metrics extends Module {
	
	private static JDA jda;
	
	private static Date clock;
	
	@Override
	public void build() throws Exception{
		jda = null;
		clock = null;
	}
	
	public static void setJDA(JDA jda){
		Metrics.jda = jda;
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
	
	public static int getNumberOfJoinedServers() throws JDANotSetException{
		
		if(jda == null)
			throw new JDANotSetException();
		
		return jda.getGuilds().size();

	}
	
}
