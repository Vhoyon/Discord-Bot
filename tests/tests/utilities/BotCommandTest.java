package tests.utilities;

import app.CommandRouter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import test.utils.MockFactory;
import utilities.BotCommand;
import vendor.objects.Request;
import vendor.utilities.settings.IntegerField;
import vendor.utilities.settings.Setting;
import vendor.utilities.settings.SettingField;
import vendor.utilities.settings.TextField;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

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
		
		Request mockRequest = MockFactory.createRequest('-');
		
		doReturn(mockRequest).when(mockCommand).getRequest();
		
		String expected = "`--" + testParameterName + "`";
		
		assertEquals(expected, mockCommand.formatParameter(testParameterName));
	}
	
	@Test
	void testUsageStringCreation(){
		String testCommandName = "test";
		String prefix = "!!";
		
		doReturn(testCommandName).when(mockCommand).getCommandName();
		doReturn(prefix + testCommandName).when(mockCommand).buildCommand(
				testCommandName);
		
		String expected = "`" + prefix + testCommandName + "`";
		
		assertEquals(expected, mockCommand.getUsage());
	}
	
	@Test
	void testGetSettingsNotChanged(){
		String defValue = "defValue";
		
		SettingField testField = new TextField("test", "ENV_TEST", defValue);
		Setting settings = new Setting(testField);
		
		CommandRouter mockRouter = MockFactory.createRouter(settings);
		
		doReturn(mockRouter).when(mockCommand).getRouter();
		
		assertEquals(defValue, mockCommand.setting("test"));
	}
	
	@Test
	void testGetSettingsChanged(){
		SettingField testField = new TextField("test", "ENV_TEST", "testValue");
		Setting settings = new Setting(testField);
		
		CommandRouter mockRouter = MockFactory.createRouter(settings);
		
		doReturn(mockRouter).when(mockCommand).getRouter();
		
		String newValue = "modifiedValue";
		
		mockCommand.setSetting("test", newValue);
		
		assertEquals(newValue, mockCommand.setting("test"));
	}
	
	@Test
	void testGetSettingsChangedCallback(){
		SettingField testField = new TextField("test", "ENV_TEST", "testValue");
		Setting settings = new Setting(testField);
		
		CommandRouter mockRouter = MockFactory.createRouter(settings);
		
		doReturn(mockRouter).when(mockCommand).getRouter();
		
		AtomicReference<String> callbackedModifiedValue = new AtomicReference<>(
				null);
		
		String newValue = "modifiedValue";
		
		mockCommand.setSetting("test", newValue,
				(modifiedValue) -> callbackedModifiedValue
						.set((String)modifiedValue));
		
		assertEquals(newValue, callbackedModifiedValue.get());
	}
	
	@Test
	void testGetSettingsAutoCast(){
		SettingField testFieldText = new TextField("testText", "ENV_TEST_TEXT",
				"testValue");
		SettingField testFieldInt = new IntegerField("testInt", "ENV_TEST_INT",
				2);
		Setting settings = new Setting(testFieldText, testFieldInt);
		
		CommandRouter mockRouter = MockFactory.createRouter(settings);
		
		doReturn(mockRouter).when(mockCommand).getRouter();
		
		Executable shouldThrowClassCastException = () -> {
			int badObject = mockCommand.setting("testText");
		};
		
		assertThrows(ClassCastException.class, shouldThrowClassCastException);
		
		int goodObject = mockCommand.setting("testInt");
		
		assertEquals(2, goodObject);
	}
	
}