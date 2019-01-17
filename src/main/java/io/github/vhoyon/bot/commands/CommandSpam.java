package io.github.vhoyon.bot.commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import io.github.ved.jrequester.Option;
import io.github.ved.jsanitizers.IntegerSanitizer;
import io.github.ved.jsanitizers.exceptions.BadFormatException;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;
import io.github.vhoyon.vramework.exceptions.BadContentException;
import io.github.vhoyon.vramework.interfaces.Stoppable;
import io.github.vhoyon.vramework.objects.Mention;

/**
 * Command to spam people. Thus its name. It's a fun command.
 * <p>
 * This command has few parameters to change its behavior :
 * <ul>
 * <li>Not adding content sends a generic message while adding content to the
 * command's request will send it as the spamming message;</li>
 * <li>Adding the parameter {@code -c} (or {@code --count}) and a number
 * afterward allows to change the number of messages to be sent (default is
 * {@code 10});</li>
 * <li>Adding the flag {@code -n} (or {@code --number}) allows to append the
 * current position of the spam after the message (e.g. : {@code [message] #1});
 * </li>
 * <li>Adding the parameter {@code -u} (or {@code --user}) and a mention as the
 * parameter content will spam all of the found users in the mention found (be
 * it a direct mention or a role mention). Bots will not be spammed.</li>
 * </ul>
 * </p>
 * 
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 */
public class CommandSpam extends BotCommand implements Stoppable {
	
	@Override
	public void actions(){
		
		boolean canSpam = true;
		
		boolean shouldSendToMember = hasOption("u");
		
		Mention memberToSpam = null;
		List<Member> membersToSpam = null;
		
		if(shouldSendToMember){
			try{
				
				String possibleMention = getOption("u").getContent();
				
				if(isStringMention(possibleMention)){
					memberToSpam = getOptionAsMention("u");
					
					if(getBotMember().equals(memberToSpam))
						throw new BadContentException(
								"You think I'll spam myself? C'mon, I'm better than that...");
					else if(memberToSpam.getUser().isBot())
						throw new BadContentException(
								"I won't spam my fellow bots!");
				}
				else if(isStringMentionRole(possibleMention)){
					Role role = getGuild().getRoleById(
							getIdFromStringMentionRole(possibleMention));
					
					membersToSpam = getGuild().getMembersWithRoles(role);
					
					if(membersToSpam.size() == 0)
						throw new BadContentException(
								"The role you mentionned has no member in it, nobody was spammed!");
				}
				else{
					throw new BadContentException(
							"Your mention is not valid. Tag a user (or a role!) with "
									+ code("@[username|role]") + "!");
				}
				
			}
			catch(BadContentException e){
				new BotError(this, e.getMessage());
				canSpam = false;
			}
		}
		
		if(canSpam){
			
			// Defaults to 10 messages.
			AtomicInteger numberOfSpam = new AtomicInteger(10);
			
			onOptionPresent("c", option -> {
				try{
					
					numberOfSpam.set(IntegerSanitizer.sanitizeValue(option
							.getContent()));
					
				}
				catch(BadFormatException e){
					
				}
			});
			
			String message;
			
			if(hasContent()){
				
				if(shouldSendToMember){
					message = ital(getMember().getAsMention()
							+ " is spamming you this :")
							+ " " + getContent();
				}
				else{
					message = getContent();
				}
				
			}
			else{
				
				if(shouldSendToMember){
					message = ital(getMember().getAsMention()
							+ " is spamming you!");
				}
				else{
					message = ital(bold(getMember().getAsMention()))
							+ " is spamming y'all!";
				}
				
			}
			
			boolean shouldAppendNumber = hasOption("n");
			
			try{
				
				for(int i = 0; i < numberOfSpam.get() && isAlive(); i++){
					
					if(i != 0)
						Thread.sleep(1250);
					
					String messageToSend = shouldAppendNumber ? message + " #"
							+ (i + 1) : message;
					
					if(!shouldSendToMember){
						
						sendMessage(messageToSend);
						
					}
					else{
						
						if(memberToSpam != null){
							if(i == 0 && memberToSpam.isMentionningSelf()){
								sendMessage("I like how you are spamming yourself. Good job.");
							}
							
							sendMessageToMember(memberToSpam, messageToSend);
						}
						else if(membersToSpam != null){
							
							for(Member member : membersToSpam){
								if(!(member.getUser().isBot() || member
										.getUser().isFake()))
									new Thread(() -> sendMessageToMember(
											member, messageToSend)).start();
							}
							
						}
						
					}
					
				}
				
			}
			catch(InterruptedException e){}
			
		}
		
	}
	
	@Override
	public String getCall(){
		return SPAM;
	}
	
	@Override
	public String getCommandDescription(){
		return "This command sends the specified amount of the specified message in a text channel";
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option("Specifies how many messages will be sent.", "c",
					"count"),
			new Option(
					"Specifies a user to send the messages to. Mention the user you want to spam using the "
							+ code("@[username]") + " notation.", "u", "user"),
			new Option(
					"Specifies if the message should have its number appended at the end. This parameter will add "
							+ code("#1")
							+ " after the first message, for example.", false,
					"n", "number")
		};
	}
	
}
