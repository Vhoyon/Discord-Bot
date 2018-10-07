package utils;

import io.github.vhoyon.bot.app.CommandRouter;
import io.github.vhoyon.vramework.objects.Request;
import io.github.vhoyon.vramework.utilities.settings.Setting;

import static org.mockito.Mockito.*;

public class MockFactory {
	
	public static Request createRequest(char paramPrefix){
		Request request = spy(new Request(""));
		doReturn(paramPrefix).when(request).getParametersPrefix();
		
		return request;
	}
	
	public static CommandRouter createRouter(Setting settings){
		CommandRouter router = mock(CommandRouter.class);
		
		doReturn(settings).when(router).getSettings();
		
		return router;
	}
	
}
