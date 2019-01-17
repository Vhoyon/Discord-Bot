package utils;

import static org.mockito.Mockito.*;

import io.github.ved.jrequester.Request;
import io.github.vhoyon.bot.app.CommandRouter;
import io.github.vhoyon.vramework.utilities.settings.SettingRepository;

public class MockFactory {
	
	public static Request createRequest(char paramPrefix){
		Request request = spy(new Request(""));
		doReturn(paramPrefix).when(request).getOptionsPrefix();
		
		return request;
	}
	
	public static CommandRouter createRouter(SettingRepository settings){
		CommandRouter router = mock(CommandRouter.class);
		
		doReturn(settings).when(router).getSettings();
		
		return router;
	}
	
}
