package io.github.vhoyon.bot.app;

import io.github.vhoyon.bot.consoles.TerminalConsole;
import io.github.vhoyon.bot.consoles.UIConsole;
import io.github.vhoyon.vramework.Framework;
import io.github.vhoyon.vramework.interfaces.Console;
import io.github.vhoyon.vramework.modules.Audit;
import io.github.vhoyon.vramework.modules.Environment;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.objects.AuditableFile;
import io.github.vhoyon.vramework.objects.Request;
import io.github.vhoyon.vramework.utilities.FrameworkTemplate;

/**
 * The main class for running Vhoyon's bot.
 * 
 * @version 1.0
 * @since 0.1.0
 * @author Stephano Mehawej
 */
public class Main {
	
	public static void main(String[] args){
		
		try{
			
			Request programRequest = new Request(args);
			
			programRequest.setParamLink("d", "debug");
			programRequest.setParamLink("i", "instant");
			programRequest.setParamLink("t", "terminal");
			
			if(programRequest.hasError()){
				System.out.println(programRequest.getDefaultErrorMessage());
			}
			
			Framework.build(Main.class, programRequest.hasParameter("d"));
			
			Audit.setOutputs(new AuditableFile("audit.txt", Framework
					.runnableSystemPath()));
			
			FrameworkTemplate.botToken = Environment.getVar("BOT_TOKEN");
			
			Console console;
			
			if(programRequest.hasParameter("t")){
				
				console = new TerminalConsole(){
					@Override
					public void onStart() throws Exception{
						Environment.refresh();
						
						FrameworkTemplate.startBot(this, new MessageListener());
					}
					
					@Override
					public void onStop() throws Exception{
						FrameworkTemplate.stopBot(this);
					}
					
					@Override
					public void onInitialized(){
						logLink();
					}
				};
				
			}
			else{
				
				console = new UIConsole(){
					@Override
					public void onStart() throws Exception{
						Environment.refresh();
						
						FrameworkTemplate.startBot(this, new MessageListener());
					}
					
					@Override
					public void onStop() throws Exception{
						FrameworkTemplate.stopBot(this);
					}
					
					@Override
					public void onInitialized(){
						logLink();
					}
				};
				
			}
			
			Logger.setSeparator(null);
			
			boolean startImmediately = programRequest.hasParameter("i");
			
			// CAREFUL : This call blocks the main thread!
			console.initialize(startImmediately);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Logs a link to the linked loggers that makes this bot join a server of
	 * choice with the condition that the {@code CLIENT_ID} environment variable
	 * is not empty.
	 *
	 * @since v0.4.0
	 */
	private static void logLink(){
		
		String clientId = Environment.getVar("CLIENT_ID", null);
		
		if(clientId != null){
			Logger.log("Link to join the bot to a server :\n\n"
					+ "https://discordapp.com/oauth2/authorize?client_id="
					+ clientId + "&scope=bot&permissions=0", false);
		}
		
	}
	
}
