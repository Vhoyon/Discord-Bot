package tests.utilities;


import org.junit.jupiter.api.*;
import org.mockito.Mock;
import utilities.BotCommand;
import test.utils.MockFactory;
import vendor.objects.Request;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotCommandTest {
	
	@Mock
	BotCommand mockCommand;
	
	@BeforeEach
	void setUp(){
		mockCommand = spy(BotCommand.class);
	}
	
	@AfterEach
	void tearDown(){
		mockCommand = null;
	}
	
	@Test
	void testFormatParameterShort(){
		String testParameterName = "t";
		
		Request request = MockFactory.createRequest('-');
		
		doReturn(request).when(mockCommand).getRequest();
		
		String expected = "`-" + testParameterName + "`";
		
		assertEquals(expected, mockCommand.formatParameter(testParameterName));
	}

	@Test
	void testFormatParameterLong(){
		String testParameterName = "test";

		Request request = MockFactory.createRequest('-');

		doReturn(request).when(mockCommand).getRequest();

		String expected = "`--" + testParameterName + "`";

		assertEquals(expected, mockCommand.formatParameter(testParameterName));
	}
	
	@Test
	void testUsageStringCreation(){
		String testCommandName = "test";
		
		doReturn(testCommandName).when(mockCommand).getCommandName();
		doReturn("!!" + testCommandName).when(mockCommand).buildCommand(
				testCommandName);
		
		String expected = "`!!" + testCommandName + "`";
		
		assertEquals(expected, mockCommand.getUsage());
	}
	
	@Test
	void setting(){
		fail("Not yet implemented...");
	}
	
	@Test
	void setSetting(){
		fail("Not yet implemented...");
	}
	
}