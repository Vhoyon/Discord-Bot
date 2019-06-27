package io.github.vhoyon.bot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;
import io.github.ved.jrequester.Option;
import io.github.ved.jsanitizers.EnumSanitizer;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.bot.utilities.specifics.CommandConfirmed;
import io.github.vhoyon.vramework.exceptions.BadContentException;
import io.github.vhoyon.vramework.interfaces.Stoppable;
import io.github.vhoyon.vramework.utilities.MessageManager;
import io.github.vhoyon.vramework.utilities.ThreadPool;

/**
 * This command clears every messages that matches the request's conditions in
 * the TextChannel where it was called from.
 * 
 * @version 1.0
 * @since v0.4.0
 * @author Stephano
 */
public class CommandClear extends BotCommand implements Stoppable {
	
	protected List<Predicate<Message>> conditions;
	protected int messageWeight = 0;
	
	protected MessageManager confManager;
	protected MessageManager notifyManager;
	
	protected void addReplacement(String key, Object value){
		
		if(confManager == null){
			confManager = new MessageManager();
			notifyManager = new MessageManager();
		}
		
		confManager.addReplacement(key, value);
		notifyManager.addReplacement(key, value);
		
	}
	
	protected void addCondition(String parameterUsed,
			Predicate<Message> condition){
		
		if(this.conditions == null)
			this.conditions = new ArrayList<>();
		
		this.conditions.add(condition);
		
		onOptionPresent(parameterUsed, option -> {
			this.messageWeight += option.getWeight();
		});
		
	}
	
	@Override
	public void actions(){
		
		boolean shouldDoClear = true;
		
		if(hasOption("c")){
			
			String content = getOption("c").getContent();
			
			if(content == null){
				
				final String commandPrefix = getRequest().getCommandPrefix();
				
				addReplacement("Prefixes", code(commandPrefix));
				addCondition("c", message -> message.getContentStripped()
						.replaceAll("\\\\\\\\", "").startsWith(commandPrefix));
				
			}
			else{
				
				List<String> prefixes = EnumSanitizer
						.extractEnumFromString(content);
				
				StringBuilder builder = new StringBuilder();
				for(String prefix : prefixes){
					builder.append(code(prefix)).append(", ");
				}
				builder.delete(builder.length() - 2, builder.length());
				
				addReplacement("Prefixes", builder.toString());
				
				addCondition(
						"c",
						message -> prefixes.stream().anyMatch(
								prefix -> message.getContentStripped()
										.replaceAll("\\\\\\\\", "")
										.startsWith(prefix)));
				
			}
			
		}
		
		if(hasOption("u", "s", "b")){
			
			try{
				
				String paramUsed;
				
				final Member usr;
				
				if(hasOption("u")){
					usr = getOptionAsMention("u");
					paramUsed = "u";
				}
				else if(hasOption("s")){
					usr = getMember();
					paramUsed = "s";
				}
				else{
					usr = getBotMember();
					paramUsed = "b";
				}
				
				addReplacement("user", usr.getAsMention());
				
				addCondition(paramUsed,
						message -> usr.equals(message.getMember()));
				
			}
			catch(BadContentException e){
				sendMessage(lang("MentionError", code("@[username]")));
				
				shouldDoClear = false;
			}
			
		}
		
		if(shouldDoClear)
			doClearLogic(hasOption("i"));
		
	}
	
	protected void setupConfMessages(){
		
		if(this.confManager == null)
			this.confManager = new MessageManager();
		
		this.confManager.addMessage(-12, "ConfPrefBotInv", "Prefixes");
		this.confManager.addMessage(-10, "ConfPrefSelfInv", "Prefixes", "user");
		this.confManager.addMessage(-9, "ConfPrefUsrInv", "Prefixes", "user");
		this.confManager.addMessage(-8, "ConfPrefInv", "Prefixes");
		this.confManager.addMessage(-4, "ConfBotInv");
		this.confManager.addMessage(-2, "ConfSelfInv", "user");
		this.confManager.addMessage(-1, "ConfUsrInv", "user");
		this.confManager.addMessage(0, "ConfAll");
		this.confManager.addMessage(1, "ConfUsr", "user");
		this.confManager.addMessage(2, "ConfSelf", "user");
		this.confManager.addMessage(4, "ConfBot");
		this.confManager.addMessage(8, "ConfPref", "Prefixes");
		this.confManager.addMessage(9, "ConfPrefUsr", "Prefixes", "user");
		this.confManager.addMessage(10, "ConfPrefSelf", "Prefixes", "user");
		this.confManager.addMessage(12, "ConfPrefBot", "Prefixes");
		
	}
	
