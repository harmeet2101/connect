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
package com.tenpearls.android.managers;

import java.net.URLEncoder;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.androauth.oauth.OAuth20Service;
import com.androauth.oauth.OAuthService;
import com.androauth.oauth.OAuthUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.tenpearls.android.entities.OAuth20TokenExt;
import com.tenpearls.android.entities.base.BaseOAuthAPIConfig;
import com.tenpearls.android.interfaces.OAuth20TokenListener;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.network.JsonRequest;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * An OAuth20 gateway class that simplifies the overall work required for
 * implementing the OAuth20 flow. The class mainly requires an OAuth20Api object
 * to function and a listener to receive the callback methods.
 * 
 * @author 10Pearls
 */
public class OAuth20Gateway {

	/** The Constant KEY_CODE. */
	static final String   KEY_CODE          = "code";

	/** The Constant KEY_GRANT_TYPE. */
	static final String   KEY_GRANT_TYPE    = "grant_type";

	/** The Constant KEY_REDIRECT_URI. */
	static final String   KEY_REDIRECT_URI  = "redirect_uri";

	/** The Constant KEY_CLIENT_ID. */
	static final String   KEY_CLIENT_ID     = "client_id";

	/** The Constant KEY_CLIENT_SECRET. */
	static final String   KEY_CLIENT_SECRET = "client_secret";

	/** The Constant KEY_REFRESH_TOKEN. */
	static final String   KEY_REFRESH_TOKEN = "refresh_token";

	/** The Constant KEY_ACCESS_TOKEN. */
	static final String   KEY_ACCESS_TOKEN  = "access_token";

	/** The Constant KEY_EXPIRES_IN. */
	static final String   KEY_EXPIRES_IN    = "expires_in";

	/** The Constant KEY_TOKEN_TYPE. */
	static final String   KEY_TOKEN_TYPE    = "token_type";

	/** The Constant KEY_RESPONSE_TYPE. */
	static final String   KEY_RESPONSE_TYPE = "response_type";

	/** The s instance. */
	static OAuth20Gateway sInstance;

	/**
	 * This Context object for this Gateway.
	 */
	Context               mContext;

	/**
	 * This OAuthAPI to implement the OAuth 2.0 flow.
	 */
	BaseOAuthAPIConfig          mBaseOAuthAPIConfig;

	/**
	 * This OAuth20Service object to do some basic OAuth 2.0 tasks.
	 */
	OAuth20Service        mOAuth20Service;

	/**
	 * Gets the instance of the OAuth20Gateway.
	 * 
	 * @return single instance of OAuth20Gateway
	 */
	public static OAuth20Gateway getInstance () {

		if (sInstance == null) {
			sInstance = new OAuth20Gateway ();
		}

		return sInstance;
	}

	/**
	 * Call this method when you get the instance of gateway for the first time.
	 * 
	 * @param baseOAuthAPIConfig The object of a subclass of the BaseOAuthAPI which
	 *            implements all the abstract methods
	 */
	public void initialize (BaseOAuthAPIConfig baseOAuthAPIConfig) {

		if (baseOAuthAPIConfig == null) {
			throw new NullPointerException ("OAuth20Gateway >> OAuth20API can not be null.");
		}

		this.mBaseOAuthAPIConfig = baseOAuthAPIConfig;
		mOAuth20Service = OAuth20Service.newInstance (mBaseOAuthAPIConfig, mBaseOAuthAPIConfig.getClientID (), mBaseOAuthAPIConfig.getClientSecret (), null);
		mOAuth20Service.setApiCallback (mBaseOAuthAPIConfig.getRedirectURI ());
		mOAuth20Service.setScope (mBaseOAuthAPIConfig.getServiceScope ());
		mOAuth20Service.setDuration (mBaseOAuthAPIConfig.getServiceDuration ());
	}

	/**
	 * Call this method when the server redirects you to the url with an auth
	 * code.
	 * 
	 * @param url The Url with an authorization code
	 * @param oAuth20TokenListener The listener object that would listen to
	 *            events of token receipt or failure
	 * @return the o auth access token
	 */
	public void getOAuthAccessToken (final String url, final OAuth20TokenListener oAuth20TokenListener) {

		String code = OAuthUtils.extract (url, OAuthService.CODE_REGEX);

		makeTokenRequest (mBaseOAuthAPIConfig.getAccessTokenResource (), getParamsForTokenCall (code), oAuth20TokenListener);
	}

	/**
	 * Call this method when the current token expires and a new token is need.
	 * 
	 * @param refreshToken The refresh token that was given last time
	 * @param oAuth20TokenListener The listener object that would listen to
	 *            events of token receipt or failure
	 */
	public void refreshAccessToken (String refreshToken, final OAuth20TokenListener oAuth20TokenListener) {

		makeTokenRequest (mBaseOAuthAPIConfig.getAccessTokenResource (), getParamsForRefreshTokenCall (refreshToken), oAuth20TokenListener);
	}

