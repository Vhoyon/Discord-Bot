package vendor.abstracts;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AccountManager;
import net.dv8tion.jda.core.managers.GuildController;
import vendor.exceptions.BadContentException;
import vendor.exceptions.NoContentException;
import vendor.interfaces.DiscordUtils;
import vendor.interfaces.Emojis;
import vendor.interfaces.LinkableCommand;
import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.objects.Buffer;
import vendor.objects.Mention;
import vendor.objects.MessageEventDigger;
import vendor.objects.Request;
import vendor.objects.Request.Parameter;
import vendor.res.FrameworkResources;
import vendor.utilities.FrameworkTemplate;
import vendor.utilities.formatting.DiscordFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public abstract class AbstractBotCommand extends Translatable implements
		Emojis, Utils, LinkableCommand, FrameworkResources, DiscordFormatter,
		DiscordUtils {
	
	public enum BufferLevel{
		CHANNEL, SERVER, USER
	}
	
	public static final BufferLevel DEFAULT_BUFFER_LEVEL = BufferLevel.CHANNEL;
	
	protected AbstractCommandRouter router;
	
	private boolean isCopy;
	
	public AbstractBotCommand(){
		this.isCopy = false;
	}
	
	public AbstractBotCommand(AbstractBotCommand commandToCopy){
		this();
		putStateFromCommand(commandToCopy);
	}
	
	public void putStateFromCommand(AbstractBotCommand commandToCopy){
		
		setRouter(commandToCopy.getRouter());
		setDictionary(commandToCopy.getDictionary());
		
		this.isCopy = true;
		
	}
	
	public String getCommandName(){
		
		String requestName = getRequest().getCommand();
		
		Object calls = getCalls();
		
		if(calls instanceof String[]){
			
			if(Arrays.asList((String[])calls).contains(requestName))
				return requestName;
			
		}
		else{
			
			if(calls.equals(requestName))
				return requestName;
			
		}
		
		return getDefaultCall();
		
	}
	
	protected String getContent(){
		return getRequest().getContent();
	}
	
	protected String[] getSplitContent(){
		
		if(getContent() != null)
			return getContent().split(" ");
		else
			return null;
		
	}
	
	protected String[] getSplitContentMaxed(int maxSize){
		
		if(getContent() != null)
			return getContent().split(" ", maxSize);
		else
			return null;
		
	}
	
	public Mention getContentAsMention() throws BadContentException{
		if(!isStringMention(getContent()))
			throw new BadContentException("Content is not a mention.");
		
		return new Mention(getIdFromStringMention(getContent()),
				getEventDigger());
	}
	
	public AbstractCommandRouter getRouter(){
		return router;
	}
	
	public void setRouter(AbstractCommandRouter router){
		this.router = router;
	}
	
	public Buffer getBuffer(){
		return getRouter().getBuffer();
	}
	
	public boolean remember(Object object, String associatedName){
		return remember(object, associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean remember(Object object, String associatedName,
			BufferLevel level){
		return getBuffer().push(object, associatedName, getKey(level));
	}
	
	public Object getMemory(String associatedName) throws NullPointerException{
		return getMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public Object getMemory(String associatedName, BufferLevel level)
			throws NullPointerException{
		return getBuffer().get(associatedName, getKey(level));
	}
	
	public boolean forget(String associatedName){
		return forget(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean forget(String associatedName, BufferLevel level){
		return getBuffer().remove(associatedName, getKey(level));
	}
	
	public boolean hasMemory(String associatedName){
		return hasMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean hasMemory(String associatedName, BufferLevel level){
		try{
			return getMemory(associatedName, level) != null;
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	protected MessageReceivedEvent getEvent(){
		return getEventDigger().getEvent();
	}
	
	public Member getSelfMember(){
		return getGuild().getSelfMember();
	}
	
	public SelfUser getSelfUser(){
		return FrameworkTemplate.jda.getSelfUser();
	}
	
	public AccountManager getSelfUserManager(){
		return getSelfUser().getManager();
	}
	
	public Member getMember(){
		return getEventDigger().getMember();
	}
	
	public User getUser(){
		return getEventDigger().getUser();
	}
	
	public String getUserId(){
		return getEventDigger().getUserId();
	}
	
	public String getUserName(){
		return getEventDigger().getUserName();
	}
	
	protected TextChannel getTextContext(){
		return getEventDigger().getChannel();
	}
	
	public String getTextChannelId(){
		return getEventDigger().getChannelId();
	}
	
	public GuildController getGuildController(){
		return new GuildController(getGuild());
	}
	
	public Guild getGuild(){
		return getEventDigger().getGuild();
	}
	
	public String getGuildId(){
		return getEventDigger().getGuildId();
	}
	
	public String getKey(){
		return getKey(BufferLevel.CHANNEL);
	}
	
	public MessageEventDigger getEventDigger(){
		return getRouter().getEventDigger();
	}
	
	public String getKey(BufferLevel level){
		switch(level){
		case SERVER:
			return getEventDigger().getGuildKey();
		case USER:
			return getEventDigger().getUserKey();
		case CHANNEL:
		default:
			return getEventDigger().getChannelKey();
		}
	}
	
	public Request getRequest(){
		return getRouter().getRequest();
	}
	
	public boolean isCopy(){
		return this.isCopy;
	}
	
	public boolean isAlive(){
		return this.getRouter().isAlive();
	}
	
	public void kill(){
		this.getRouter().interrupt();
	}
	
	public HashMap<String, Parameter> getParameters(){
		return this.getRequest().getParameters();
	}
	
	public HashMap<Parameter, ArrayList<String>> getParametersLinks(){
		return this.getRequest().getParametersLinks();
	}
	
	public Parameter getParameter(String... parameterNames)
			throws NoContentException{
		return this.getRequest().getParameter(parameterNames);
	}
	
	public Mention getParameterAsMention(String... parametersNames)
			throws NoContentException, BadContentException{
		
		Parameter paramFound = getParameter(parametersNames);
		
		if(!isStringMention(paramFound.getParameterContent()))
			throw new BadContentException("Parameter content is not a mention.");
		
		return new Mention(
				getIdFromStringMention(paramFound.getParameterContent()),
				getEventDigger());
		
	}
	
	public boolean hasParameter(String parameterName){
		return this.getRequest().hasParameter(parameterName);
	}
	
	public boolean hasParameter(String... parameterNames){
		return this.getRequest().hasParameter(parameterNames);
	}
	
	public boolean stopAction(){
		return false;
	}
	
	public void connect(VoiceChannel voiceChannel){
		getGuild().getAudioManager().openAudioConnection(voiceChannel);
		
		remember(voiceChannel, BUFFER_VOICE_CHANNEL);
	}
	
	public VoiceChannel getConnectedVoiceChannel(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	public void disconnect(){
		
		if(getConnectedVoiceChannel() != null){
			
			getGuild().getAudioManager().closeAudioConnection();
			
		}
		
		forget(BUFFER_VOICE_CHANNEL);
		
	}
	
	public void setSelfNickname(String nickname){
		this.setNicknameOf(this.getSelfMember(), nickname);
	}
	
	public void setNicknameOf(Member member, String nickname){
		this.getGuildController().setNickname(member, nickname).complete();
	}
	
	public String sendMessage(String messageToSend){
		if(messageToSend == null){
			log("The bot attempted to send a null message - probably a fail safe, but concerning nontheless...");
			
			return null;
		}
		
		try{
			return this.getTextContext().sendMessage(messageToSend).complete()
					.getId();
		}
		catch(IllegalArgumentException e){
			log(e.getMessage());
			
			return null;
		}
	}
	
	public String sendPrivateMessage(String messageToSend){
		
		PrivateChannel channel = getUser().openPrivateChannel().complete();
		
		if(getUser().hasPrivateChannel()){
			
			if(messageToSend == null){
				log("The bot attempted to send a null message - probably a fail safe, but concerning nonetheless...");
				
				return null;
			}
			
			try{
				return channel.sendMessage(messageToSend).complete().getId();
			}
			catch(IllegalArgumentException e){
				log(e.getMessage());
				
				return null;
			}
			
		}
		else{
			return sendMessage(lang(true, "CommandUserHasNoPrivateChannel"));
		}
		
	}
	
	public String createInfoMessage(String messageToSend, boolean isOneLiner){
		
		String infoChars = "\\~\\~";
		
		String separator;
		
		if(isOneLiner)
			separator = " ";
		else
			separator = "\n";
		
		return infoChars + separator + messageToSend + separator + infoChars;
		
	}
	
	public String sendInfoMessage(String messageToSend, boolean isOneLiner){
		return sendMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String sendInfoMessage(String messageToSend){
		return sendInfoMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend){
		return sendInfoPrivateMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		return sendPrivateMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String groupAndSendMessages(String... messages){
		
		StringBuilder messageToSend = new StringBuilder(messages[0]);
		
		for(int i = 1; i < messages.length; i++)
			messageToSend.append("\n").append(messages[i]);
		
		return sendMessage(messageToSend.toString());
		
	}
	
	public String groupAndSendMessages(ArrayList<String> messages){
		return groupAndSendMessages(messages
				.toArray(new String[messages.size()]));
	}
	
	public String editMessage(String messageId, String newMessage){
		return getTextContext().editMessageById(messageId, newMessage)
				.complete().getId();
	}
	
	public void editMessageQueue(String messageId, String newMessage){
		getTextContext().editMessageById(messageId, newMessage).queue();
	}
	
	public void log(String message){
		Logger.log(message);
	}
	
	/**
	 * @return A String that starts with the router's command prefix
	 *         followed by the <i>commandName</i> parameter.
	 */
	public String buildCommand(String command){
		return getRequest().getCommandPrefix() + command;
	}
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter, surrounded by two
	 *         "<b>`</b>" tick, meaning the visual will be like code in Discord.
	 */
	public String buildVCommand(String command){
		return code(buildCommand(command));
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	public String buildParameter(String parameter){
		return String.join(
				"",
				Collections.nCopies(parameter.length() > 1 ? 2 : 1,
						String.valueOf(getRequest().getParametersPrefix())))
				+ parameter;
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	public String buildVParameter(String parameter){
		return code(buildParameter(parameter));
	}
	
	@Override
	public String formatParameter(String parameterToFormat){
		return buildParameter(parameterToFormat);
	}
	
}
