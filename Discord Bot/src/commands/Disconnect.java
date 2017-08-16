package commands;

public class Disconnect extends Command {
	
	public Disconnect(){}
	
	@Override
	public void action(){
		
		getVoiceContext().JoinVoiceChannel();
		
	}
	
}
