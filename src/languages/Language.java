package languages;

public class Language {
	
	private String lang;
	private String country;
	
	private String[] friendlyNames;
	
	public Language(String lang, String country, String... friendlyNames){
		this.lang = lang;
		this.country = country;
		this.friendlyNames = friendlyNames;
	}
	
	public String getLang(){
		return lang;
	}
	
	public void setLang(String lang){
		this.lang = lang;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public String[] getFriendlyNames(){
		return friendlyNames;
	}
	
	public void setFriendlyNames(String[] friendlyNames){
		this.friendlyNames = friendlyNames;
	}
	
	private String getFriendlyNameAt(int position){
		return getFriendlyNames()[position];
	}
	
	@Override
	public boolean equals(Object obj){
		
		boolean isEqual = false;
		
		String friendlyName = obj.toString();
		
		if(friendlyName != null)
			for(int i = 0; !isEqual && i < getFriendlyNames().length; i++){
				
				if(friendlyName.equals(getFriendlyNameAt(i)))
					isEqual = true;
				
			}
		
		return isEqual;
	}
	
}