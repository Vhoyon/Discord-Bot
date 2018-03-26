package vendor.modules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import vendor.abstracts.Module;
import vendor.interfaces.Loggable;

public class Logger extends Module {
	
	/**
	 * The type of logging to prepend to the log message.
	 * <p>
	 * Take note that will be added to the message is literally the enum's
	 * value.
	 */
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
	
	/**
	 * Sets the separator for logs that has a prefix before the message.
	 * <p>
	 * Set to <code>null</code> if you don't want any separator.
	 * 
	 * @param newSeparator
	 */
	public static void setSeparator(String newSeparator){
		separator = newSeparator;
	}
	
	/**
	 * Overwrite the output list with those passed as parameters.
	 * 
	 * @param outputs
	 *            Objects implementing the {@link vendor.interfaces.Loggable
	 *            Loggable} interface which are meant to receive logs from the
	 *            Logger module.
	 */
	public static void setOutputs(Loggable... outputs){
		if(outputs != null)
			Logger.outputs = new ArrayList<>(Arrays.asList(outputs));
	}
	
	/**
	 * Adds an output to the already existing output list for the Logger module.
	 * 
	 * @param output
	 *            Object implementing the {@link vendor.interfaces.Loggable
	 *            Loggable} interface which is meant to receive logs from the
	 *            Logger module.
	 * @return <code>true</code> if the output was added successfully,
	 *         <code>false</code> if the output is already in the outputs list.
	 */
	public static boolean addOutput(Loggable output){
		
		if(outputs.contains(output)){
			return false;
		}
		
		return outputs.add(output);
		
	}
	
	/**
	 * Adds multiple outputs to the already existing output list for the Logger
	 * module.
	 * 
	 * @param outputs
	 *            Objects implementing the {@link vendor.interfaces.Loggable
	 *            Loggable} interface which are meant to receive logs from the
	 *            Logger module.
	 */
	public static void addOutputs(Loggable... outputs){
		for(Loggable output : outputs){
			Logger.addOutput(output);
		}
	}
	
	/**
	 * Removes an output from the output list for the Logger module.
	 * 
	 * @param output
	 *            Object implementing the {@link vendor.interfaces.Loggable
	 *            Loggable} interface.
	 * @return <code>true</code> if the output was in the list and has been
	 *         removed, <code>false</code> if the output is not in the list.
	 */
	public static boolean removeOutput(Loggable output){
		return outputs.remove(output);
	}
	
	/**
	 * Removes an output from the output list by it's index for the Logger
	 * module.
	 * 
	 * @param index
	 *            The position of the output in the internal list.
	 * @return The {@link vendor.interfaces.Loggable Loggable} object found at
	 *         this position, or <code>null</code> if the index is out of
	 *         bounds.
	 */
	public static Loggable removeOutput(int index){
		try{
			return outputs.remove(index);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public static void log(Exception e){
		log(e.getMessage(), LogType.ERROR);
	}
	
	public static void log(String message){
		log(message, (String)null, true);
	}
	
	public static void log(String message, LogType logType){
		log(message, logType, true);
	}
	
	public static void log(String message, boolean appendDate){
		log(message, (String)null, appendDate);
	}
	
	public static void log(String message, LogType logType, boolean appendDate){
		log(message, logType.toString(), appendDate);
	}
	
	public static void log(String message, String logType, boolean appendDate){
		
		if(outputs.isEmpty() && !hasIssuedWarning){
			hasIssuedWarning = true;
			
			System.err
					.println("[LOGGER WARNING] The Logger hasn't had any outputs attached and a logging call has been made - using the System's output by default. This warning will only be shown once.\n");
		}
		
		StringBuilder builder = new StringBuilder();
		
		boolean hasAddedPrefix = false;
		
		if(appendDate){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			builder.append("[").append(dateFormat.format(date)).append("]");
			
			hasAddedPrefix = true;
		}
		
		if(logType != null){
			builder.append("[").append(logType).append("]");
			
			hasAddedPrefix = true;
		}
		
		if(hasAddedPrefix){
			
			if(separator != null){
				builder.append(" ").append(separator);
			}
			
			builder.append(" ");
			
		}
		
		builder.append(message.replaceFirst("^\\s+", ""));
		
		String logText = builder.toString();
		
		if(outputs.size() == 0){
			System.out.println(logText);
		}
		else{
			for(Loggable output : outputs){
				output.log(logText, logType, appendDate);
			}
		}
		
	}
	
}
