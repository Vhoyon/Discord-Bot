package testingutils;

import app.CommandRouter;
import vendor.objects.Request;
import vendor.utilities.settings.Setting;

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
