package app;

import javax.security.auth.login.LoginException;

import ui.MainConsole;
import vendor.Framework;
import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;
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
			
			//			System.out.println(Arrays.toString(args));
			
			Framework.build();
			
			String botToken = Environment.getVar("BOT_TOKEN");
			
			boolean isDebug = Environment.getVar("DEBUG");
			
			new MainConsole(isDebug){
				@Override
				public void onStart() throws Exception{
					
					Logger.log("Starting the bot...", LogType.INFO);
					
					try{
						
						jda = new JDABuilder(AccountType.BOT)
								.setToken(botToken).buildBlocking();
						jda.addEventListener(new MessageListener());
						jda.setAutoReconnect(true);
						
						Logger.log("Bot started!", LogType.INFO);
						
					}
					catch(LoginException e){
						
						// TODO : Show a new window that asks for a good bot token
						
						throw e;
						
					}
					
				}
				
				@Override
				public void onStop() throws Exception{
					
					if(jda != null){
						
						Logger.log("Shutting down the bot...", LogType.INFO);
						
						jda.shutdownNow();
						
						jda = null;
						
						Logger.log("Bot has been shutdown!", LogType.INFO);
						
					}
					else{
						Logger.log(
								"The JDA was already closed, no action was taken.",
								LogType.INFO);
					}
					
				}
			};
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
