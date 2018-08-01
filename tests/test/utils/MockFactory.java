package test.utils;

import vendor.objects.Request;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MockFactory {
	
	public static Request createRequest(char paramPrefix){
		Request request = spy(new Request(""));
		doReturn(paramPrefix).when(request).getParametersPrefix();
		
		return request;
	}
	
}
