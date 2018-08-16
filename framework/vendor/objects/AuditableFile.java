package vendor.objects;

import vendor.interfaces.Auditable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
			
			print_line.println(auditText);
			
			print_line.close();
			
		}
		catch(IOException e){}
		
	}
	
}
