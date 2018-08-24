package commands;

import errorHandling.BotError;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import utilities.BotCommand;
import vendor.exceptions.BadContentException;
import vendor.objects.Mention;
import vendor.objects.ParametersHelp;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
public class CommandSpam extends BotCommand {
	
	@Override
	public void action(){
		
		boolean canSpam = true;
		
		boolean shouldSendToMember = hasParameter("u");
		
		Mention memberToSpam = null;
		List<Member> membersToSpam = null;
		
		if(shouldSendToMember){
			try{
				
				String possibleMention = getParameter("u").getContent();
				
				if(isStringMention(possibleMention)){
					memberToSpam = getParameterAsMention("u");
					
					if(getSelfMember().equals(memberToSpam))
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
			
			onParameterPresent("c", param -> {
				try{
					
					numberOfSpam.set(Integer.parseInt(param.getContent()));
					
				}
				catch(NumberFormatException e){
					
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
			
			boolean shouldAppendNumber = hasParameter("n");
			
			for(int i = 0; i < numberOfSpam.get() && isAlive(); i++){
				
				try{
					
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
								if(!member.getUser().isBot())
									new Thread(() -> sendMessageToMember(
											member, messageToSend)).start();
							}
							
						}
						
					}
					
				}
				catch(InterruptedException e){}
				
			}
			
		}
		
	}
	
	@Override
	public boolean stopAction(){
		return true;
	}
	
	@Override
	public Object getCalls(){
		return SPAM;
	}
	
	@Override
	public String getCommandDescription(){
		return "This command sends the specified amount of the specified message in a text channel";
	}
	
	@Override
	public ParametersHelp[] getParametersDescriptions(){
		return new ParametersHelp[]
		{
			new ParametersHelp("Specifies how many messages will be sent.",
					"c", "count"),
			new ParametersHelp(
					"Specifies a user to send the messages to. Mention the user you want to spam using the "
							+ code("@[username]") + " notation.", "u", "user"),
			new ParametersHelp(
					"Specifies if the message should have its number appended at the end. This parameter will add "
							+ code("#1")
							+ " after the first message, for example.", false,
					"n", "number")
		};
	}
	
}