	protected void setupNotifyMessages(){
		
		if(this.notifyManager == null)
			this.notifyManager = new MessageManager();
		
		this.notifyManager.addMessage(-12, "NotifPrefBotInv", "Prefixes");
		this.notifyManager.addMessage(-10, "NotifPrefSelfInv", "Prefixes",
				"user");
		this.notifyManager
				.addMessage(-9, "NotifPrefUsrInv", "Prefixes", "user");
		this.notifyManager.addMessage(-8, "NotifPrefInv", "Prefixes");
		this.notifyManager.addMessage(-4, "NotifBotInv");
		this.notifyManager.addMessage(-2, "NotifSelfInv", "user");
		this.notifyManager.addMessage(-1, "NotifUsrInv", "user");
		this.notifyManager.addMessage(0, "NotifAll");
		this.notifyManager.addMessage(1, "NotifUsr", "user");
		this.notifyManager.addMessage(2, "NotifSelf", "user");
		this.notifyManager.addMessage(4, "NotifBot");
		this.notifyManager.addMessage(8, "NotifPref", "Prefixes");
		this.notifyManager.addMessage(9, "NotifPrefUsr", "Prefixes", "user");
		this.notifyManager.addMessage(10, "NotifPrefSelf", "Prefixes", "user");
		this.notifyManager.addMessage(12, "NotifPrefBot", "Prefixes");
		
	}
	
	/**
	 * Executes the logic to clear the messages with the conditions present in
	 * the value of CommandClear's conditions array. This method resolves the
	 * confirmation and notification messages based on the weight of the current
	 * conditions status.
	 * 
	 * @see #doClearLogic(String, String, boolean)
	 */
	protected void doClearLogic(boolean shouldInvert){
		
		if(shouldInvert)
			this.messageWeight *= -1;
		
		setupConfMessages();
		setupNotifyMessages();
		
		String confMessage = confManager.getMessage(this.messageWeight,
				this.getDictionary(), this);
		String notifMessage = notifyManager.getMessage(this.messageWeight,
				this.getDictionary(), this);
		
		doClearLogic(confMessage, notifMessage, shouldInvert);
		
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
			final String notifyMessage, boolean shouldInvert){
		
		if(hasOption("f")){
			callDeleteMessages(notifyMessage, shouldInvert);
		}
		else{
			new CommandConfirmed(this){
				
				@Override
				public String getConfMessage(){
					return confMessage;
				}
				
				@Override
				public void confirmed(){
					callDeleteMessages(notifyMessage, shouldInvert);
				}
				
			};
		}
		
	}
	
	protected void callDeleteMessages(final String notifyMessage,
			boolean shouldInvert){
		try{
			
			deleteMessages(CommandClear.this.conditions, shouldInvert);
			
			if(notifyMessage != null && isAlive() && hasOption("n"))
				sendInfoMessage(notifyMessage);
			
		}
		catch(PermissionException e){
			new BotError(CommandClear.this, lang("NoPermission"));
		}
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
			final List<Predicate<Message>> messageConditions,
			boolean shouldInvert) throws PermissionException{
		
		MessageHistory messageHistory = getTextContext().getHistory();
		
		boolean shouldCompleteBeforeNext = hasOption("w");
		
		int messageProcessed = 0;
		
		ThreadPool deletePool = new ThreadPool();
		
		do{
			
			final List<Message> subMessageHistory = this.getMessageListMax(
					messageHistory, 1000, messageProcessed, messageConditions,
					shouldInvert);
			
			if(subMessageHistory == null)
				break;
			
			if(subMessageHistory.size() != 0){
				
				if(messageProcessed == 0){
					doDeleteLogic(subMessageHistory, shouldCompleteBeforeNext);
				}
				else{
					deletePool.execute(() -> doDeleteLogic(subMessageHistory,
							shouldCompleteBeforeNext));
				}
				
			}
			
			messageProcessed += messageHistory.size();
			
		}while(true);
		
		deletePool.stopWorkers();
		
	}
	
