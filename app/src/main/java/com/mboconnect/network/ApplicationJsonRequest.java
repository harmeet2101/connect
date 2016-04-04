package com.mboconnect.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.mboconnect.Application;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.entities.AccessToken;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.AccessTokenService;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.network.JsonRequest;
import com.tenpearls.android.network.VolleyManager;

import java.util.HashMap;
import java.util.Map;

public class ApplicationJsonRequest extends JsonRequest implements Cloneable {

	static String REQUEST_TAG = "mbo-mobile-api-request";
	String		  accessToken;
	String		  url;
	private ApplicationJsonRequest canceledRequest = null;

	public ApplicationJsonRequest (int method, String url, String accessToken, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (method, url, requestBody, listener, errorListener);
		this.setRetryPolicy (new DefaultRetryPolicy (ServiceConstants.SERVICE_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		this.setShouldCache (false);
		this.url = url;
		this.accessToken = accessToken;
	}

	public void setAccessToken (String accessToken) {

		this.accessToken = accessToken;
	}

	@Override
	public Object getTag () {

		return REQUEST_TAG;
	}

	@Override
	public Map<String, String> getHeaders () throws AuthFailureError {

		Map<String, String> headers = new HashMap<String, String> ();

		if (url.equals (EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL)) {

			return headers;
		}
		headers.put ("Accept-Encoding", "gzip");
		headers.put ("User-Agent", "gzip");
		headers.put ("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.put ("Authorization", "Bearer " + accessToken);
		headers.put ("MBO-REALM", "mobile");

		return headers;
	}

	public static void setRequestTag (String tag) {

		REQUEST_TAG = tag;
	}

	@Override
	public com.android.volley.Request.Priority getPriority () {

		return Priority.IMMEDIATE;
	}

	@Override
	protected Response<NetworkResponse> parseNetworkResponse (NetworkResponse response) {

		REQUEST_TAG = "mbo-mobile-api-request";
		return super.parseNetworkResponse (response);
	}

	@Override
	protected VolleyError parseNetworkError (VolleyError volleyError) {

		// Check access token expiration error, and logout the user
		if (isAuthenticationError (volleyError) && !LoginActivity.isLoginMode) {

			if (!DataModel.isIsRefreshToken ()) {

				attemptAccessTokenRefresh();
			}
			else {

				DataModel.setIsRefreshToken(false);
				this.cancel();
				Application.getInstance ().onAuthenticationFailed();
			}
		}

		return super.parseNetworkError (volleyError);
	}

	private void attemptAccessTokenRefresh () {

		DataModel.setIsRefreshToken(true);

		try {

			canceledRequest = (ApplicationJsonRequest) ApplicationJsonRequest.this.clone ();

		}
		catch (CloneNotSupportedException e) {

			e.printStackTrace ();
		}
		this.cancel();

		AccessTokenService accessTokenService = new AccessTokenService(Application.getInstance());
		accessTokenService.refreshToken(new APIResponseListner() {

			@Override
			public void onSuccess(CustomHttpResponse response) {

				AccessToken token = (AccessToken) response.getResponse();
				DataModel.setAccessToken(token);
				DataModel.setIsRefreshToken(false);

				if (canceledRequest != null) {

					canceledRequest.setAccessToken(DataModel.getAccessToken());
					VolleyManager.getInstance().getRequestQueue().add(canceledRequest);
					canceledRequest = null;
				}
			}

			@Override
			public void onFailure(CustomHttpResponse response) {

			}
		});
	}

	private boolean isAuthenticationError (VolleyError volleyError) {

		return (volleyError.getStatusCode () == 401 ? true : false) || (volleyError.getStatusCode() == 500 && url.equals(EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL));
	}

	@Override
	protected String getJsonResponseErrorKey () {

		return "detail";
	}
}