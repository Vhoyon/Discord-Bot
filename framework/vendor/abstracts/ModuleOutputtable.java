package vendor.abstracts;

import java.util.ArrayList;
import java.util.function.Consumer;

import vendor.interfaces.Outputtable;

public abstract class ModuleOutputtable extends Module {
	
	protected static <E extends Outputtable> boolean handleMessageAndWarning(String message, ArrayList<E> outputs, boolean hasIssuedWarning, Consumer<E> onOutputs){
		
		if((outputs == null || outputs.isEmpty()) && !hasIssuedWarning){
			hasIssuedWarning = true;
			
			System.err
					.println("[OUTPUT WARNING] The Logger hasn't had any outputs attached and a logging call has been made - using the System's output by default. This warning will only be shown once.\n");
		}
		
		if((outputs == null || outputs.isEmpty())){
			System.out.println(message);
		}
		else{
			for(E output : outputs){
				onOutputs.accept(output);
			}
		}
		
		return hasIssuedWarning;
		
	}
	
}