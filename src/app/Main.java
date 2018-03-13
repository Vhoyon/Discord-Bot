package app;

import javax.security.auth.login.LoginException;

import ui.MainConsole;
import vendor.Framework;
import vendor.modules.Environment;
import vendor.modules.Logger;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 * 
 * @author Stephano
 *
 */
public class Main {
	
	private static JDA jda;
	
	public static void main(String[] args){
		
		try{
			
			Framework.build();
			
			String botToken = Environment.getVar("BOT_TOKEN");
			
			boolean isDebug = Environment.getVar("DEBUG");
			
			new MainConsole(isDebug){
				@Override
				public void onStart(){
					
					Logger.log("Starting the bot...");
					
					try{
						
						jda = new JDABuilder(AccountType.BOT)
								.setToken(botToken).buildBlocking();
						jda.addEventListener(new MessageListener());
						jda.setAutoReconnect(true);
						
						Logger.log("Bot started!");
						
					}
					catch(LoginException | IllegalArgumentException
							| InterruptedException e){
						Logger.log(e.getMessage());
					}
					
				}
				
				@Override
				public void onStop(){
					
					if(jda != null){
						
						Logger.log("Shutting down the bot...");
						
						jda.shutdownNow();
						
						jda = null;
						
						Logger.log("Bot has been shutdown!");
						
					}
					
				}
			};
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
