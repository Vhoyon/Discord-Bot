package app;

import javax.security.auth.login.LoginException;

import consoles.TerminalConsole;
import consoles.UIConsole;
import utilities.Dictionary;
import utilities.specifics.Request;
import vendor.Framework;
import vendor.interfaces.Console;
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
			
			String requestableArgs = "RUN_PROGRAM " + convertArgsToString(args);
			
			Request programRequest = new Request(requestableArgs,
					new Dictionary(), "-");
			
			if(programRequest.hasErrors()){
				System.out.println(programRequest.getError().getMessage());
			}
			
			Framework.build();
			
			String botToken = Environment.getVar("BOT_TOKEN");
			
			Console console = null;
			
			if(programRequest.hasParameter("t", "terminal")){
				
				console = new TerminalConsole(){
					@Override
					public void onStart() throws Exception{
						startBot(botToken);
					}
					
					@Override
					public void onStop() throws Exception{
						stopBot();
					}
					
					@Override
					public void onInitialized(){
						logLinkIfDebug();
					}
				};
				
			}
			else{
				
				console = new UIConsole(){
					@Override
					public void onStart() throws Exception{
						startBot(botToken);
					}
					
					@Override
					public void onStop() throws Exception{
						stopBot();
					}
					
					@Override
					public void onInitialized(){
						logLinkIfDebug();
					}
				};
				
			}

			// CAREFUL : This call blocks the main thread!
			console.initialize();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static void startBot(String botToken) throws Exception{
		
		Logger.log("Starting the bot...", LogType.INFO);
		
		try{
			
			jda = new JDABuilder(AccountType.BOT).setToken(botToken)
					.buildBlocking();
			jda.addEventListener(new MessageListener());
			jda.setAutoReconnect(true);
			
			Logger.log("Bot started!", LogType.INFO);
			
		}
		catch(LoginException e){
			
			// TODO : Show a new window that asks for a good bot token
			
			throw e;
			
		}
		
	}
	
	private static void stopBot() throws Exception{
		
		if(jda != null){
			
			Logger.log("Shutting down the bot...", LogType.INFO);
			
			jda.shutdownNow();
			
			jda = null;
			
			Logger.log("Bot has been shutdown!", LogType.INFO);
			
		}
		else{
			Logger.log("The JDA was already closed, no action was taken.",
					LogType.INFO);
		}
		
	}
	
	private static void logLinkIfDebug(){
		
		boolean isDebug = Environment.getVar("DEBUG");
		
		if(isDebug){
			String clientId = Environment.getVar("CLIENT_ID", null);
			
			if(clientId != null){
				Logger.log("Link to join the bot to a server :\n\n"
						+ "https://discordapp.com/oauth2/authorize?&client_id="
						+ clientId + "&scope=bot&permissions=0" + "\n", false);
			}
		}
		
	}
	
	private static String convertArgsToString(String[] args){
		StringBuilder builder = new StringBuilder();
		
		for(String arg : args){
			
			if(arg.startsWith("-")){
				builder.append(arg);
			}
			else{
				builder.append("\"").append(arg).append("\"");
			}
			
			builder.append(" ");
		}
		
		return builder.toString();
	}
	
}
