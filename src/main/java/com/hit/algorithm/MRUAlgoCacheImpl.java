
package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MRUAlgoCacheImpl<K,T> implements IAlgoCache <K,T> {
	private HashMap<K,T> memory;
	private int capacity;
	private LinkedList<K>keysOfMemory;
	
	public MRUAlgoCacheImpl(int capacity){
		memory = new LinkedHashMap<K,T>(capacity);
		keysOfMemory = new LinkedList<K>();
		this.capacity = capacity;
	}
    public T getElement(K key){
    	if(keysOfMemory.contains(key)){
    		keysOfMemory.remove(key);       //if this page in the ram put the page at the beginning of ram
    		keysOfMemory.addFirst(key);
			return memory.get(key);
		}
		else
			return null;
	}
	public T putElement(K key,T value){
		T first = null;
		K exitFirstKey;
		if(getElement(key)==null){            //if page not exist
			if(keysOfMemory.size()>=capacity){      //if ram is full
				exitFirstKey = keysOfMemory.getFirst();       //exit first page and 
				first = memory.get(exitFirstKey); 
				this.removeElement(exitFirstKey);
			}
			memory.put(key, value);                 //add new page
			keysOfMemory.addFirst(key);
			return first;
		}
		else{
		keysOfMemory.remove(key);
		keysOfMemory.addFirst(key);
		return first;
		}
	}
	public void removeElement(K key){               //remove  page that should exit
		memory.remove(key);
		keysOfMemory.remove(key);
	}

}
