
package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LRUAlgoCacheImpl<K,T> implements IAlgoCache <K,T> {
	private HashMap<K,T> ram;
	private LinkedList<K>keysOfPageInRam;
	private int capacity;

	public LRUAlgoCacheImpl(int capacity){
		ram = new LinkedHashMap<K,T>(capacity);
		keysOfPageInRam = new LinkedList<K>();
		this.capacity = capacity;
	}
	public T getElement(K key){                     //return value if exist or null if not
		if(ram.containsKey(key)){
			keysOfPageInRam.remove(key);       //if this page in the ram put the page at the beginning of ram
			keysOfPageInRam.addFirst(key);
			return ram.get(key);
		}
		else
			return null;	
	}
	public T putElement(K key,T value){       //return last value that was remove or null if any value wasn`t remove 
		T last = null;
		K lastKey;
		if(getElement(key)==null){            //if page not exist
			if(ram.size()>=capacity){      //if ram is full
				lastKey = keysOfPageInRam.getLast();
				last = ram.get(lastKey); 
				this.removeElement(lastKey);
			}
			ram.put(key, value);
			keysOfPageInRam.addFirst(key);
			return last;
		}
		else{
		return last;
		}
	}
	public void removeElement(K key){        // //remove  page that should exit
		ram.remove(key);
		keysOfPageInRam.remove(key);
	}
}
