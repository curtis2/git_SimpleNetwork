package com.elliott.network_frame.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

/**
 * 网络框架的请求类
 * 
 * @author lenovo
 *
 */
public abstract class Request<T> implements Comparable<Request<T>> {

	/**
	 * 网络请求的类型
	 */
	public static enum HttpMethod {
		GET("GET"), POSE("POST"), PUT("PUT"), DELETE("DETETE");

		private String mHttpMethod = "";

		private HttpMethod(String method) {
			this.mHttpMethod = method;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return mHttpMethod;
		}
	}

	/**
	 * 优先级队列
	 * 
	 * @author lenovo
	 *
	 */
	public static enum Priority {
		LOW, NORMAL, HIGH, IMMEDIATE;
	}

	/**
	 * default-encoding
	 * 
	 */
	public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
	/**
	 * Content_Type
	 */
	public static final String HEADER_CONTENT_TYPE = "Content_Type";

	/**
	 * 请求序列号
	 */
	protected int mSerialNum = 0;

	/**
	 * 请求的优先级
	 */
	protected Priority mPriority = Priority.NORMAL;

	/**
	 * 是否取消
	 */
	protected boolean isCancel;

	/**
	 * 是否应该缓存
	 */
	private boolean mShouldCache = true;

	/**
	 * 请求的监听器
	 */
	protected RequestListener<T> mRequestListener;

	/**
	 * 请求的url
	 */
	private String mUrl = "";
	/**
	 * 请求的方法
	 */
	protected HttpMethod mHttpMethod = HttpMethod.GET;

	/**
	 * 请求的headr
	 */
	private Map<String, String> mHeaders = new HashMap<String, String>();
	/**
	 * 请求的参数
	 */
	private Map<String, String> mBodyParams = new HashMap<String, String>();

	public Request() {
		// TODO Auto-generated constructor stub
	}

	public Request(RequestListener<T> mRequestListener, String url,
			HttpMethod mhttHttpMethod) {
		super();
		this.mRequestListener = mRequestListener;
		this.mUrl = url;
		this.mHttpMethod = mhttHttpMethod;
	}

	/**
	 * 添加请求头
	 * 
	 * @param name
	 * @param value
	 */
	public void addHead(String name, String value) {
		mHeaders.put(name, value);
	}

	/**
	 * 从原生的网络请求中解析数据，子类复写
	 */
	public abstract T parseResponse(Response response);

	/**
	 * 分发请求的响应，执行在ui线程
	 * 
	 * @param response
	 */
	public final void deliveryResponse(Response response) {
		T result = parseResponse(response);
		if (mRequestListener != null) {
			int stCode = response != null ? response.getStatueCode() : -1;
			String errMsg = response != null ? response.getMessage()
					: "unknow error";
			Log.e("", "### 执行回调 : stCode = " + stCode + ", result : " + result
					+ ", err : " + errMsg);
			mRequestListener.onComplete(stCode, result, errMsg);
		}
	}

	public String getUrl() {
		return mUrl;
	}

	public RequestListener<T> getRequestListener() {
		return mRequestListener;
	}

	public int getSerialNumber() {
		return mSerialNum;
	}

	public void setSerialNumber(int mSerialNum) {
		this.mSerialNum = mSerialNum;
	}

	public Priority getPriority() {
		return mPriority;
	}

	public void setPriority(Priority mPriority) {
		this.mPriority = mPriority;
	}

	protected String getParamsEncoding() {
		return DEFAULT_PARAMS_ENCODING;
	}

	public String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset="
				+ getParamsEncoding();
	}

	public HttpMethod getHttpMethod() {
		return mHttpMethod;
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public Map<String, String> getParams() {
		return mBodyParams;
	}

	public boolean isHttps() {
		return mUrl.startsWith("https");
	}

	/**
	 * 返回post或者put的字节数组
	 * 
	 * @return
	 */
	public byte[] getBody() {
		Map<String, String> params = getParams();
		if (params != null && params.size() > 0) {
			return encondingParamsters(params, getParamsEncoding());
		}
		return null;
	}

	/**
	 * 转换参数
	 * 
	 * @param params
	 * @param paramsEncoding
	 * @return
	 */
	private byte[] encondingParamsters(Map<String, String> params,
			String paramsEncoding) {
		// TODO Auto-generated method stub
		StringBuilder encodeParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodeParams.append(URLEncoder.encode(entry.getKey(),
						paramsEncoding));
				encodeParams.append(URLEncoder.encode("=", paramsEncoding));
				encodeParams.append(URLEncoder.encode(entry.getValue(),
						paramsEncoding));
				encodeParams.append(URLEncoder.encode("&", paramsEncoding));
			}
			return encodeParams.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"encodeing not suppot " + paramsEncoding, e);
		}
	}

	@Override
	public int compareTo(Request<T> another) {
		// TODO Auto-generated method stub
		Priority mPriority = this.getPriority();
		Priority anPriority = another.getPriority();
		// 判断请求的优先级
		return mPriority.equals(anPriority) ? this.getSerialNumber()
				- another.getSerialNumber() : mPriority.ordinal()
				- anPriority.ordinal();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/**
	 * 该请求是否应该缓存
	 * 
	 * @param shouldCache
	 */
	public void setShouldCache(boolean shouldCache) {
		this.mShouldCache = shouldCache;
	}

	public boolean shouldCache() {
		return mShouldCache;
	}

	public void cancel() {
		isCancel = true;
	}

	public boolean isCanceled() {
		return isCancel;
	}

	/**
	 * 网络请求的监听器，执行在ui线程
	 * 
	 * @param response
	 *            请求的response类型
	 * @author lenovo
	 *
	 * @param <T>
	 */
	public static interface RequestListener<T> {
		/**
		 * 请求完成后回调
		 * 
		 * @param code
		 * @param response
		 * @param errMsg
		 */
		void onComplete(int stCode, T response, String errMsg);
	}

}
