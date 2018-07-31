package utilities;

import app.CommandRouter;
import utilities.interfaces.*;
import vendor.abstracts.AbstractBotCommand;
import vendor.utilities.settings.Setting;

import java.util.function.Consumer;

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
	
	public Setting getSettings(){
		return getRouter().getSettings();
	}
	
	public <SettingValue> SettingValue setting(String settingName){
		
		Setting settings = this.getSettings();
		
		Object value = settings.getField(settingName).getValue();
		
		return (SettingValue)value;
		
	}
	
	public void setSetting(String settingName, Object value){
		this.setSetting(settingName, value, null);
	}
	
	public void setSetting(String settingName, Object value,
			Consumer<Object> onChange){
		
		Setting settings = this.getSettings();
		
		settings.save(settingName, value, onChange);
		
	}
	
}
