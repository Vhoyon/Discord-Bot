package vendor.modules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import vendor.abstracts.Module;

public class Logger extends Module {
	
	private static JTextArea textArea;
	
	@Override
	public void build() throws Exception{}
	
	public static void setTextArea(JTextArea textAreaToLogIn){
		textArea = textAreaToLogIn;
	}
	
	public static void log(String message){
		log(message, true);
	}
	
	public static void log(String message, boolean appendDate){
		
		if(textArea == null){
			new Exception("The logger hasn't been attached to a JTextArea yet!")
					.printStackTrace();
			
			return;
		}
		
		if(appendDate){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			textArea.append("[" + dateFormat.format(date) + "] - ");
		}
		//		else{
		//			textArea.append("\n");
		//		}
		
		textArea.append(message + "\n\n");
		
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
}
