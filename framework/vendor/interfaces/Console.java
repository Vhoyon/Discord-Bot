package vendor.interfaces;

public interface Console {
	
	enum QuestionType{
		YES_NO, YES_NO_CANCEL
	}

	int NO = 0;
	int YES = 1;
	int CANCEL = 2;

	void onStart() throws Exception;
	
	void onStop() throws Exception;
	
	void onInitialized();
	
	void initialize();
	
	String getInput(String message);
	
	int getConfirmation(String question, QuestionType questionType);
	
}
