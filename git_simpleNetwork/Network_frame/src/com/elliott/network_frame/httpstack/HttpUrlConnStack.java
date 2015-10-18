package com.elliott.network_frame.httpstack;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Request.HttpMethod;
import com.elliott.network_frame.base.Response;
import com.elliott.network_frame.config.HttpUrlConfig;

/**
 * api为9以上时，使用httpurlconnection
 * 
 * @author lenovo
 *
 */
public class HttpUrlConnStack implements HttpStack {

	/**
	 * 执行http请求时的一些配置
	 */
	HttpUrlConfig config = HttpUrlConfig.getsConfig();

	@Override
	public Response performRequest(Request<?> request) {
		// TODO Auto-generated method stub
		HttpURLConnection urlConnection = null;
		try {
			// 构建urlConnection
			urlConnection = createUrlConnetion(request.getUrl());
			// set headers
			setRequestHeaders(urlConnection, request);
			// set params
			setRequestParams(urlConnection, request);

			// 配置https
			configHttps(request);
			// 提取请求
			return fatchResponse(urlConnection);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	private Response fatchResponse(HttpURLConnection urlConnection)
			throws IOException {
		// TODO Auto-generated method stub
		// Initialize HttpResponse with data from the HttpURLConnection.
		ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
		int responseCode = urlConnection.getResponseCode();
		if (responseCode == -1) {
			throw new IOException(
					"Could not retrieve response code from HttpUrlConnection.");
		}
		// 状态行数据
		StatusLine responseStatus = new BasicStatusLine(protocolVersion,
				urlConnection.getResponseCode(),
				urlConnection.getResponseMessage());
		// 构建response
		Response response = new Response(responseStatus);
		// 设置response数据
		response.setEntity(entityFromURLConnwction(urlConnection));
		addHeadersToResponse(response, urlConnection);
		return response;
	}

	private void configHttps(Request<?> request) {
		// TODO Auto-generated method stub
		if (request.isHttps()) {
			SSLSocketFactory mssSslSocketFactory = config
					.getMssSslSocketFactory();
			if (mssSslSocketFactory != null) {
				HttpsURLConnection
						.setDefaultSSLSocketFactory(mssSslSocketFactory);
				HttpsURLConnection.setDefaultHostnameVerifier(config
						.getMhoHostnameVerifier());
			}
		}

	}

	private void setRequestParams(HttpURLConnection urlConnection,
			Request<?> request) throws ProtocolException, IOException {
		// TODO Auto-generated method stub
		HttpMethod httpMethod = request.getHttpMethod();
		urlConnection.setRequestMethod(httpMethod.toString());
		byte[] body = request.getBody();
		if (body != null) {
			urlConnection.setDoInput(true);
			urlConnection.addRequestProperty(request.HEADER_CONTENT_TYPE,
					request.getBodyContentType());
			DataOutputStream dataOutputStream = new DataOutputStream(
					urlConnection.getOutputStream());
			dataOutputStream.write(body);
			dataOutputStream.close();
		}

	}

	private void setRequestHeaders(HttpURLConnection urlConnection,
			Request<?> request) {
		// TODO Auto-generated method stub
		Set<String> keySet = request.getHeaders().keySet();
		for (String headName : keySet) {
			urlConnection.addRequestProperty(headName, request.getHeaders()
					.get(headName));
		}
	}

	private HttpURLConnection createUrlConnetion(String url) throws IOException {
		// TODO Auto-generated method stub
		URL newUrl = new URL(url);
		URLConnection openConnection = newUrl.openConnection();
		openConnection.setConnectTimeout(config.connTimeOut);
		openConnection.setReadTimeout(config.soTimeOut);
		openConnection.setDoInput(true);
		openConnection.setUseCaches(false);
		return (HttpURLConnection) openConnection;
	}

	/**
	 * 执行HTTP请求之后获取到其数据流,即返回请求结果的流
	 * 
	 * @param connection
	 * @return
	 */
	private HttpEntity entityFromURLConnwction(HttpURLConnection connection) {
		BasicHttpEntity entity = new BasicHttpEntity();
		InputStream inputStream = null;
		try {
			inputStream = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			inputStream = connection.getErrorStream();
		}

		// TODO : GZIP
		entity.setContent(inputStream);
		entity.setContentLength(connection.getContentLength());
		entity.setContentEncoding(connection.getContentEncoding());
		entity.setContentType(connection.getContentType());

		return entity;
	}

	private void addHeadersToResponse(BasicHttpResponse response,
			HttpURLConnection connection) {
		for (Entry<String, List<String>> header : connection.getHeaderFields()
				.entrySet()) {
			if (header.getKey() != null) {
				Header h = new BasicHeader(header.getKey(), header.getValue()
						.get(0));
				response.addHeader(h);
			}
		}
	}
}
