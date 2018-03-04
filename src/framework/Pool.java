package framework;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Pool<E> extends ArrayList<E> {
	
	public Pool(E[] poolObjects){
		this.addAll(new ArrayList<>(Arrays.asList(poolObjects)));
	}
	
	@SuppressWarnings("unchecked")
	public E[] getPoolArray(){
		return (E[])this.toArray(new Object[this.size()]);
	}
	
}
