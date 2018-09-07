package utilities;

import app.CommandRouter;
import utilities.interfaces.*;
import vendor.abstracts.AbstractBotCommand;
import vendor.exceptions.BadFormatException;
import vendor.utilities.settings.Setting;

import java.util.function.Consumer;

/**
 * Vhoyon's custom implementation of the
 * {@link vendor.abstracts.AbstractBotCommand AbstractBotCommand} to format
 * parameters how we want them and add few utilities such as Settings handling.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class BotCommand extends AbstractBotCommand implements
		Commands, Resources {
	
	/**
	 * @see vendor.abstracts.AbstractBotCommand#AbstractBotCommand()
	 *      AbstractBotCommand()
	 */
	public BotCommand(){
		super();
	}
	
	/**
	 * @see vendor.abstracts.AbstractBotCommand#AbstractBotCommand(AbstractBotCommand)
	 *      AbstractBotCommand(AbstractBotCommand commandToCopy)
	 */
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
	
	/**
	 * Gets the formatted usage for this command.
	 *
	 * @return A formatted String that uses {@link #buildVCommand(String)} and
	 *         {@link #getCommandName()}.
	 * @version 1.0
	 * @since v0.6.0
	 */
	public String getUsage(){
		return buildVCommand(getCommandName());
	}
	
	/**
	 * Gets the {@link vendor.utilities.settings.Setting Setting} object from
	 * this command's router.
	 *
	 * @version 1.0
	 * @since v0.8.0
	 * @see app.CommandRouter#getSettings()
	 */
	public Setting getSettings(){
		return getRouter().getSettings();
	}
	
	/**
	 * Gets the value of the {@link vendor.utilities.settings.SettingField
	 * SettingField} associated to the name of the parameter {@code settingName}
	 * .
	 *
	 * @version 1.0
	 * @since v0.8.0
	 */
	public <SettingValue> SettingValue setting(String settingName){
		
		Setting settings = this.getSettings();
		
		Object value = settings.getField(settingName).getValue();
		
		return (SettingValue)value;
		
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value}.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link vendor.utilities.settings.SettingField SettingField}
	 *             associated with the {@code name} provided.
	 * @version 1.0
	 * @since v0.8.0
	 * @see #setSetting(String, Object, Consumer)
	 */
	public void setSetting(String settingName, Object value)
			throws BadFormatException{
		this.setSetting(settingName, value, null);
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value} and
	 * runs arbitrary code after a successfull change.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @param onChange
	 *            Arbitrary code to run when the setting has been changed using
	 *            a {@link java.util.function.Consumer} and the
	 *            {@link java.util.function.Consumer#accept(Object)} method, in
	 *            which the validated value is sent to. Can be {@code null} (or
	 *            use {@link #setSetting(String, Object)}) to not run anything
	 *            on change success.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link vendor.utilities.settings.SettingField SettingField}
	 *             associated with the {@code name} provided.
	 * @version 1.0
	 * @since v0.8.0
	 */
	public void setSetting(String settingName, Object value,
			Consumer<Object> onChange) throws BadFormatException{
		
		Setting settings = this.getSettings();
		
		settings.save(settingName, value, onChange);
		
	}
	
}
