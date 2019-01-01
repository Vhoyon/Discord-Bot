package io.github.vhoyon.bot.utilities;

import io.github.vhoyon.bot.app.CommandRouter;
import io.github.vhoyon.bot.utilities.interfaces.Commands;
import io.github.vhoyon.bot.utilities.interfaces.Resources;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.utilities.settings.Setting;
import io.github.vhoyon.vramework.utilities.settings.SettingRepository;

import java.util.function.Consumer;

/**
 * Vhoyon's custom implementation of the
 * {@link io.github.vhoyon.vramework.abstracts.AbstractBotCommand
 * AbstractBotCommand} to format
 * parameters how we want them and add few utilities such as Settings handling.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class BotCommand extends AbstractBotCommand implements
		Commands, Resources {
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotCommand#AbstractBotCommand()
	 *      AbstractBotCommand()
	 */
	public BotCommand(){
		super();
	}
	
	/**
	 * @see io.github.vhoyon.vramework.abstracts.AbstractBotCommand#AbstractBotCommand(AbstractBotCommand)
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
	 * Gets the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 * object from this command's router.
	 *
	 * @return The {@link Setting} object of this
	 *         {@link io.github.vhoyon.bot.app.CommandRouter
	 *         Router}.
	 * @since v0.8.0
	 * @see io.github.vhoyon.bot.app.CommandRouter#getSettings()
	 */
	public SettingRepository getSettings(){
		return getRouter().getSettings();
	}
	
	/**
	 * Gets the {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 * object from this router.
	 * 
	 * @param settingName
	 *            The name of the setting to get from the SettingRepository of
	 *            this Router.
	 * @return The Setting object, generalized to Object to include all Fields
	 *         possible. The burden of casting to the right type goes to you.<br>
	 *         If you want to get the value and have it casted automatically to
	 *         your own return value, please see {@link #setting(String)}.
	 * @since v0.14.0
	 * @see #setting(String)
	 */
	public Setting<Object> getSetting(String settingName){
		return this.getSettings().getField(settingName);
	}
	
	/**
	 * Gets the value of the
	 * {@link io.github.vhoyon.vramework.utilities.settings.Setting} associated
	 * to the name of the parameter {@code settingName} .
	 *
	 * @param settingName
	 *            The name of the Setting to get the value from.
	 * @param <SettingValue>
	 *            The type of the value to be casted automatically to.
	 * @return The value of the Setting with the name {@code settingName}.
	 * @since v0.8.0
	 */
	public <SettingValue> SettingValue setting(String settingName){
		Object value = this.getSetting(settingName).getValue();
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
	 *             {@link io.github.vhoyon.vramework.utilities.settings.SettingField
	 *             SettingField} associated with the {@code name} provided.
	 * @since v0.8.0
	 * @see #setSetting(String, Object, Consumer)
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
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
	 *             {@link io.github.vhoyon.vramework.utilities.settings.SettingField
	 *             SettingField} associated with the {@code name} provided.
	 * @since v0.8.0
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 */
	public void setSetting(String settingName, Object value,
			Consumer<Object> onChange) throws BadFormatException{
		
		SettingRepository settings = this.getSettings();
		
		settings.save(settingName, value, onChange);
		
	}
	
}
