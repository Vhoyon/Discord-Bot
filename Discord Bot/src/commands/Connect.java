package commands;

public class Connect extends Command {
	
	@Override
	public void action(){
		
		getVoiceContext().JoinVoiceChannel();
		
	}
	
}
