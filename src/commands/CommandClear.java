package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;
import utilities.BotCommand;
import utilities.specifics.CommandConfirmed;
import vendor.objects.ParametersHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This command clears every messages that matches the request's conditions in
 * the TextChannel where it was called from.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano
 */
public class CommandClear extends BotCommand {
	
	@Override
	public void action(){
		
		new CommandConfirmed(this){
			
			@Override
			public String getConfMessage(){
				return lang("ConfirmMessage");
			}
			
			@Override
			public void confirmed(){
				
				try{
					
					deleteAllMessages();
					
					if(isAlive())
						sendMessage("Cleared everything!");
					
				}
				catch(PermissionException e){
					sendMessage(lang("NoPermission"));
				}
				
			}
			
		};
		
	}
	
	/**
	 * Deletes all the messages in the TextChannel where this command was called
	 * from.
	 * <p>
	 * This deletion is done by batch of 100 messages until it can't anymore
	 * (Discord doesn't allow do batch delete messages older than 2 weeks old,
	 * so we batch delete all we can and delete individually the rest).
	 * </p>
	 * 
	 * @throws PermissionException
	 *             Thrown if the bot does not have the sufficient permissions to
	 *             delete a message in the list.
	 * @since v0.10.0
	 * @see #deleteMessagesIf(Predicate)
	 */
	protected void deleteAllMessages() throws PermissionException{
		this.deleteMessagesIf(null);
	}
	
	/**
	 * Deletes all the messages in the TextChannel where this command was called
	 * from and applies the condition supplied by the predicate as a parameter
	 * (view {@code messageCondition}).
	 * <p>
	 * This deletion is done by batch of 100 messages until it can't anymore
	 * (Discord doesn't allow do batch delete messages older than 2 weeks old,
	 * so we batch delete all we can and delete individually the rest).
	 * </p>
	 * 
	 * @param messageCondition
	 *            This parameter is used to run arbitrary code to make a
	 *            condition on the message itself. You could use this to only
	 *            delete the messages of certain users, for example. Can be
	 *            {@code null} (or see {@link #deleteAllMessages()}) to not have
	 *            any condition and delete all messages.
	 * @throws PermissionException
	 *             Thrown if the bot does not have the sufficient permissions to
	 *             delete a message in the list.
	 * @since v0.10.0
	 * @see #deleteAllMessages()
	 */
	protected void deleteMessagesIf(Predicate<Message> messageCondition)
			throws PermissionException{
		
		MessageHistory messageHistory = getTextContext().getHistory();
		
		boolean shouldCompleteBeforeNext = hasParameter("s");
		
		final List<Message> fullMessageHistory = this.getFullMessageList(
				messageHistory, messageCondition);
		
		boolean deletingIndividually = messageCondition != null
				|| fullMessageHistory.size() < 2;
		
		final int batchSize = 100;
		
		for(int i = 0; i < fullMessageHistory.size() && isAlive(); i += batchSize){
			
			List<Message> currentMessageList = null;
			
			try{
				currentMessageList = fullMessageHistory.subList(i, i
						+ batchSize);
			}
			catch(IndexOutOfBoundsException e){
				currentMessageList = fullMessageHistory.subList(i,
						fullMessageHistory.size());
			}
			
			if(!deletingIndividually){
				
				try{
					
					if(shouldCompleteBeforeNext){
						getTextContext().deleteMessages(currentMessageList)
								.complete();
					}
					else{
						getTextContext().deleteMessages(currentMessageList)
								.queue();
					}
					
				}
				catch(IllegalArgumentException e){
					deletingIndividually = true;
				}
				
			}
			
			if(deletingIndividually){
				
				for(Message message : currentMessageList){
					
					if(shouldCompleteBeforeNext){
						message.delete().complete();
					}
					else{
						message.delete().queue();
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Gets the full message list from the {@code messageHistory} parameter.
	 * 
	 * @param messageHistory
	 *            The {@link MessageHistory} object used by this TextChannel to
	 *            reference which history should be searched for.
	 * @return A {@link List} of of all {@link Message} that is present in the
	 *         TextChannel where this command was invoked.
	 * @since v0.10.0
	 * @see #getFullMessageList(MessageHistory, Predicate)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory){
		return this.getFullMessageList(messageHistory, null);
	}
	
	/**
	 * Gets the full message list that matches the condition supplied by the
	 * {@code messageCondition} parameter out of the {@code messageHistory}
	 * parameter.
	 *
	 * @param messageHistory
	 *            The {@link MessageHistory} object used by this TextChannel to
	 *            reference which history should be searched for.
	 * @param messageCondition
	 *            This parameter is used to run arbitrary code to make a
	 *            condition on the message itself. You could use this to only
	 *            find the messages of certain users, for example. Can be
	 *            {@code null} (or see
	 *            {@link #getFullMessageList(MessageHistory)}) to not have any
	 *            condition and find all messages.
	 * @return A {@link List} of of all {@link Message} that is present in the
	 *         TextChannel where this command was invoked.
	 * @since v0.10.0
	 * @see #getFullMessageList(MessageHistory)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			Predicate<Message> messageCondition){
		
		boolean isEmpty;
		
		do{
			
			isEmpty = messageHistory.retrievePast(100).complete().isEmpty();
			
		}while(!isEmpty && isAlive());
		
		List<Message> fullHistory = messageHistory.getRetrievedHistory();
		
		if(messageCondition == null)
			return fullHistory;
		
		ArrayList<Message> messagesWithCondition = new ArrayList<>();
		
		fullHistory.forEach(message -> {
			
			if(messageCondition.test(message))
				messagesWithCondition.add(message);
			
		});
		
		return messagesWithCondition;
		
	}
	
	@Override
	public boolean stopAction(){
		return true;
	}
	
	@Override
	public Object getCalls(){
		return CLEAR;
	}
	
	@Override
	public String getCommandDescription(){
		return "Clear all the messages that in the text channel you execute the command!";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp(
					"Waits that the message has been successfully deleted before deleting the others. Useful if you are not sure if you should delete all the messages as you can stop the command.",
					false, "s", "slow")
		};
	}
	
}
