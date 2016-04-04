package com.mboconnect.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class ApplicationJsonDeleteRequest extends ApplicationJsonRequest {

	public ApplicationJsonDeleteRequest (String url, String accessToken, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (Method.DELETE, url, accessToken, "", listener, errorListener);
	}
}
