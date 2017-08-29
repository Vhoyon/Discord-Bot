package framework;

public class MusicPool extends Pool<Music> {

	public MusicPool(Music[] musics){
		super(musics);
	}
	
	public void addToQueue(String url){
		
		// Will prolly need to test for errors when creating a new music to add to queue
		add(new Music(url));
		
	}
	
	public void removeFromQueue(String associatedName){
		
		
		
	}
	
}

class Music {
	
	private String url;
	private String associatedName;
	
	public Music(String url){
		
		this.url = url;
		
	}
	
	@Override
	public boolean equals(Object obj){
		
		// TODO Redefine equals to test for associated name
		
		return super.equals(obj);
	}
	
}
