package com.mboconnect.services;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.mboconnect.Application;
import com.mboconnect.R;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.entities.BaseEntity;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.network.ApplicationJsonDeleteRequest;
import com.mboconnect.network.ApplicationJsonGetRequest;
import com.mboconnect.network.ApplicationJsonPostRequest;
import com.mboconnect.network.ApplicationJsonPutRequest;
import com.mboconnect.services.response.BaseResponse;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.Loader;
import com.tenpearls.android.models.BaseWebServiceModel;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class BaseService extends BaseWebServiceModel {

	public Context mContext;
	private String baseUrl;

	public BaseService (Context context) {

		mContext = context;
		baseUrl = EnvironmentConstants.SERVICE_BASE_URL;
	}

	private VolleyError extractError (NetworkResponse response) {

		String responseString = new String (response.data);
		JsonObject jsonObject = null;
		try {
			jsonObject = JsonUtility.parseToJsonObject (responseString);
		}
		catch (Exception e) {
			// return new VolleyError (getContext ().getString
			// (R.string.unknown_error));
		}

		if (jsonObject == null) {

			jsonObject = new JsonObject ();
		}
		JsonObject results = JsonUtility.getJsonObject (jsonObject, "result");
		if (results == null)
			results = jsonObject;

		JsonObject error = JsonUtility.getJsonObject (results, "error");
		if (error == null)
			return null;

		String message = JsonUtility.getString (error, "message");
		if (message == null)
			message = getContext ().getString (R.string.unknown_error);

		VolleyError volleyError = new VolleyError (message);
		volleyError.setStatusCode (response.statusCode);
		return volleyError;
	}

	final protected <T> CustomHttpResponse getResponse (NetworkResponse networkResponse, BaseResponse<T> output) {

		String responseString = new String (networkResponse.data);
		VolleyError error = extractError (networkResponse);
		if (error != null)
			return getResponse (error);

		output.set (responseString);
		CustomHttpResponse customResponse = new CustomHttpResponse (output, null, networkResponse.statusCode);
		return customResponse;
	}

	final protected <T> CustomHttpResponse getResponse (NetworkResponse networkResponse, BaseEntity output) {

		String responseString = new String (networkResponse.data);
		VolleyError error = extractError (networkResponse);
		if (error != null)
			return getResponse (error);

		output.set (responseString);
		CustomHttpResponse customResponse = new CustomHttpResponse (output, null, networkResponse.statusCode);
		return customResponse;
	}

	final protected <T> CustomHttpResponse getResponse (NetworkResponse networkResponse) {

		VolleyError error = extractError (networkResponse);
		if (error != null)
			return getResponse (error);

		CustomHttpResponse customResponse = new CustomHttpResponse (null, null, networkResponse.statusCode);
		return customResponse;
	}

	final protected CustomHttpResponse getResponse (VolleyError error) {

		String errorMessage = "There was a problem connecting to server";

		if (error == null) {
			CustomHttpResponse customHttpError = new CustomHttpResponse (errorMessage, null, error.getStatusCode ());
			return customHttpError;
		}

		if (error.networkResponse != null || StringUtility.isEmptyOrNull (error.getMessage ()))
			error = extractError (error.networkResponse);

		if (!StringUtility.isEmptyOrNull (error.getMessage ())) {
			errorMessage = error.getMessage ();
		}

		CustomHttpResponse customHttpError = new CustomHttpResponse (errorMessage, null, error.getStatusCode ());
		return customHttpError;
	}

	final protected String getServiceURL (String method) {

		return baseUrl + method;
	}

	@Override
	protected Context getContext () {

		return Application.getInstance ();
	}

	@Override
	protected boolean shouldShowInternetNotWorkingView () {

		return true;
	}

	@Override
	protected void executeRequest (Request<?> request) {

		if (DeviceUtility.isInternetConnectionAvailable (Application.getInstance ())) {
			VolleyManager.getInstance ().getRequestQueue ().add (request);
		}
		else {
			Loader.hideLoader ();
			Utils.showInternetConnectionNotFoundMessage ();
		}
	}

	protected <T> void get (String url, String accessToken, final APIResponseListner listener, final BaseEntity response) {

		ApplicationJsonGetRequest request = new ApplicationJsonGetRequest (url, accessToken, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void get (String url, String accessToken, final APIResponseListner listener) {

		ApplicationJsonGetRequest request = new ApplicationJsonGetRequest (url, accessToken, getSuccessListener (listener), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void get (String url, String accessToken, final APIResponseListner listener, final BaseResponse<T> response) {

		ApplicationJsonGetRequest request = new ApplicationJsonGetRequest (url, accessToken, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void post (String url, String accessToken, String requestBody, APIResponseListner listener, BaseResponse<T> response) {

		ApplicationJsonPostRequest request = new ApplicationJsonPostRequest (url, accessToken, requestBody, getSuccessListener (listener, response), getErrorListener (listener));

		if (DeviceUtility.isInternetConnectionAvailable (Application.getInstance ())) {

			executeRequest (request);
		}
		else {

			listener.onFailure (new CustomHttpResponse ("", ""));
			executeRequest (request);
		}
	}

	protected <T> void post (String url, String accessToken, ArrayList<BasicNameValuePair> params, String bodyType, APIResponseListner listener, BaseEntity response) {

		ApplicationJsonPostRequest request = new ApplicationJsonPostRequest (url, accessToken, params, getSuccessListener (listener, response), getErrorListener (listener), bodyType);
		executeRequest (request);
	}

	protected <T> void post (String url, String accessToken, String requestBody, APIResponseListner listener, BaseEntity response) {

		ApplicationJsonPostRequest request = new ApplicationJsonPostRequest (url, accessToken, requestBody, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void put (String url, String accessToken, String requestBody, APIResponseListner listener, BaseResponse<T> response) {

		ApplicationJsonPutRequest request = new ApplicationJsonPutRequest (url, accessToken, requestBody, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void put (String url, String accessToken, String requestBody, APIResponseListner listener, BaseEntity response) {

		ApplicationJsonPutRequest request = new ApplicationJsonPutRequest (url, accessToken, requestBody, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void delete (String url, String accessToken, final APIResponseListner listener) {

		ApplicationJsonDeleteRequest request = new ApplicationJsonDeleteRequest (url, accessToken, getSuccessListener (listener), getErrorListener (listener));
		executeRequest (request);
	}

	protected <T> void delete (String url, String accessToken, final APIResponseListner listener, final BaseEntity response) {

		ApplicationJsonDeleteRequest request = new ApplicationJsonDeleteRequest (url, accessToken, getSuccessListener (listener, response), getErrorListener (listener));
		executeRequest (request);
	}

	private <T> Response.Listener<NetworkResponse> getSuccessListener (final APIResponseListner listener, final BaseEntity response) {

		return new Response.Listener<NetworkResponse> () {
			@Override
			public void onResponse (NetworkResponse arg0) {

				if (listener == null)
					return;

				CustomHttpResponse customResponse = getResponse (arg0, response);
				if (customResponse.getStatusCode () < 300 && customResponse.getStatusCode () >= 200)
					listener.onSuccess (customResponse);
				else
					listener.onFailure (customResponse);
			}
		};
	}

	private <T> Response.Listener<NetworkResponse> getSuccessListener (final APIResponseListner listener) {

		return new Response.Listener<NetworkResponse> () {
			@Override
			public void onResponse (NetworkResponse arg0) {

				if (listener == null)
					return;

				CustomHttpResponse customResponse = getResponse (arg0);
				if (customResponse.getStatusCode () < 300 && customResponse.getStatusCode () >= 200)
					listener.onSuccess (customResponse);
				else
					listener.onFailure (customResponse);
			}
		};
	}

	private <T> Response.Listener<NetworkResponse> getSuccessListener (final APIResponseListner listener, final BaseResponse<T> response) {

		return new Response.Listener<NetworkResponse> () {

			@Override
			public void onResponse (NetworkResponse arg0) {

				if (listener == null)
					return;

				CustomHttpResponse customResponse = getResponse (arg0, response);
				if (customResponse.getStatusCode () == 200)
					listener.onSuccess (customResponse);
				else
					listener.onFailure (customResponse);
			}
		};
	}

	private Response.ErrorListener getErrorListener (final APIResponseListner listener) {

		return new Response.ErrorListener () {

			@Override
			public void onErrorResponse (VolleyError arg0) {

				if (listener == null)
					return;
				listener.onFailure (getResponse (arg0));
			}
		};
	}

}
