package com.elliott.network_frame.requests;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Response;

public class StringRequest extends Request<String> {

	@Override
	public String parseResponse(Response response) {
		// TODO Auto-generated method stub
		return new String(response.getRawDate());
	}

}
