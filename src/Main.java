import javax.security.auth.login.LoginException;

import ressources.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * 
 * @author Stephano
 *
 */
public class Main {
	
	private static JDA jda;
	
	public static void main(String[] args){
		
		try{
			
			jda = new JDABuilder(AccountType.BOT).setToken(
					Ressources.DISCORDTOKEN).buildBlocking();
			jda.addEventListener(new MessageListener());
			jda.setAutoReconnect(true);
			
		}
		catch(LoginException | IllegalArgumentException | InterruptedException
				| RateLimitedException e){
			e.printStackTrace();
		}
		
	}
	
}
