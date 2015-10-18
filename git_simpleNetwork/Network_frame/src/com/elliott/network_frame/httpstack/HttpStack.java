package com.elliott.network_frame.httpstack;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Response;

/**
 * 执行网络请求的接口
 * @author lenovo
 *
 */
public interface  HttpStack {

	/**
	 * 执行http请求
	 * @param request
	 * @return
	 */
	public Response performRequest(Request<?> request);
	
}
