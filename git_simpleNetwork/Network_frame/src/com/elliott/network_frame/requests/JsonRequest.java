package com.elliott.network_frame.requests;

import org.json.JSONObject;

import com.elliott.network_frame.base.Request;
import com.elliott.network_frame.base.Response;

public class JsonRequest extends Request<JSONObject>{

	@Override
	public JSONObject parseResponse(Response response) {
		// TODO Auto-generated method stub
		String jsonString=new String(response.getRawDate());
		try {
			return new JSONObject(jsonString);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
