import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main{
	
	private static JDA jda;

	public static void main(String[] args) {
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(Ressources.TOKEN).buildBlocking();
			jda.addEventListener(new MessageListener());
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

}
