package utilities;

import app.CommandRouter;
import utilities.interfaces.*;
import vendor.abstracts.AbstractBotCommand;
import vendor.exceptions.BadFormatException;
import vendor.utilities.settings.Setting;

public abstract class BotCommand extends AbstractBotCommand implements
		Commands, Resources {
	
	public BotCommand(){
		super();
	}
	
	public BotCommand(BotCommand botCommandToCopy){
		super(botCommandToCopy);
	}
	
	@Override
	public CommandRouter getRouter(){
		return (CommandRouter)super.getRouter();
	}
	
	@Override
	public String formatParameter(String parameterToFormat){
		return buildVParameter(parameterToFormat);
	}
	
	public String getUsage(){
		return buildVCommand(getCommandName());
	}
	
	public <SettingValue> SettingValue setting(String settingName)
			throws BadFormatException{
		
		Setting settings = (Setting)getMemory(BUFFER_SETTINGS);
		
		Object value = settings.getField(settingName).getValue();
		
		return (SettingValue)value;
		
	}
	
	public void setSetting(String settingName, Object value){
		
		Setting settings = (Setting)getMemory(BUFFER_SETTINGS);
		
		settings.save(settingName, value, this);
		
	}
	
}
