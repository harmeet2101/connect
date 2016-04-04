package com.mboconnect.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;

public class ApplicationJsonPostRequest extends ApplicationJsonRequest {

	Vector<NameValuePair>		  paramsVector = null;
	ArrayList<BasicNameValuePair> paramsArray  = null;
	String						  requestBody  = null;
	String						  bodyType	   = "application/json";

	public ApplicationJsonPostRequest (String url, String accessToken, Vector<NameValuePair> params, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (Request.Method.POST, url, accessToken, "", listener, errorListener);
		this.paramsVector = params;
	}

	public ApplicationJsonPostRequest (String url, String accessToken, ArrayList<BasicNameValuePair> params, Listener<NetworkResponse> listener, ErrorListener errorListener, String bodyType) {

		super (Request.Method.POST, url, accessToken, "", listener, errorListener);
		this.paramsArray = params;
		this.bodyType = bodyType;
	}

	public ApplicationJsonPostRequest (String url, String accessToken, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (Request.Method.POST, url, accessToken, "", listener, errorListener);

		if (requestBody == null)
			requestBody = new JsonObject ().toString ();

		this.requestBody = requestBody;
	}

	@Override
	public byte[] getBody () {

		String parametersString = "";

		if (paramsVector != null) {
			try {
				StringBuilder stringBuilder = new StringBuilder ();
				for (int i = 0; i < paramsVector.size (); i++) {

					if (i > 0)
						stringBuilder.append ("&");

					NameValuePair nameValuePair = paramsVector.elementAt (i);
					stringBuilder.append (nameValuePair.getName ());
					stringBuilder.append ("=");
					stringBuilder.append (URLEncoder.encode (nameValuePair.getValue (), "UTF-8"));
					parametersString = stringBuilder.toString ();
				}
			}
			catch (Exception e) {
			}
		}
		else if (paramsArray != null) {
			try {
				StringBuilder stringBuilder = new StringBuilder ();
				for (int i = 0; i < paramsArray.size (); i++) {

					if (i > 0)
						stringBuilder.append ("&");

					NameValuePair nameValuePair = paramsArray.get (i);
					stringBuilder.append (nameValuePair.getName ());
					stringBuilder.append ("=");
					stringBuilder.append (URLEncoder.encode (nameValuePair.getValue (), "UTF-8"));
					parametersString = stringBuilder.toString ();
				}
			}
			catch (Exception e) {
			}
		}
		else {
			parametersString = requestBody;
		}

		return parametersString.getBytes ();
	}

	@Override
	public String getBodyContentType () {

		return bodyType;
	}
}
