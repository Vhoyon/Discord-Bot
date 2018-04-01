package app;

import javax.security.auth.login.LoginException;

import consoles.TerminalConsole;
import consoles.UIConsole;
import utilities.Buffer;
import utilities.specifics.CommandsThreadManager;
import vendor.Framework;
import vendor.interfaces.Console;
import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;
import vendor.objects.Dictionary;
import vendor.objects.Request;
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
	
	private static String botToken;
	
	public static void main(String[] args){
		
		try{
			
			String requestableArgs = "RUN_PROGRAM " + convertArgsToString(args);
			
			Request programRequest = new Request(requestableArgs,
					new Dictionary());
			
			if(programRequest.hasError()){
				System.out.println(programRequest.getError());
			}
			
			Framework.build();
			
			botToken = Environment.getVar("BOT_TOKEN");
			
			Console console;
			
			if(programRequest.hasParameter("t", "terminal")){
				
				console = new TerminalConsole(){
					@Override
					public void onStart() throws Exception{
						startBot(this);
					}
					
					@Override
					public void onStop() throws Exception{
						stopBot(this);
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
						startBot(this);
					}
					
					@Override
					public void onStop() throws Exception{
						stopBot(this);
					}
					
					@Override
					public void onInitialized(){
						logLinkIfDebug();
					}
				};
				
			}
			
			Logger.setSeparator(null);
			
			// CAREFUL : This call blocks the main thread!
			console.initialize();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static void startBot(Console console) throws Exception{
		
		boolean success = false;
		
		do{
			
			Logger.log("Starting the bot...", LogType.INFO);
			
			try{
				
				jda = new JDABuilder(AccountType.BOT).setToken(botToken)
						.buildBlocking();
				jda.addEventListener(new MessageListener());
				jda.setAutoReconnect(true);
				
				Logger.log("Bot started!", LogType.INFO);
				
				success = true;
				
			}
			catch(LoginException e){
				
				botToken = console
						.getInput("The bot token provided is invalid. Please enter a valid token here :");
				
				if(botToken == null || botToken.length() == 0)
					throw e;
				
				Logger.log("Application's Bot Token has been set to : "
						+ botToken, LogType.INFO);
				
			}
			
		}while(!success);
		
	}
	
	private static void stopBot(Console console) throws Exception{
		
		if(jda != null){
			
			Logger.log("Shutting down the bot...", LogType.INFO);
			
			boolean canStopBot = true;
			
			if(CommandsThreadManager.hasRunningCommands()){
				
				int conf = console
						.getConfirmation(
								"There are running commands, are you sure you want to stop the bot?",
								Console.QuestionType.YES_NO);
				
				if(conf == Console.NO){
					canStopBot = false;
				}
				else{

					int numberOfStoppedCommands = CommandsThreadManager
							.stopAllCommands();
					
					Logger.log("Stopped " + numberOfStoppedCommands
							+ " running commands before stopping the bot.",
							LogType.INFO);
					
				}
				
			}
			
			if(!canStopBot){
				
				throw new Exception("Bot not stopped.");
				
			}
			else{
				
				jda.shutdownNow();
				
				jda = null;

				Buffer.get().emptyMemory();

				Logger.log("Bot has been shutdown!", LogType.INFO);
				
			}
			
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
						+ "https://discordapp.com/oauth2/authorize?client_id="
						+ clientId + "&scope=bot&permissions=0", false);
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
