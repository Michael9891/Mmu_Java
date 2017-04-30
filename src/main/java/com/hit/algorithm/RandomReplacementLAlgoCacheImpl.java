
package com.hit.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomReplacementLAlgoCacheImpl<K,T> implements IAlgoCache <K,T>{
	private HashMap<K,T> ram;
	private ArrayList<K> keyOfPageInRam;
	private int capacity;
	
	public RandomReplacementLAlgoCacheImpl(int capacity){    
		ram = new HashMap<K,T>();
		keyOfPageInRam = new ArrayList<K>();
		this.capacity = capacity;
	}
    public T getElement(K key){          //return value if exist else return null
    	if(ram.containsKey(key)){
			return ram.get(key);
		}
		else
			return null;
	}
	public T putElement(K key,T value){           //return null if there is free place or if exist in ram else return value that exit from ram
		Random rand = new Random();
		T randValRemove = null;
		K exitKeyRand;
		int randKey;
		if(!ram.containsKey(key)){                      //if page not exist
			if(keyOfPageInRam.size()>=capacity){                  //if ram is full
				randKey = rand.nextInt(keyOfPageInRam.size());
				exitKeyRand = keyOfPageInRam.get(randKey);            //remove random page
				randValRemove = ram.get(exitKeyRand);      
				keyOfPageInRam.remove(randKey);
			    removeElement(exitKeyRand);
			}
			keyOfPageInRam.add(key);
			ram.put(key, value);
			return randValRemove;        
		}
		
		return randValRemove;
	}
	public void removeElement(K key){
		ram.remove(key);
	}
}
