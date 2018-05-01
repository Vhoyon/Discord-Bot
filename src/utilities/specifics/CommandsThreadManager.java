package utilities.specifics;

import java.util.Set;
import java.util.Stack;

import utilities.BotCommand;
import app.CommandRouter;
import vendor.interfaces.Utils;

public class CommandsThreadManager {
	
	private CommandsThreadManager(){}
	
	/**
	 * Method that determines whether a command is running by scanning all the
	 * threads used in the server of the <code>commandID</code> parameter,
	 * looking for the desired <code>command</code> parameter.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param eventDigger
	 *            The server's <code>commandID</code> required to search for
	 *            commands running in said server's text channel.
	 * @return The command found with all of it's attribute in a
	 *         <code>Command</code> object, <code>null</code> if the command
	 *         wasn't found.
	 */
	public static BotCommand getCommandRunning(String commandName,
			MessageEventDigger eventDigger, CommandRouter inRouter){
		
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		for(CommandRouter router : routers)
			if(!router.equals(inRouter)
					&& router.getName().equals(
							eventDigger.getCommandKey(commandName)))
				return router.getCommand();
		
		return null;
		
	}
	
	public static BotCommand getLatestRunningCommand(){
		
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		if(routers.empty()){
			return null;
		}
		else{
			
			CommandRouter latestRouter = routers.pop();
			
			return latestRouter.getCommand();
			
		}
		
	}
	
	public static BotCommand getLatestRunningCommand(String guildID){
		
		Stack<CommandRouter> guildRouters = getRunningCommandRouters(guildID);
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			CommandRouter latestRouter = guildRouters.pop();
			
			return latestRouter.getCommand();
			
		}
		
	}
	
	public static BotCommand getLatestRunningCommandExcept(
			BotCommand commandToIgnore){
		
		Stack<CommandRouter> guildRouters = getRunningCommandRouters();
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			CommandRouter latestRouter = guildRouters.pop();
			
			if(latestRouter.getCommand().equals(commandToIgnore)){
				if(guildRouters.empty()){
					return null;
				}
				else{
					latestRouter = guildRouters.pop();
				}
			}
			
			return latestRouter.getCommand();
			
		}
		
	}
	
	public static BotCommand getLatestRunningCommandExcept(
			BotCommand commandToIgnore, String commandID){
		
		Stack<CommandRouter> guildRouters = getRunningCommandRouters(commandID);
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			CommandRouter latestRouter = guildRouters.pop();
			
			if(latestRouter.getCommand().equals(commandToIgnore)){
				if(guildRouters.empty()){
					return null;
				}
				else{
					latestRouter = guildRouters.pop();
				}
			}
			
			return latestRouter.getCommand();
			
		}
		
	}
	
	/**
	 * Method that quickly tells if a command is running based off its name in
	 * the guild provided in parameters.
	 * <p>
	 * Internally, this uses the method <code>getCommandRunning()</code> and
	 * tests if that returns <code>null</code> or not.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param eventDigger
	 *            The server's <code>commandID</code> required to search for
	 *            commands running in said server.
	 * @return <code>true</code> if the command is running in the specified
	 *         command id, <code>false</code> otherwise.
	 */
	public static boolean isCommandRunning(String commandName,
			MessageEventDigger eventDigger, CommandRouter router){
		return getCommandRunning(commandName, eventDigger, router) != null;
	}
	
	public static int stopAllCommands(){
		
		int numberOfCommandsStopped = 0;
		
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		for(CommandRouter router : routers)
			if(router.getCommand().stopAction())
				numberOfCommandsStopped++;
		
		return numberOfCommandsStopped;
		
	}
	
	public static boolean hasRunningCommands(){
		return !getRunningCommandRouters().empty();
	}
	
	private static Stack<CommandRouter> getRunningCommandRouters(){
		Thread[] threads = getThreadsArray();
		
		Stack<CommandRouter> routers = new Stack<>();
		
		for(Thread thread : threads)
			if(thread instanceof CommandRouter)
				routers.add((CommandRouter)thread);
		
		return routers;
	}
	
	private static Stack<CommandRouter> getRunningCommandRouters(
			String commandID){
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		Stack<CommandRouter> guildRouters = new Stack<>();
		
		for(CommandRouter router : routers)
			if(router.getName().contains(commandID))
				guildRouters.push(router);
		
		return guildRouters;
	}
	
	private static Thread[] getThreadsArray(){
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		return threadSet.toArray(new Thread[threadSet.size()]);
	}
	
}
