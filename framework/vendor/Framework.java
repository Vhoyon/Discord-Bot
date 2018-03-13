package vendor;

import vendor.abstracts.Module;

public class Framework {
	
	private Framework(){}
	
	private static String[] modules =
	{
		"Environment",
		"Logger"
	};
	
	public static void build() throws Exception{
		
		StringBuilder errors = new StringBuilder();
		
		for(String moduleName : modules){
			
			try{
				String formattedModuleName = moduleName.replaceAll("/", ".");
				
				Class<?> moduleClass = Class.forName("vendor.modules."
						+ formattedModuleName);
				
				if(Module.class.equals(moduleClass.getSuperclass())){
					
					Module module = (Module)moduleClass
							.newInstance();
					
					try{
						module.build();
					}
					catch(Exception e){
						
						errors.append(module.getLoadingErrorMessage(e) + "\n");
						
					}
					
				}
				
			}
			catch(ClassNotFoundException | InstantiationException
					| IllegalAccessException e){
				errors.append("Module \"" + e.getMessage() + "\" not found." + "\n");
			}
			
		}
		
		if(errors.length() != 0){
			throw new Exception(errors.toString());
		}
		
	}
	
}
