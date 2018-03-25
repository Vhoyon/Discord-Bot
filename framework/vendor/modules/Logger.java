package vendor.modules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import vendor.abstracts.Module;
import vendor.interfaces.Loggable;

public class Logger extends Module {
	
	public static enum LogType{
		INFO, WARNING, ERROR
	}
	
	private static ArrayList<Loggable> outputs;
	
	private static boolean hasIssuedWarning;
	
	private static String separator;
	
	@Override
	public void build() throws Exception{
		outputs = new ArrayList<>();
		hasIssuedWarning = false;
		separator = "-";
	}
	
	public static void setSeparator(String newSeparator){
		separator = newSeparator;
	}
	
	public static void setOutputs(Loggable... outputs){
		Logger.outputs = new ArrayList<>(Arrays.asList(outputs));
	}
	
	public static boolean addOutput(Loggable output){
		
		if(outputs.contains(output)){
			return false;
		}
		
		return outputs.add(output);
		
	}
	
	public static boolean removeOutput(Loggable output){
		return outputs.remove(output);
	}
	
	public static Loggable removeOutput(int index){
		try{
			return outputs.remove(index);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public static void log(String message){
		log(message, null, true);
	}
	
	public static void log(String message, LogType logType){
		log(message, logType, true);
	}
	
	public static void log(String message, boolean appendDate){
		log(message, null, appendDate);
	}
	
	public static void log(String message, LogType logType, boolean appendDate){
		
		if(outputs.isEmpty() && !hasIssuedWarning){
			hasIssuedWarning = true;
			
			System.out
					.println("[LOGGER WARNING] The Logger hasn't had any outputs attached and a logging call has been made - using the System's output by default. This warning will only be shown once.\n");
		}
		
		StringBuilder builder = new StringBuilder();
		
		boolean hasAddedPrefix = false;
		
		if(appendDate){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			builder.append("[").append(dateFormat.format(date)).append("] ");
			
			hasAddedPrefix = true;
		}
		
		if(logType != null){
			builder.append("[").append(logType).append("] ");
			
			hasAddedPrefix = true;
		}
		
		if(hasAddedPrefix){
			builder.append(separator).append(" ");
		}
		
		builder.append(message);
		
		String logText = builder.toString();
		
		if(outputs == null){
			System.out.println(logText);
		}
		else{
			for(Loggable loggable : outputs){
				loggable.log(logText);
			}
		}
		
	}
	
}
