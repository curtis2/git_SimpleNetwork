package com.elliott.network_frame.base;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
/**
 * 请求结果类，将响应结果放到rawDate中
 * @author lenovo
 *
 */
public class Response extends BasicHttpResponse {

	public byte[] rawData = new byte[0];

	public Response(StatusLine statusLine) {
		// TODO Auto-generated constructor stub
		super(statusLine);
	}

	public Response(ProtocolVersion ver, int code, String reason) {
		// TODO Auto-generated constructor stub
		super(ver, code, reason);
	}

	@Override
	public void setEntity(HttpEntity entity) {
		// TODO Auto-generated method stub
		super.setEntity(entity);
		rawData=entitytoBytes(entity);
	}
	
	public byte[] getRawDate(){
		return rawData;
	}
	
	public String getMessage(){
		return getStatusLine().getReasonPhrase();
	}
	public int getStatueCode(){
		return getStatusLine().getStatusCode();
	}
	
	/**
	 * reads the content of entity into a bytes
	 * @param entity
	 * @return
	 */
	private byte[] entitytoBytes(HttpEntity entity){
		try {
			return EntityUtils.toByteArray(entity);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new byte[0];
	}
}
