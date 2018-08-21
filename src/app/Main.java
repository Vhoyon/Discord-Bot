package app;

import consoles.TerminalConsole;
import consoles.UIConsole;
import vendor.Framework;
import vendor.interfaces.Console;
import vendor.modules.Audit;
import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.objects.AuditableFile;
import vendor.objects.Request;
import vendor.utilities.FrameworkTemplate;

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
			
			if(programRequest.hasError()){
				System.out.println(programRequest.getDefaultErrorMessage());
			}
			
			Framework.build(programRequest.hasParameter("d"));
			
			Audit.setOutputs(new AuditableFile("audit.txt", Framework
					.runnableSystemPath()));
			
			FrameworkTemplate.botToken = Environment.getVar("BOT_TOKEN");
			
			Console console;
			
			if(programRequest.hasParameter("t", "terminal")){
				
				console = new TerminalConsole(){
					@Override
					public void onStart() throws Exception{
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
			
			// CAREFUL : This call blocks the main thread!
			console.initialize();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Logs a link to the linked loggers that makes this bot join a server of choice with the condition that the {@code CLIENT_ID} environment variable is not empty.
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
