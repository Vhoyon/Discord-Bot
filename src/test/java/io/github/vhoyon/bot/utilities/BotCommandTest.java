package io.github.vhoyon.bot.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import utils.MockFactory;
import io.github.ved.jrequester.Request;
import io.github.vhoyon.vramework.util.formatting.DiscordFormatter;

class BotCommandTest implements DiscordFormatter {
	
	@Mock
	BotCommand mockCommand;
	
	@BeforeEach
	void setUp(){
		mockCommand = Mockito.spy(BotCommand.class);
	}
	
	@AfterEach
	void tearDown(){
		mockCommand = null;
	}
	
	@Test
	void testFormatOptionShort(){
		String testParameterName = "t";
		char prefix = '-';
		
		Request request = MockFactory.createRequest(prefix);
		
		doReturn(request).when(mockCommand).getRequest();
		
		String expected = code(prefix + testParameterName);
		
		assertEquals(expected, mockCommand.formatOption(testParameterName));
	}
	
	@Test
	void testFormatOptionLong(){
		String testParameterName = "test";
		char prefix = '-';
		
		Request mockRequest = MockFactory.createRequest(prefix);
		
		doReturn(mockRequest).when(mockCommand).getRequest();
		
		String expected = code(String.valueOf(prefix) + String.valueOf(prefix)
				+ testParameterName);
		
		assertEquals(expected, mockCommand.formatOption(testParameterName));
	}
	
	@Test
	void testUsageStringCreation(){
		String testCommandName = "test";
		String prefix = "!!";
		
		doReturn(testCommandName).when(mockCommand).getCommandName();
		doReturn(prefix + testCommandName).when(mockCommand).buildCommand(
				testCommandName);
		
		String expected = code(prefix + testCommandName);
		
		assertEquals(expected, mockCommand.getUsage());
	}
	
}