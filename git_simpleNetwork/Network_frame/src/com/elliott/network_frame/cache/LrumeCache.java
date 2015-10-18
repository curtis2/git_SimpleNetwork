package com.elliott.network_frame.cache;

import android.support.v4.util.LruCache;

import com.elliott.network_frame.base.Response;

/**
 * 网络框架缓存，将请求结果缓存到内存中
 * @author lenovo
 *
 */
public class LrumeCache implements Cache<String, Response>{

	/**
	 * 缓存response
	 */
	private LruCache< String, Response> mResponseCache;
	
	public LrumeCache() {
		// TODO Auto-generated constructor stub
		//计算最大可用内存
		int maxMemory=(int) (Runtime.getRuntime().maxMemory()/1024);
		//取最大可用内存的1/8作为可用内存
		int maxSize=maxMemory/8;
		
		mResponseCache=new LruCache<String, Response>(maxSize){
			@Override
			protected int sizeOf(String key, Response value) {
				// TODO Auto-generated method stub
				return value.rawData.length/1024;
			}
		};
	}
	@Override
	public Response get(String key) {
		// TODO Auto-generated method stub
		return mResponseCache.get(key);
	}

	@Override
	public void put(String key, Response value) {
		// TODO Auto-generated method stub
		mResponseCache.put(key, value);
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		mResponseCache.remove(key);
	}


}
