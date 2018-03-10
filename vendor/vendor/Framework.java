package vendor;

import abstracts.FrameworkModule;

public class Framework {
	
	private Framework(){}
	
	private static String[] modules =
	{
		"Environment"
	};
	
	public static void build() throws Exception{
		
		StringBuilder errors = new StringBuilder();
		
		for(String moduleName : modules){
			
			try{
				String formattedModuleName = moduleName.replaceAll("/", ".");
				
				Class<?> moduleClass = Class.forName("vendor.modules."
						+ formattedModuleName);
				
				if(FrameworkModule.class.equals(moduleClass.getSuperclass())){
					
					FrameworkModule module = (FrameworkModule)moduleClass
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
				errors.append(e.getMessage() + "\n");
			}
			
		}
		
		if(errors.length() != 0){
			throw new Exception(errors.toString());
		}
		
	}
	
}
