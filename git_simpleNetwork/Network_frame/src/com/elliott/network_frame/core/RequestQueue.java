package com.elliott.network_frame.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Log;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.httpstack.HttpStack;
import com.elliott.network_frame.httpstack.HttpStackFactory;

/**
 * 请求的队列，使用优先级队列，使得请求可以根据优先级进行处理
 * @author lenovo
 *
 */
public final class RequestQueue {

	/**
	 * 请求的优先级队列，使得请求可以根据优先级处理
	 */
	private BlockingQueue<Request<?>> mRequestQueue=new PriorityBlockingQueue<Request<?>>();
	
	/**
	 * 请求的序列号生成器
	 */
	private AtomicInteger mSerialNumGenerator=new AtomicInteger(0);
	
	/**
	 * 默认的线程数量
	 */
	public static int DEAFULT_CORE_NUMS=Runtime.getRuntime().availableProcessors()+1;
	
	/**
	 * 处理器的数量
	 */
	private int mDispatherNums=DEAFULT_CORE_NUMS;
	
	/**
	 * 请求真正的处理器
	 */
	private NetworkExecutor[] mDispathers=null;
	
	/**
	 * 网络请求类
	 */
	private HttpStack mHttpStack;

	public RequestQueue(int mDispatherNums, HttpStack mHttpsStack) {
		super();
		this.mDispatherNums = mDispatherNums;
		this.mHttpStack = mHttpsStack!=null?mHttpsStack:HttpStackFactory.createHttpStack();
	}
	
	private final void startNewWorkExector(){
		mDispathers=new NetworkExecutor[mDispatherNums];
		for (int i = 0; i < mDispatherNums; i++) {
			mDispathers[i]=new NetworkExecutor(mRequestQueue, mHttpStack);
			mDispathers[i].start();
		}
	}
	
	public void start(){
		stop();
		startNewWorkExector();
	}

	private void stop() {
		// TODO Auto-generated method stub
		if(mDispathers!=null&&mDispathers.length>0){
			for (int i = 0; i < mDispathers.length; i++) {
				mDispathers[i].quit();
			}
		}
	}
	
	/**
	 * 添加请求，不能重复添加请求
	 * @param request
	 */
	public void addRequest(Request<?> request){
		if(!mRequestQueue.contains(request)){
			request.setSerialNumber(generatorSerialNum());
		   mRequestQueue.add(request);
		}else{
			Log.d("", "### 请求队列中已经含有");
		}
		
	}

	/**
	 * 清空请求队列
	 */
	public void clear(){
		mRequestQueue.clear();
	}
	
	
	public BlockingQueue<Request<?>> getAllRequests(){
		return mRequestQueue;
	}
	/**
	 * 生成请求的序列号
	 * @return
	 */
	private int generatorSerialNum() {
		// TODO Auto-generated method stub
		return mSerialNumGenerator.incrementAndGet();
	}
	
	
}
