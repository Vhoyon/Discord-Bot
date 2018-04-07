package vendor.objects;

public class ParametersHelp {
	
	private String parameterDescription;
	private String param;
	private String[] paramVariants;
	
	public ParametersHelp(String parameterDescription, String param,
			String... paramVariants){

		this.parameterDescription = parameterDescription;
		this.param = param;
		this.paramVariants = paramVariants;

	}
	
	public String getParameterDescription(){
		return this.parameterDescription;
	}
	
	public String getParam(){
		return this.param;
	}
	
	public String[] getParamVariants(){
		return this.paramVariants;
	}
}
