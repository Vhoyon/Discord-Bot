package commands;

public class Disconnect extends Command {
	
	@Override
	public void action(){
		
		getVoiceContext().JoinVoiceChannel();
		
	}
	
}
