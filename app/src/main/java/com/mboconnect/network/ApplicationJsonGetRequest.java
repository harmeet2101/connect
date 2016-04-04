package com.mboconnect.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class ApplicationJsonGetRequest extends ApplicationJsonRequest {

	public ApplicationJsonGetRequest (String url, String accessToken, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (Request.Method.GET, url, accessToken, null, listener, errorListener);
	}
}
