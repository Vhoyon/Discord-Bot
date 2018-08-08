package vendor.interfaces;

public interface Loggable extends Outputtable {
	
	public void log(String logText, String logType, boolean hasAppendedDate);
	
}
