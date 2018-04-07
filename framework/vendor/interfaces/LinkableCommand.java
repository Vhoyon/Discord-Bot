package vendor.interfaces;

import vendor.objects.ParametersHelp;

public interface LinkableCommand {
	
	default String getCommandDescription(){
		return null;
	}

	default String getParameterPrefix(){
		return "-";
	}
	
	default ParametersHelp[] getParametersDescriptions(){
		return null;
	}
	
	default String getHelp(){
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(getCommandDescription()).append("\n");
		
		ParametersHelp[] parametersDescriptions = getParametersDescriptions();
		
		for(int i = 0; i < parametersDescriptions.length; i++){
			
		}
		
		return builder.toString();
		
	}
	
}
