package vendor.utilities.settings;

import java.util.Collections;

public class EnumSortedField extends EnumField {
	
	public EnumSortedField(String name, String env,
		Object defaultValue, Object... otherValues){
		super(name, env, defaultValue, otherValues);
	}
	
	public ArrayList<String> getPossibleValues(){
		return Collections.sort(super.getPossibleValues());
	}
	
}
