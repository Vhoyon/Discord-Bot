package app;

import consoles.TerminalConsole;
import consoles.UIConsole;
import vendor.utilities.FrameworkTemplate;
import vendor.Framework;
import vendor.interfaces.Console;
import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.objects.Request;

/**
 * 
 * @author Stephano
 *
 */
public class Main {
	
	public static void main(String[] args){
		
		try{
			
			Request programRequest = new Request(args);
			
			if(programRequest.hasError()){
				System.out.println(programRequest.getDefaultErrorMessage());
			}
			
			Framework.build(programRequest.hasParameter("d"));
			
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
	
	private static void logLink(){
		
		String clientId = Environment.getVar("CLIENT_ID", null);
		
		if(clientId != null){
			Logger.log("Link to join the bot to a server :\n\n"
					+ "https://discordapp.com/oauth2/authorize?client_id="
					+ clientId + "&scope=bot&permissions=0", false);
		}
		
	}
	
}
