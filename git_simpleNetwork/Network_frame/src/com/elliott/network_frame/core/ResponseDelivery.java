package com.elliott.network_frame.core;

import java.util.concurrent.Executor;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Response;

import android.os.Handler;
import android.os.Looper;

/**
 * 请求结果投递类，将请求结果投递到ui线程
 * @author lenovo
 *
 */
public class ResponseDelivery implements Executor{

	/**
	 * 主线程的handler
	 */
	Handler mResponseHanlder=new Handler(Looper.getMainLooper());
	
	/**
	 * 分发响应
	 * @param request
	 * @param response
	 */
	public void deliveryResponse(final Request<?> request,final Response response){
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
               request.deliveryResponse(response);			
			}
		};
		execute(runnable);
	}
	@Override
	public void execute(Runnable command) {
		// TODO Auto-generated method stub
		mResponseHanlder.post(command);
	}

}
