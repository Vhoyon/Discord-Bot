package java.io.github.vhoyon.bot.commands;

import io.github.stephanomehawej.gitinteractions.Issue;
import io.github.stephanomehawej.gitinteractions.exceptions.attributeNotFoundException;
import io.github.stephanomehawej.gitinteractions.exceptions.invalidValueException;
import io.github.vhoyon.bot.utilities.BotCommand;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CommandSuggestion extends BotCommand {
	
	@Override
	public void actions() {
		Issue issue = null;

		try {
			
			URI uri = new URI("");
			JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
			JSONObject issueInfo = new JSONObject(tokener);


			issue = new Issue(issueInfo.get("repo").toString(),issueInfo.get("login").toString(),issueInfo.get("OAuth").toString());
			issue.JSonIssue(issueInfo);
			issue.sendIssue();
		} catch (attributeNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (invalidValueException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
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
