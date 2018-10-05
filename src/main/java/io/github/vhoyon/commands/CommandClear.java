package io.github.vhoyon.commands;

import io.github.vhoyon.errorHandling.BotError;
import io.github.vhoyon.utilities.BotCommand;
import io.github.vhoyon.utilities.specifics.CommandConfirmed;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;
import vendor.exceptions.BadContentException;
import vendor.interfaces.Stoppable;
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
public class CommandClear extends BotCommand implements Stoppable {
	
	protected ArrayList<Predicate<Message>> conditions;
	
	protected void addCondition(Predicate<Message> condition){
		
		if(this.conditions == null)
			this.conditions = new ArrayList<>();
		
		this.conditions.add(condition);
		
	}
	
	@Override
	public void action(){
		
		boolean shouldDoClear = true;
		
		String confirmationMessage = null;
		String notifyMessage = null;
		
		if(!hasParameter("u", "s", "b")){
			
			confirmationMessage = lang("ConfirmMessage");
			notifyMessage = "Cleared everything!";
			
		}
		else{
			
			try{
				
				final Member usr;
				
				if(hasParameter("u")){
					usr = getParameterAsMention("u");
				}
				else if(hasParameter("s")){
					usr = getMember();
				}
				else{
					usr = getBotMember();
				}
				
				confirmationMessage = lang("ConfUsrClear",
						code(usr.getEffectiveName()));
				notifyMessage = "Cleared " + usr.getAsMention()
						+ "'s messages!";
				
				addCondition(message -> usr.equals(message.getMember()));
				
			}
			catch(BadContentException e){
				sendMessage(lang("MentionError", code("@[username]")));
				
				shouldDoClear = false;
			}
			
		}
		
		if(shouldDoClear)
			doClearLogic(confirmationMessage, notifyMessage);
		
	}
	
	/**
	 * Executes the logic to clear the messages with the conditions present in
	 * the value of CommandClear's conditions array.
	 *
	 * @param confMessage
	 *            The message to send as confirmation if necessary
	 * @param notifyMessage
	 *            The message to send to notify that every message has been
	 *            deleted
	 * @since v0.10.0
	 */
	protected void doClearLogic(final String confMessage,
			final String notifyMessage){
		
		new CommandConfirmed(this){
			
			@Override
			public String getConfMessage(){
				return confMessage;
			}
			
			@Override
			public void confirmed(){
				
				try{
					
					deleteMessages(CommandClear.this.conditions);
					
					if(notifyMessage != null && isAlive() && hasParameter("n"))
						sendInfoMessage(notifyMessage);
					
				}
				catch(PermissionException e){
					new BotError(CommandClear.this, lang("NoPermission"));
				}
				
			}
			
		};
		
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
	 * @param messageConditions
	 *            This parameter is used to run arbitrary code to make
	 *            conditions on the message itself. You could use this to only
	 *            delete the messages of certain users, for example. Can be
	 *            {@code null} to not have any condition and delete all
	 *            messages.
	 * @throws PermissionException
	 *             Thrown if the bot does not have the sufficient permissions to
	 *             delete a message in the list.
	 * @since v0.10.0
	 */
	protected void deleteMessages(
			final ArrayList<Predicate<Message>> messageConditions)
			throws PermissionException{
		
		MessageHistory messageHistory = getTextContext().getHistory();
		
		boolean shouldCompleteBeforeNext = hasParameter("w");
		
		final List<Message> fullMessageHistory = this.getFullMessageList(
				messageHistory, messageConditions);
		
		boolean deletingIndividually = fullMessageHistory.size() < 2;
		
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
		return this.getFullMessageList(messageHistory,
				(ArrayList<Predicate<Message>>)null);
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
	 * @see #getFullMessageList(MessageHistory, ArrayList)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			Predicate<Message> messageCondition){
		
		ArrayList<Predicate<Message>> singleCondition = new ArrayList<>();
		singleCondition.add(messageCondition);
		
		return getFullMessageList(messageHistory, singleCondition);
		
	}
	
	/**
	 * Gets the full message list that matches the conditions supplied by the
	 * {@code messageCondition} parameter out of the {@code messageHistory}
	 * parameter.
	 *
	 * @param messageHistory
	 *            The {@link MessageHistory} object used by this TextChannel to
	 *            reference which history should be searched for.
	 * @param messageConditions
	 *            This parameter is used to run arbitrary code to make
	 *            conditions on the message itself. You could use this to only
	 *            find the messages of certain users, for example. Can be
	 *            {@code null} (or see
	 *            {@link #getFullMessageList(MessageHistory)}) to not have any
	 *            condition and find all messages.
	 * @return A {@link List} of of all {@link Message} that is present in the
	 *         TextChannel where this command was invoked with the conditions
	 *         applied.
	 * @since v0.10.0
	 * @see #getFullMessageList(MessageHistory)
	 * @see #getFullMessageList(MessageHistory, Predicate)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			ArrayList<Predicate<Message>> messageConditions){
		
		boolean isEmpty;
		
		do{
			
			isEmpty = messageHistory.retrievePast(100).complete().isEmpty();
			
		}while(!isEmpty && isAlive());
		
		List<Message> fullHistory = messageHistory.getRetrievedHistory();
		
		if(messageConditions == null)
			return fullHistory;
		
		ArrayList<Message> messagesWithCondition = new ArrayList<>();
		
		boolean invert = hasParameter("i");
		
		boolean conditionsGateIsAnd = !hasParameter("or");
		
		//@formatter:off
		fullHistory.forEach(message -> {
			
			boolean shouldDelete = conditionsGateIsAnd;
			
			// Acts as a AND gate, so if any of the conditions is false, the message is not deleted
			for(Predicate<Message> messageCondition : messageConditions){
				
				// Determine Gate logic and invert the logic if it's an OR
				if(messageCondition.test(message) != conditionsGateIsAnd){
					shouldDelete = !conditionsGateIsAnd;
					
					break;
				}
				
			}
			
			// If inverting, the condition must be false to clear this message
			if(shouldDelete != invert)
				messagesWithCondition.add(message);
			
		});
		//@formatter:on
		
		return messagesWithCondition;
		
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
					"Allows you to delete the messages of a user you specify.",
					"u", "user"),
			new ParametersHelp("Allows you to delete your own messages.",
					false, "s", "self"),
			new ParametersHelp(
					"Allows you to delete all of the bots messages.", false,
					"b", "bot"),
			new ParametersHelp(
					"Inverts the condition applied to the command (example : using this in combination with "
							+ formatParameter("s")
							+ " would clear messages of everyone but yourself).",
					false, "i", "invert"),
			new ParametersHelp(
					"This makes the bot notify the text channel of what it cleared.",
					false, "n", "notify"),
			new ParametersHelp(
					"Waits that the message has been successfully deleted before deleting the others. Useful if you are not sure if you should delete all the messages as you can stop the command.",
					false, "w", "wait"),
			new ParametersHelp(
					"Allows the conditions parser to use an OR logic gate instead of an AND for all conditions.",
					false, "or"),
		};
	}
	
}
