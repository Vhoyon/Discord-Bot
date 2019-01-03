package io.github.vhoyon.bot.utilities;

import io.github.vhoyon.vramework.objects.Request;
import io.github.vhoyon.vramework.utilities.formatting.DiscordFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import utils.MockFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

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
	void testFormatParameterShort(){
		String testParameterName = "t";
		char prefix = '-';
		
		Request request = MockFactory.createRequest(prefix);
		
		doReturn(request).when(mockCommand).getRequest();
		
		String expected = code(prefix + testParameterName);
		
		assertEquals(expected, mockCommand.formatParameter(testParameterName));
	}
	
	@Test
	void testFormatParameterLong(){
		String testParameterName = "test";
		char prefix = '-';
		
		Request mockRequest = MockFactory.createRequest(prefix);
		
		doReturn(mockRequest).when(mockCommand).getRequest();
		
		String expected = code(String.valueOf(prefix) + String.valueOf(prefix)
				+ testParameterName);
		
		assertEquals(expected, mockCommand.formatParameter(testParameterName));
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
	
//	@Test
//	void testGetSettingsNotChanged(){
//		String defValue = "defValue";
//		
//		Setting testField = new TextField("test", "ENV_TEST", defValue);
//		SettingRepository settings = new SettingRepository(testField);
//		
//		CommandRouter mockRouter = MockFactory.createRouter(settings);
//		
//		doReturn(mockRouter).when(mockCommand).getRouter();
//		
//		assertEquals(defValue, mockCommand.setting("test"));
//	}
//	
//	@Test
//	void testGetSettingsChanged(){
//		Setting testField = new TextField("test", "ENV_TEST", "testValue");
//		SettingRepository settings = new SettingRepository(testField);
//		
//		CommandRouter mockRouter = MockFactory.createRouter(settings);
//		
//		doReturn(mockRouter).when(mockCommand).getRouter();
//		
//		String newValue = "modifiedValue";
//		
//		mockCommand.setSetting("test", newValue);
//		
//		assertEquals(newValue, mockCommand.setting("test"));
//	}
//	
//	@Test
//	void testGetSettingsChangedCallback(){
//		Setting testField = new TextField("test", "ENV_TEST", "testValue");
//		SettingRepository settings = new SettingRepository(testField);
//		
//		CommandRouter mockRouter = MockFactory.createRouter(settings);
//		
//		doReturn(mockRouter).when(mockCommand).getRouter();
//		
//		AtomicReference<String> callbackedModifiedValue = new AtomicReference<>(
//				null);
//		
//		String newValue = "modifiedValue";
//		
//		mockCommand.setSetting("test", newValue,
//				(modifiedValue) -> callbackedModifiedValue
//						.set((String)modifiedValue));
//		
//		assertEquals(newValue, callbackedModifiedValue.get());
//	}
//	
//	@Test
//	void testGetSettingsAutoCast(){
//		Setting testFieldText = new TextField("testText", "ENV_TEST_TEXT",
//				"testValue");
//		Setting testFieldInt = new IntegerField("testInt", "ENV_TEST_INT",
//				2);
//		SettingRepository settings = new SettingRepository(testFieldText, testFieldInt);
//		
//		CommandRouter mockRouter = MockFactory.createRouter(settings);
//		
//		doReturn(mockRouter).when(mockCommand).getRouter();
//		
//		Executable shouldThrowClassCastException = () -> {
//			int badObject = mockCommand.setting("testText");
//		};
//		
//		assertThrows(ClassCastException.class, shouldThrowClassCastException);
//		
//		int goodObject = mockCommand.setting("testInt");
//		
//		assertEquals(2, goodObject);
//	}
	
}