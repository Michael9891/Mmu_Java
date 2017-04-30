package com.hit.algorithm;
public interface IAlgoCache <K,T> {

	public T getElement(K key);
	public T putElement(K key,T value);
	public void removeElement(K key);
}
