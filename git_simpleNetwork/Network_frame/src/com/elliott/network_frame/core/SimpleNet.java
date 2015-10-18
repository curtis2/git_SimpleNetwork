package com.elliott.network_frame.core;

import com.elliott.network_frame.httpstack.HttpStack;

/**
 * 框架入口
 * @author lenovo
 */

public final class SimpleNet {
	/**
	 * 构建一个请求队列，线程数为默认
	 * @return
	 */
	public static RequestQueue newRequestQueue(){
		return newRequestQueue(RequestQueue.DEAFULT_CORE_NUMS);
	}
	
	/**
	 * 构建一个指定线程数的队列
	 * @param coreNums
	 * @return
	 */
	public static RequestQueue newRequestQueue(int coreNums){
		return newRequestQueue(coreNums, null);
	}
	

	/**
	 * 构建一个指定线程数和http请求类的队列
	 * @param coreNums
	 * @param httpStack
	 * @return
	 */
	public static RequestQueue newRequestQueue(int coreNums, HttpStack httpStack) {
		// TODO Auto-generated method stub
		RequestQueue queue=new RequestQueue(Math.max(0, coreNums),httpStack );
		queue.start();
		return queue;
	}
}
