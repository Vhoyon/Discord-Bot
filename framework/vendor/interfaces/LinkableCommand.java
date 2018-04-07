package vendor.interfaces;

public interface LinkableCommand {

    default String getHelpString(){
        return null;
    }

    default String[][] getParametersHelpStrings(){
        return null;
    }
	
}
