package utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class BotCommandTest {
	
	BotCommand mockCommand;
	
	@BeforeEach
	void setUp(){
		mockCommand = mock(BotCommand.class);
	}
	
	@AfterEach
	void tearDown(){
		mockCommand = null;
	}
	
	@Test
	void getRouter(){
		fail("Testing failures in Travis...");
	}
	
	@Test
	void formatParameter(){
		
	}
	
	@Test
	void getUsage(){
		
	}
	
	@Test
	void setting(){
		
	}
	
	@Test
	void setSetting(){
		
	}
	
	@Test
	void setSetting1(){
		
	}
	
}