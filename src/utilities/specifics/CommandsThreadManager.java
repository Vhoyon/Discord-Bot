package utilities.specifics;

import java.util.Set;
import java.util.Stack;

import utilities.Command;
import app.CommandRouter;

public class CommandsThreadManager {
	
	private CommandsThreadManager(){}
	
	/**
	 * Method that determines whether a command is running by scanning all the
	 * threads used in the server of the <code>guildID</code> parameter, looking
	 * for the desired <code>command</code> parameter.
	 * 
	 * @param commandName
	 *            The command name to search for.
	 * @param guildID
	 *            The server's <code>guildID</code> required to search for
	 *            commands running in said server.
	 * @return The command found with all of it's attribute in a
	 *         <code>Command</code> object, <code>null</code> if the command
	 *         wasn't found.
	 */
	public static Command getCommandRunning(String commandName, String guildID,
			CommandRouter inRouter){
		
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		for(CommandRouter router : routers)
			if(!router.equals(inRouter)
					&& router.getName().equals(commandName + guildID))
				return router.getCommand();
		
		return null;
		
	}
	
	public static Command getLatestRunningCommand(String guildID){
		
		Stack<CommandRouter> guildRouters = getRunningCommandRoutersOfGuild(guildID);
		
		if(guildRouters.empty()){
			return null;
		}
		else{
			
			CommandRouter latestRouter = guildRouters.pop();
			
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
	 * @param guildID
	 *            The server's <code>guildID</code> required to search for
	 *            commands running in said server.
	 * @return <code>true</code> if the command is running in the specified
	 *         guild id, <code>false</code> otherwise.
	 */
	public static boolean isCommandRunning(String commandName, String guildID,
			CommandRouter router){
		return getCommandRunning(commandName, guildID, router) != null;
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
		
		if(routers.size() == 0)
			routers = null;
		
		return routers;
		
	}
	
	private static Stack<CommandRouter> getRunningCommandRoutersOfGuild(
			String guildID){
		Stack<CommandRouter> routers = getRunningCommandRouters();
		
		Stack<CommandRouter> guildRouters = new Stack<>();
		
		for(CommandRouter router : routers)
			if(router.getName().matches("^.*\\Q" + guildID + "\\E$"))
				guildRouters.push(router);
		
		return guildRouters;
	}
	
	private static Thread[] getThreadsArray(){
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		return threadSet.toArray(new Thread[threadSet.size()]);
	}
	
}
