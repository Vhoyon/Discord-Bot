package commands;

import errorHandling.exceptions.NoParameterContentException;
import framework.Command;

public class CommandTimer extends Command {

	private int[] timeRef = new int[3];
	private boolean isAlive = true;

	@Override
	public void action() {

		String[] constraints = getSplitContent();
		int seconds = 0;
		int hours = 0;
		int minutes = 0;

		try {
			// seconds = Integer.parseInt(constraints[0]);
			if (isParameterPresent("h")) {
				hours = Integer.parseInt(getParameter("h").getParameterContent());
			}
			if (isParameterPresent("m")) {
				minutes = Integer.parseInt(getParameter("m").getParameterContent());
			}
			if (isParameterPresent("s")) {
				seconds = Integer.parseInt(getParameter("s").getParameterContent());
			}

		} catch (NullPointerException e) {
			sendMessage(
					"You must give an amount of time to the " + buildVCommand("timer") + " command for it too count");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoParameterContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int totalTime = (hours * 3600) + (minutes * 60) + seconds;
		String lastmsgId = null;
		String editedMessage = null;
		// long totalTime = (seconds * 1000);
		// long temps = System.currentTimeMillis();
		sendMessage(hours + "hours " + minutes + " minutes " + seconds + " seconds");
		lastmsgId = (String) getBuffer().get(BUFFER_LASTMSGID);

		for (int i = totalTime; i >= 0 && isAlive; i--) {

			timeConstruct(i);
			editMessage(timeRef[0] + "hours " + timeRef[1] + " minutes " + timeRef[2] + " seconds", lastmsgId);
			if (i == 0) {
				sendMessage("TimerEnded");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

	}

	private void timeConstruct(int remainingTime) {
		
		if (remainingTime / 3600 >= 0) {
			timeRef[0] = remainingTime / 3600;
			remainingTime -= timeRef[0] * 3600;
		}else
			timeRef[0] = 0;
		if (remainingTime / 60 >= 0) {
			timeRef[1] = remainingTime / 60;
			remainingTime -= timeRef[1] * 60;
		}else
			timeRef[1] = 0;
		if (remainingTime >= 0) {
			timeRef[2] = remainingTime;
		}

	}

	@Override
	public boolean stopAction() {
		isAlive = false;
		return true;
	}

}
