package io.github.vhoyon.bot.commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
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
						throw new BadContentException(lang("ErrorSpamSelf"));
					else if(memberToSpam.getUser().isBot())
						throw new BadContentException(lang("ErrorSpamBots"));
				}
				else if(isStringMentionRole(possibleMention)){
					Role role = getGuild().getRoleById(
							getIdFromStringMentionRole(possibleMention));
					
					membersToSpam = getGuild().getMembersWithRoles(role);
					
					if(membersToSpam.size() == 0)
						throw new BadContentException(lang("ErrorEmptyRole"));
				}
				else{
					throw new BadContentException(lang("ErrorInvalidMention",
							code("@[username|role]")));
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
					message = lang("PrivateSpamFromMemberWithContent",
							ital(getMember().getAsMention()), getContent());
				}
				else{
					message = getContent();
				}
				
			}
			else{
				
				if(shouldSendToMember){
					message = lang("PrivateSpamFromMember", ital(getMember()
							.getAsMention()));
				}
				else{
					message = lang("SpamAllFromMember", ital(bold(getMember()
							.getAsMention())));
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
								sendMessage(lang("SpammingAuthor"));
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
		return lang("Description");
	}
	
	@Override
	public Option[] getOptions(){
		return new Option[]
		{
			new Option(lang("OptionCount"), "c", "count"),
			new Option(lang("OptionUser", code("@[username]")), "u", "user"),
			new Option(lang("OptionNumber", code("#1")), false, "n", "number")
		};
	}
	
}
