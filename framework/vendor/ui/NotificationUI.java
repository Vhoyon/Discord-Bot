package vendor.ui;

import vendor.abstracts.AbstractUIConsole;
import vendor.interfaces.Loggable;
import vendor.modules.Logger;

import javax.swing.*;

public class NotificationUI extends AbstractUIConsole {
	
	@Override
	public void initialize(){
		Logger.setOutputs(new NotificationLogger(this));
	}
	
	@Override
	public int getConfirmation(String question, QuestionType questionType){
		return super.getConfirmation(question, questionType);
	}
	
	private class NotificationLogger implements Loggable {
		
		private JFrame frame;
		
		public NotificationLogger(JFrame frame){
			this.frame = frame;
		}
		
		@Override
		public void log(String logText, String logType, boolean hasAppendedDate){
			JOptionPane.showMessageDialog(frame, logText);
		}
	}
	
}