	protected void doDeleteLogic(final List<Message> messagesToDelete,
			boolean shouldCompleteBeforeNext) throws PermissionException{
		
		boolean deletingIndividually = messagesToDelete.size() < 2;
		
		for(int i = 0; i < messagesToDelete.size() && isAlive(); i += 100){
			
			List<Message> currentMessageList;
			
			try{
				currentMessageList = messagesToDelete.subList(i, i + 100);
			}
			catch(IndexOutOfBoundsException e){
				currentMessageList = messagesToDelete.subList(i,
						messagesToDelete.size());
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
	 * @see #getFullMessageList(MessageHistory, Predicate, boolean)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			boolean shouldInvert){
		return this.getFullMessageList(messageHistory,
				(List<Predicate<Message>>)null, shouldInvert);
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
	 *            {@link #getFullMessageList(MessageHistory, boolean)}) to not
	 *            have any condition and find all messages.
	 * @return A {@link List} of of all {@link Message} that is present in the
	 *         TextChannel where this command was invoked.
	 * @since v0.10.0
	 * @see #getFullMessageList(MessageHistory, boolean)
	 * @see #getFullMessageList(MessageHistory, List, boolean)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			Predicate<Message> messageCondition, boolean shouldInvert){
		
		List<Predicate<Message>> singleCondition = new ArrayList<>();
		singleCondition.add(messageCondition);
		
		return getFullMessageList(messageHistory, singleCondition, shouldInvert);
		
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
	 *            {@link #getFullMessageList(MessageHistory, boolean)}) to not
	 *            have any condition and find all messages.
	 * @return A {@link List} of of all {@link Message} that is present in the
	 *         TextChannel where this command was invoked with the conditions
	 *         applied.
	 * @since v0.10.0
	 * @see #getFullMessageList(MessageHistory, boolean)
	 * @see #getFullMessageList(MessageHistory, Predicate, boolean)
	 */
	protected List<Message> getFullMessageList(MessageHistory messageHistory,
			List<Predicate<Message>> messageConditions, boolean shouldInvert){
		return getMessageListMax(messageHistory, -1, 0, messageConditions,
				shouldInvert);
	}
	
	protected List<Message> getMessageListMax(MessageHistory messageHistory,
			int numberOfMessageToHandle, int startRetrievingAt,
			List<Predicate<Message>> messageConditions, boolean shouldInvert){
		
		boolean isEmpty;
		
		int numberOfMessages = 0;
		
		do{
			
			List<Message> subMessageList = messageHistory.retrievePast(100)
					.complete();
			
			isEmpty = subMessageList.isEmpty();
			
			numberOfMessages += subMessageList.size();
			
		}while(!isEmpty
				&& (numberOfMessageToHandle < 0 || numberOfMessages < numberOfMessageToHandle)
				&& isAlive());
		
		List<Message> fullHistory = messageHistory.getRetrievedHistory();
		
		if(startRetrievingAt > fullHistory.size()){
			return null;
		}
		
		List<Message> subHistory = null;
		
		try{
			subHistory = fullHistory.subList(startRetrievingAt,
					fullHistory.size());
		}
		catch(IndexOutOfBoundsException e){}
		
		if(subHistory == null || subHistory.size() == 0)
			return null;
		
		return getMessagesWithConditions(subHistory, messageConditions,
				shouldInvert);
		
	}
	
	private List<Message> getMessagesWithConditions(
			List<Message> messageHistory,
			List<Predicate<Message>> messageConditions, boolean shouldInvert){
		
		if(messageConditions == null)
			return messageHistory;
		
		List<Message> messagesWithCondition = new ArrayList<>();
		
		boolean conditionsGateIsAnd = !hasOption("or");
		
		//@formatter:off
		messageHistory.forEach(message -> {
			
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
			if(shouldDelete != shouldInvert)
				messagesWithCondition.add(message);
			
		});
		//@formatter:on
		
		return messagesWithCondition;
		
	}
	
	@Override
	public String getCall(){
		return CLEAR;
	}
	
	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(lang("OptionUser"), 1, "u", "user"),
			new Option(lang("OptionSelf"), false, 2, "s", "self"),
			new Option(lang("OptionBot"), false, 3, "b", "bot"),
			new Option("OptionCommand", true, 4, "c", "command"),
			new Option(lang("OptionInvert", formatOption("s")), false, "i",
					"invert"),
			new Option(lang("OptionNotify"), false, "n", "notify"),
			new Option(lang("OptionForce"), "f", "force"),
			new Option(lang("OptionWait"), false, "w", "wait"),
			new Option(lang("OptionOr"), false, "or"),
		};
	}
	
}
