package commands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.exceptions.PermissionException;
import utilities.BotCommand;
import utilities.specifics.CommandConfirmed;
import vendor.exceptions.BadContentException;
import vendor.objects.ParametersHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This command clears every messages that matches the request's conditions in
 * the TextChannel where it was called from.
 * 
 * @author Stephano
 */
public class CommandClear extends BotCommand {
	
	@Override
	public void action(){
		
		if(!hasParameter("u","s","b")){
			
			new CommandConfirmed(this){
				
				@Override
				public String getConfMessage(){
					return lang("ConfirmMessage");
				}
				
				@Override
				public void confirmed(){
					
					try{
						
						deleteAllMessages();
						
						if(isAlive() && hasParameter("n"))
							sendMessage("Cleared everything!");
						
					}
					catch(PermissionException e){
						sendMessage(lang("NoPermission"));
					}
					
				}
				
			};
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
					usr = getSelfMember();
				}
				new CommandConfirmed(this){
					
					@Override
					public String getConfMessage(){
						return lang("ConfUsrClear",
								code(usr.getEffectiveName()));
					}
					
					@Override
					public void confirmed(){
						
						try{
							deleteMessagesIf(message -> usr.equals(message
									.getMember()));
							
							if(isAlive() && hasParameter("n"))
								sendInfoMessage("Cleared " + usr.getAsMention()
										+ "'s messages!");
							
						}
						catch(PermissionException e){
							sendMessage(lang("NoPermission"));
						}
					}
					
				};
			}
			catch(BadContentException e){
				sendMessage(lang("MentionError", code("@[username]")));
			}
			
		}
		
	}
	
	protected void deleteAllMessages() throws PermissionException{
		this.deleteMessagesIf(null);
	}
	
	protected void deleteMessagesIf(Predicate<Message> messageCondition)
			throws PermissionException{
		
		MessageHistory messageHistory = getTextContext().getHistory();
		
		boolean shouldCompleteBeforeNext = hasParameter("i");
		
		final List<Message> fullMessageHistory = this.getFullMessageList(
				messageHistory, messageCondition);
		
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
	
	protected List<Message> getFullMessageList(MessageHistory messageHistory){
		return this.getFullMessageList(messageHistory, null);
	}
	
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
					false, "i", "interactive"),
			new ParametersHelp(
				"Allows you to delete the messages of a user you specifie.",
				true, "u", "user"),
			new ParametersHelp(
				"Allows you to delete your own messages.",
				false, "s", "self"),
			new ParametersHelp(
				"Allows you to delete all of the bots messages.",
				false, "b", "bot"),
			new ParametersHelp(
				"This makes the bot notify the text channel of what it cleared.",
				false, "n", "notify"),
		};
	}
	
}
