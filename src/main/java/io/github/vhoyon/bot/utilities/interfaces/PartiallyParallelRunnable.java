package io.github.vhoyon.bot.utilities.interfaces;

import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;

public interface PartiallyParallelRunnable {
	
	boolean duplicatedRunnableCondition(AbstractBotCommand thisCommand,
			AbstractBotCommand runningCommand);
	
}
