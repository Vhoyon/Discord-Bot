package app;

import consoles.TerminalConsole;
import consoles.UIConsole;
import vendor.utilities.FrameworkTemplate;
import vendor.Framework;
import vendor.interfaces.Console;
import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.objects.Dictionary;
import vendor.objects.Request;

/**
 * 
 * @author Stephano
 *
 */
public class Main {
	
	public static void main(String[] args){
		
		try{
			
			String requestableArgs = "RUN_PROGRAM " + convertArgsToString(args);
			
			Request programRequest = new Request(requestableArgs,
					new Dictionary());
			
			if(programRequest.hasError()){
				System.out.println(programRequest.getError());
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
