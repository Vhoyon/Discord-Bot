package utilities;

import app.CommandRouter;
import utilities.interfaces.*;
import vendor.abstracts.AbstractBotCommand;

public abstract class BotCommand extends AbstractBotCommand implements
		Commands, Resources {

	@Override
	public CommandRouter getRouter() {
		return (CommandRouter)super.getRouter();
	}

	@Override
	public String formatParameter(String parameterToFormat){
		return buildVParameter(parameterToFormat);
	}
	
	public String getUsage(){
		return buildVCommand(getCommandName());
	}
	
}
