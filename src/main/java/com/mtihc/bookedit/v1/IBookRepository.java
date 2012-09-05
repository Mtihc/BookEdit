package com.mtihc.bookedit.v1;

import java.util.Set;

public interface IBookRepository<K, V extends Book> {

	public abstract V get(K key);

	public abstract void set(K key, V value);

	public abstract boolean has(K key);

	public abstract void remove(K key);
	
	public abstract Set<K> getKeys();

}