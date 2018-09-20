package vendor.objects;

import vendor.interfaces.Auditable;
import vendor.modules.Logger;
import vendor.res.FrameworkResources;

import java.io.*;
import java.nio.file.Files;

public class AuditableFile implements Auditable {
	
	private File auditableFile;
	
	public AuditableFile(String fileName, String folder){
		this(folder + (folder.endsWith(File.separator) ? "" : File.separator)
				+ fileName);
	}
	
	public AuditableFile(String filePath){
		this.auditableFile = new File(filePath);
	}
	
	public File getFile(){
		return this.auditableFile;
	}
	
	@Override
	public void audit(String auditText, boolean hasAppendedDate,
			boolean shouldPrependAudit){
		
		File auditFile = getFile();
		
		try{
			
			PrintWriter writer = handleAuditHeader(auditFile);
			
			writer.println(auditText);
			
			writer.close();
			
		}
		catch(IllegalAccessError e){
			Logger.log("The specified audit file is not owned by the bot.",
					Logger.LogType.ERROR);
		}
		catch(IOException e){
			
			if(!auditFile.exists()){
				Logger.log(
						"The audit file does not exist and could not be created. Running the bot in admin mode might fix this.",
						Logger.LogType.ERROR);
			}
			else if(auditFile.isDirectory()){
				Logger.log(
						"The location given for the audit is a directory. Please go change the location in your code to point to a file.",
						Logger.LogType.ERROR);
			}
			else if(!Files.isWritable(auditFile.toPath())){
				Logger.log(
						"The file is not writable. Running the bot in admin mode might fix this.",
						Logger.LogType.ERROR);
			}
			else{
				Logger.log(
						"An unspecified error happened. Please try to run the bot again!",
						Logger.LogType.ERROR);
			}
			
		}
		
	}
	
	protected PrintWriter handleAuditHeader(File auditFile)
			throws IllegalAccessError, IOException{
		
		boolean shouldWriteHeader = false;
		
		// If file doesn't exist, create it with a header.
		if(!auditFile.exists()){
			shouldWriteHeader = true;
		}
		else{
			
			BufferedReader reader = new BufferedReader(
					new FileReader(auditFile));
			String header = reader.readLine();
			
			// If file exists but is empty, we should write header to it.
			if(header == null){
				shouldWriteHeader = true;
			}
			else if(!header.equals(FrameworkResources.AUDIT_HEADER)){
				throw new IllegalAccessError();
			}
			
		}
		
		PrintWriter writer = new PrintWriter(new FileWriter(auditFile, true));
		
		if(shouldWriteHeader){
			writer.println(FrameworkResources.AUDIT_HEADER + "\n");
		}
		
		return writer;
		
	}
	
}
