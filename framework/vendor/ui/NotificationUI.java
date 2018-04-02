package vendor.ui;

import vendor.abstracts.AbstractUIConsole;
import vendor.modules.Logger;
import vendor.objects.NotificationLogger;


public class NotificationUI extends AbstractUIConsole {

	private NotificationLogger logger;

	@Override
	public void initialize(){

		logger = new NotificationLogger(this);

		Logger.setOutputs(logger);

	}

}
