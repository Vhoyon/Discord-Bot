package io.github.vhoyon.bot.commands;

import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.bot.utilities.specifics.CommandConfirmed;
import io.github.vhoyon.vramework.utilities.CommandsThreadManager;

/**
 * Command that stops a running command.
 * <p>
 * There is two mode to this command :
 * <ol>
 * <li>Calling the command without any content, which finds the latest running
 * command and confirms with the user that he wants to stop the found command;</li>
 * <li>Giving the name of the command the users wants to stop as content to the
 * command request to directly (<i>so <b>no</b> confirmation here</i>) stop the
 * command requested if it is running.</li>
 * </ol>
 * </p>
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandStop extends BotCommand {
	
	@Override
	public void actions(){
		
		if(!hasContent()){
			
			BotCommand commandToStop = (BotCommand)CommandsThreadManager
					.getLatestRunningCommandExcept(this, getKey());
			
			if(commandToStop == null){
				sendMessage(lang("BotCantStop"));
			}
			else{
				
				boolean isConfirming = setting("confirm_stop");
				
				if(!isConfirming){
					stopCommandLogic(commandToStop);
				}
				else{
					new CommandConfirmed(this){
						@Override
						public String getConfMessage(){
							return lang("ConfirmStopCommand",
									buildVCommand(commandToStop
											.getCommandName()));
						}
						
						@Override
						public void confirmed(){
							stopCommandLogic(commandToStop);
						}
					};
				}
				
			}
			
		}
		else{
			
			BotCommand commandToStop = (BotCommand)CommandsThreadManager
					.getCommandRunning(getContent(), getEventDigger(),
							getRouter());
			
			if(commandToStop == null){
				new BotError(this, lang("NoCommandToStopMessage",
						code(getContent())));
			}
			else{
				stopCommandLogic(commandToStop);
			}
			
		}
		
	}
	
	/**
	 * Applies the logic to stop a command.
	 * 
	 * @param commandToStop
	 *            The command to stop.
	 * @since v0.6.0
	 */
	private void stopCommandLogic(BotCommand commandToStop){
		
		if(commandToStop.kill())
			sendInfoMessage(lang("CommandFullyStoppedMessage",
					code(commandToStop.getCommandName()), EMOJI_OK_HAND));
		else{
			new BotError(this, lang("CommandNotStoppedMessage",
					code(commandToStop.getCommandName())), true);
		}
		
	}
	
	@Override
	public String getCall(){
		return STOP;
	}
	
	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
	
}
