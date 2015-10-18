package com.elliott.network_frame.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * httpUrlConnection的一些配置信息
 * @author lenovo
 *
 */
public class HttpUrlConfig extends HttpConfig{

	private static HttpUrlConfig sConfig=new HttpUrlConfig();
	
	private SSLSocketFactory mssSslSocketFactory;
	private HostnameVerifier mhoHostnameVerifier;

	public static HttpUrlConfig getsConfig() {
		return sConfig;
	}
	/**
	 * 配置https请求的SSLSocketFactory与HostnameVerifier
	 * @param socketFactory
	 * @param hostnameVerifier
	 */
	public void setHttpUrlConfig(SSLSocketFactory socketFactory,HostnameVerifier hostnameVerifier){
		this.mssSslSocketFactory=socketFactory;
		this.mhoHostnameVerifier=hostnameVerifier;
	}
	
	public SSLSocketFactory getMssSslSocketFactory() {
		return mssSslSocketFactory;
	}
	
	public HostnameVerifier getMhoHostnameVerifier() {
		return mhoHostnameVerifier;
	}
	
}
