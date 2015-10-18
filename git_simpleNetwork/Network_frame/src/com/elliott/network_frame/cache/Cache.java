package com.elliott.network_frame.cache;

/**
 * 网络请求的缓存接口
 * @author lenovo
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K,V> {

	public V get(K key);
	
	public void put(K key, V value);
	
	public void remove(K key);
}
