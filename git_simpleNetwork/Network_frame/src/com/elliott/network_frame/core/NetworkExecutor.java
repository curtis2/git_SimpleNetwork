package com.elliott.network_frame.core;

import java.util.concurrent.BlockingQueue;

import android.util.Log;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Response;
import com.elliott.network_frame.cache.Cache;
import com.elliott.network_frame.cache.LrumeCache;
import com.elliott.network_frame.httpstack.HttpStack;

/**
 * 网络请求excutor，从请求队列中循环取出请求执行
 * @author lenovo
 *
 */
public class NetworkExecutor extends Thread{

	
	/**
	 * 网络请求队列
	 */
	private BlockingQueue<Request<?>> mRequestQueue;
	/**
	 * 网络请求栈
	 */
	private HttpStack mHttpStack;
	
	/**
	 * 请求结果分发器
	 */
	private static ResponseDelivery mResponseDelivery=new ResponseDelivery();
	/**
	 * 请求结果的缓存
	 */
	private static Cache<String, Response> mRequestCache=new LrumeCache();
	
	/**
	 * 是否停止
	 */
	private boolean isStop=false;
	
	
	public NetworkExecutor(BlockingQueue<Request<?>> mRequestQueue,
			HttpStack mHttpStack) {
		this.mRequestQueue = mRequestQueue;
		this.mHttpStack = mHttpStack;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isStop) {
			try {
				final Request<?> request=mRequestQueue.take();
				if(request.isCanceled()){
					   Log.d("### ", "### 取消执行了"); 
					   continue;
				}
				Response response=null;
				if(isUseCache(request)){
					//从缓存中获取数据
					response=mRequestCache.get(request.getUrl());
				}else{
					//请求网络
					response=mHttpStack.performRequest(request);
					
					//是否缓存网络请求
					if(request.shouldCache()&&isSuccess(response)){
					  mRequestCache.put(request.getUrl(), response);
					}
				}
				
			mResponseDelivery.deliveryResponse(request, response);
			} catch (InterruptedException e) {
				// TODO: handle exception
	            Log.i("", "### 请求分发器退出");  
			}
		}
	}


	
	private boolean isSuccess(Response response) {
		// TODO Auto-generated method stub
		return response!=null&&response.getStatueCode()==200;
	}


	private boolean isUseCache(Request<?> request) {
		// TODO Auto-generated method stub
		return request.shouldCache()&&mRequestCache.get(request.getUrl())!=null;
	}
	public void quit(){
		isStop=true;
		interrupt();
	}
}
