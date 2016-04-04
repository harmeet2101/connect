/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Subclass of {@link com.android.volley.toolbox.JsonRequest} which serves as a
 * layer to aid in processing web service errors and some other stuff.
 * 
 * @author 10Pearls
 * 
 */
public class JsonRequest extends com.android.volley.toolbox.JsonRequest<NetworkResponse> {

	/**
	 * Creates a new instance based on the provided parameters.
	 * 
	 * @param method The HTTP method to use.
	 * @param url The HTTP Url to hit.
	 * @param requestBody Optional request body to accompany the network call.
	 * @param listener Success listener.
	 * @param errorListener Error listener.
	 */
	public JsonRequest (int method, String url, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (method, url, requestBody, listener, errorListener);
	}

	/**
	 * Scrutinizes the server's response and classifies it as success/error
	 * response based on the status code.
	 * 
	 * @param response the response
	 * @return the response
	 */
	@Override
	protected Response<NetworkResponse> parseNetworkResponse (NetworkResponse response) {

		int statusCode = response.statusCode;

		if (statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 404 || statusCode == 405 || statusCode == 500) {
			return Response.error (new VolleyError (response));
		}
		else {
			return Response.success (response, HttpHeaderParser.parseCacheHeaders (response));
		}
	}

	/**
	 * Override this method to return the json key used in the web service to
	 * represent errors.
	 * 
	 * @return JSON key which is used to represent errors in the Web Service
	 *         response.
	 */
	protected String getJsonResponseErrorKey () {

		throw new UnsupportedOperationException (this.getClass ().getName () + " >> You must override getJsonResponseErrorKey()");
	}

	/**
	 * Parses error returned from the server based on the json key returned
	 * from.
	 * 
	 * @param volleyError the volley error
	 * @return Error returned from web service based on the key returned from
	 *         {@code getJsonResponseErrorKey}. {@code getJsonResponseErrorKey}.
	 */
	@Override
	protected VolleyError parseNetworkError (VolleyError volleyError) {

		String jsonObjectErrorKey = getJsonResponseErrorKey ();
		String errorString = "Unknown error, please try again.";

		if (StringUtility.isEmptyOrNull (jsonObjectErrorKey))
			return volleyError;

		try {
			String errorJsonString = new String (volleyError.networkResponse.data);
			JsonObject errorJsonObject = JsonUtility.parseToJsonObject (errorJsonString);

			if (errorJsonObject != null) {
				if (errorJsonObject.has (jsonObjectErrorKey)) {
					errorString = errorJsonObject.get (jsonObjectErrorKey).getAsString ();
				}
			}

			return new VolleyError (errorString);
		}
		catch (Exception e) {
			return new VolleyError (errorString);
		}
	}

}
