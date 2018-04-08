package vendor.interfaces;

import vendor.objects.ParametersHelp;

public interface LinkableCommand {
	
	String[] getCalls();
	
	default String getDefaultCall(){
		String[] calls = getCalls();
		
		if(calls == null)
			return null;
		
		return calls[0];
	}
	
	default String getCommandDescription(){
		return null;
	}
	
	default ParametersHelp[] getParametersDescriptions(){
		return null;
	}
	
	default String getHelp(String textWhenParametersAvailable,
			String textHeader, String textWhenNoParameters){
		
		String commandDescription = getCommandDescription();
		ParametersHelp[] parametersHelp = getParametersDescriptions();
		
		StringBuilder builder = new StringBuilder();
		
		if(textHeader != null)
			builder.append(textHeader).append("\n");
		
		if(commandDescription != null){
			
			if(textHeader != null)
				builder.append("\t");
			
			builder.append(commandDescription);
			
		}
		
		if(parametersHelp == null){
			if(textWhenNoParameters != null)
				builder.append("\n\t").append(textWhenNoParameters);
		}
		else{
			
			String paramsSeparator = ", ";
			
			if(textWhenParametersAvailable != null)
				builder.append("\n").append(textWhenParametersAvailable);
			
			for(ParametersHelp paramHelp : parametersHelp){
				
				builder.append("\n").append("\t")
						.append(formatParameter(paramHelp.getParam()));
				
				for(String param : paramHelp.getParamVariants()){
					builder.append(paramsSeparator).append(formatParameter(param));
				}
				
				String paramHelpDescription = paramHelp
						.getParameterDescription();
				if(paramHelpDescription != null)
					builder.append(" : ").append(paramHelpDescription);
				
			}
			
		}
		
		return builder.toString();
		
	}
	
	default String formatParameter(String parameterToFormat){
		return "-" + parameterToFormat;
	}
	
}
