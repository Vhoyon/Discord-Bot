package commands;

public class Connect extends Command {
	
	public Connect(){}
	
	@Override
	public void action(){
		
		getVoiceContext().JoinVoiceChannel();
		
	}
	
}
