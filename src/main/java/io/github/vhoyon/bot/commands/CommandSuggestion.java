package io.github.vhoyon.bot.commands;

import io.github.stephanomehawej.gitinteractions.Issue;
import io.github.stephanomehawej.gitinteractions.Management;
import io.github.stephanomehawej.gitinteractions.exceptions.attributeNotFoundException;
import io.github.stephanomehawej.gitinteractions.exceptions.invalidValueException;
import io.github.vhoyon.bot.errorHandling.BotError;
import io.github.vhoyon.bot.utilities.BotCommand;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CommandSuggestion extends BotCommand {
	
	@Override
	public void actions() {
		Issue issue = null;

		if(!hasContent()){
			new BotError(lang("ContentMissing"));
		}else{
			try {
				issue = new Issue(env("SUGGESTION_GITHUB_REPO"),env("SUGGESTION_GITHUB_LOGIN"),env("SUGGESTION_GITHUB_OAUTH"));
				issue.setAttribue("title",lang( "Title" ,new Management().countByLabel("suggestions")+1));
				issue.setAttribue("body",getContent());
				issue.setAttribue("labels", new String[]{"suggestions"});
				String url = issue.sendIssue();
				
				sendInfoMessage(lang("SentSuggestion", url));

			} catch (IOException e) {
				new BotError(this,e.getMessage());
				e.printStackTrace();
			} catch (invalidValueException e) {
				new BotError(this,e.getMessage());
				e.printStackTrace();
			} catch (Exception e){
				new BotError(this,e.getMessage());

			}
		}
		
	}
	@Override
	public String getCall(){
		return SUGGESTION;
	}

	@Override
	public String getCommandDescription(){
		return lang("Description");
	}
}
