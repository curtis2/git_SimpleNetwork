package com.elliott.network_frame.httpstack;

import android.os.Build;

/**
 * http请求工厂类，用于构建http请求
 * 根据api版本选择httpClient或者 httpUrlConnection
 * @author lenovo
 */
public class HttpStackFactory {

	private final static  int GINGERBREEAD_NUM=9;
	/**
	 * @return
	 */
	public static HttpStack createHttpStack() {
		// TODO Auto-generated method stub
		int runtimeSDkApi=Build.VERSION.SDK_INT;
		if(runtimeSDkApi>=GINGERBREEAD_NUM){
			return new HttpUrlConnStack();
		}
		return new HttpClientStack();
	}

}
