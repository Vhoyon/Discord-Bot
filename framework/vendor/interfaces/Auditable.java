package vendor.interfaces;

public interface Auditable extends Outputtable {

	public void audit(String auditText, boolean hasAppendedDate, boolean shouldPrependAudit);
	
}
