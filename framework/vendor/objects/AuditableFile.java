package vendor.objects;

import vendor.exceptions.BadFormatException;
import vendor.interfaces.Auditable;
import vendor.modules.Logger;
import vendor.res.FrameworkResources;

import java.io.*;

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
		
		try{

			
			FileWriter writer = new FileWriter(getFile(), true);
			PrintWriter print_line = new PrintWriter(writer);
			if (getFile().exists()){
				FileReader reader = new FileReader(getFile());
				BufferedReader br = new BufferedReader(reader);
				String header =  br.readLine();
				if (header != null){
					if (!header.equals(FrameworkResources.AUDIT_HEADER)) {
						throw new BadFormatException();
					}
				}else{
					print_line.println(FrameworkResources.AUDIT_HEADER);
				}
				
			}
			print_line.println(auditText);
			
			print_line.close();
			
			
		}
		catch (BadFormatException e){
			Logger.log("The specified audit file is not owned by the bot.", Logger.LogType.ERROR);
		}
//		catch (FileNotFoundException e){
//			Logger.log("The audit file does not exist and could not be created. Try to run the bot in admin mode.",Logger.LogType.ERROR);
//		}
		catch(IOException e){
			if (!getFile().exists()){
				Logger.log("The audit file does not exist and could not be created. Try to run the bot in admin mode.",Logger.LogType.ERROR);
			}
			if(getFile().isDirectory()){
				Logger.log("The location given for the audit is a directory.", Logger.LogType.ERROR );
			}
			if (!getFile().canWrite()){
				Logger.log("The file is not writable.", Logger.LogType.ERROR );
			}
			
		}
		
	}
	
}