	/**
	 * This method return the Request Parameters for the Access Token Call.
	 * 
	 * @param code The Authorization code returned by the API
	 * 
	 * @return Vector A vector of Request Parameters
	 */
	private Vector<NameValuePair> getParamsForTokenCall (String code) {

		Vector<NameValuePair> params = new Vector<NameValuePair> ();
		params.add (new BasicNameValuePair (KEY_CODE, code));
		params.add (new BasicNameValuePair (KEY_GRANT_TYPE, mBaseOAuthAPIConfig.getGrantTypeCode ()));
		params.add (new BasicNameValuePair (KEY_REDIRECT_URI, mBaseOAuthAPIConfig.getRedirectURI ()));
		params.add (new BasicNameValuePair (KEY_CLIENT_ID, mBaseOAuthAPIConfig.getClientID ()));
		params.add (new BasicNameValuePair (KEY_CLIENT_SECRET, mBaseOAuthAPIConfig.getClientSecret ()));
		return params;
	}

	/**
	 * This method return the Request Parameters for the Refresh Token Call.
	 * 
	 * @param refreshToken The Refresh Token returned by the API
	 * 
	 * @return Vector A vector of Request Parameters
	 */
	private Vector<NameValuePair> getParamsForRefreshTokenCall (String refreshToken) {

		Vector<NameValuePair> params = new Vector<NameValuePair> ();
		params.add (new BasicNameValuePair (KEY_REFRESH_TOKEN, refreshToken));
		params.add (new BasicNameValuePair (KEY_GRANT_TYPE, mBaseOAuthAPIConfig.getGrantTypeRefresh ()));
		params.add (new BasicNameValuePair (KEY_REDIRECT_URI, mBaseOAuthAPIConfig.getRedirectURI ()));
		params.add (new BasicNameValuePair (KEY_CLIENT_ID, mBaseOAuthAPIConfig.getClientID ()));
		params.add (new BasicNameValuePair (KEY_CLIENT_SECRET, mBaseOAuthAPIConfig.getClientSecret ()));
		return params;
	}

	/**
	 * This method will parse the server response for the access token call.
	 * 
	 * @param response The server response for the access token call
	 * @return OAuth20TokenExt The OAuth20TokenExt object
	 */
	public OAuth20TokenExt parseOAuth20TokenExt (String response) {

		JsonObject rootObj = JsonUtility.parseToJsonObject (response);
		String accessToken = JsonUtility.getString (rootObj, KEY_ACCESS_TOKEN);
		String refreshToken = JsonUtility.getString (rootObj, KEY_REFRESH_TOKEN);
		int expiresIn = JsonUtility.getInt (rootObj, KEY_EXPIRES_IN);
		return new OAuth20TokenExt (accessToken, refreshToken, expiresIn);
	}

	/**
	 * This method makes the Token request call.
	 * 
	 * @param url The Url to hit
	 * @param params A vector of Request Parameters
	 * @param oAuth20TokenListener A listener that will receive the token
	 *            receipt or failure methods
	 */
	private void makeTokenRequest (String url, final Vector<NameValuePair> params, final OAuth20TokenListener oAuth20TokenListener) {

		JsonRequest jsonRequest = new JsonRequest (Method.POST, url, "", new Listener<NetworkResponse> () {
			@Override
			public void onResponse (NetworkResponse networkResponse) {

				OAuth20TokenExt oAuth20TokenExt = parseOAuth20TokenExt (new String (networkResponse.data));
				if (oAuth20TokenListener != null) {
					oAuth20TokenListener.onOAuthAccessTokenReceived (oAuth20TokenExt);
				}
			}
		}, new Response.ErrorListener () {
			@Override
			public void onErrorResponse (VolleyError error) {

				if (oAuth20TokenListener != null) {
					oAuth20TokenListener.onAccessTokenRequestFailed (new CustomHttpResponse (error.getMessage (), "", error.networkResponse.statusCode));
				}
			}
		}) {
			@Override
			public String getBodyContentType () {

				return "application/x-www-form-urlencoded";
			}

			@Override
			public byte[] getBody () {

				String parametersString = "";

				if (params != null) {
					try {
						StringBuilder stringBuilder = new StringBuilder ();
						for (int i = 0; i < params.size (); i++) {

							if (i > 0)
								stringBuilder.append ("&");

							NameValuePair nameValuePair = params.elementAt (i);
							stringBuilder.append (nameValuePair.getName ());
							stringBuilder.append ("=");
							stringBuilder.append (URLEncoder.encode (nameValuePair.getValue (), "UTF-8"));
							parametersString = stringBuilder.toString ();
						}
					}
					catch (Exception e) {
					}
				}

				return parametersString.getBytes ();
			}
		};

		VolleyManager.getInstance ().getRequestQueue ().add (jsonRequest);
	}

	/**
	 * This method will return the Authorize Url for the currently set
	 * OAuth20API.
	 * 
	 * @return String The Authorize Url for the currently set OAuth20API
	 */
	public String getAuthorizeUrl () {

		return mOAuth20Service.getAuthorizeUrl ();
	}
}
