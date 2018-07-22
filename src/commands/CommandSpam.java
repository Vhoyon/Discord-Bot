package commands;

import net.dv8tion.jda.core.entities.Member;
import utilities.BotCommand;
import vendor.exceptions.BadContentException;
import vendor.objects.ParametersHelp;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandSpam extends BotCommand {
	
	@Override
	public void action(){
		
		// Defaults to 10 messages.
		AtomicInteger numberOfSpam = new AtomicInteger(10);
		
		onParameterPresent("c", param -> {
			try{
				
				numberOfSpam.set(Integer.parseInt(param.getContent()));
				
			}
			catch(NumberFormatException e){
				
			}
		});
		
		boolean shouldSendToMember = hasParameter("u");
		
		Member memberToSpam = null;
		List<Member> membersToSpam = null;
		
		if(shouldSendToMember){
			try{
				
				String possibleMention = getParameter("u").getContent();
				
				if(isStringMention(possibleMention)){
					memberToSpam = getParameterAsMention("u");
				}
				else if(isStringMentionRole(possibleMention)){
					membersToSpam = getGuild()
							.getMembersWithRoles(
									getGuild()
											.getRoleById(
													getIdFromStringMentionRole(possibleMention)));
				}
				
			}
			catch(BadContentException e){
				sendMessage("The member specified is not valid. Tag him with "
						+ code("@[username]") + "!");
			}
		}
		
		if(!shouldSendToMember
				|| (shouldSendToMember && (memberToSpam != null || membersToSpam != null))){
			
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
							sendMessageToMember(memberToSpam, messageToSend);
						}
						else if(membersToSpam != null){
							
							for(Member member : membersToSpam){
								sendMessageToMember(member, messageToSend);
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
							+ " after the first message, for example.", "n",
					"number")
		};
	}
}
