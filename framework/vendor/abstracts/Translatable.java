package vendor.abstracts;

import vendor.objects.Dictionary;

public abstract class Translatable implements vendor.interfaces.Translatable {
	
	private Dictionary dict;
	
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	public Dictionary getDictionary(){
		return this.dict;
	}
	
}
