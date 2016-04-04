package com.mboconnect.services;

import android.content.Context;

import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.entities.AccessToken;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.utils.Utils;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by ali.mehmood on 6/25/2015.
 */
public class AccessTokenService extends BaseService {

	static Context context;

	public AccessTokenService (Context context) {

		super(context);
		this.context=context;
	}

	public void getAccessToken (APIResponseListner responseListner, String username, String password) {

		String url = EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL;

		super.post(url, null, getParams(username, password), "application/x-www-form-urlencoded", responseListner, new AccessToken());
	}

	public void updateAccessToken (APIResponseListner responseListner){

		String url = EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL;
		ArrayList<BasicNameValuePair> params=new ArrayList<>();
		params.add (new BasicNameValuePair ("client_id", Utils.decodeFromBase64 (EnvironmentConstants.CLIENT_ID)));
		params.add (new BasicNameValuePair("refresh_token", ""));
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
		super.post(url, null, params, "application/x-www-form-urlencoded", responseListner, new AccessToken());

	}

	public void refreshToken (APIResponseListner responseListner) {

		String url = EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL;

		super.post(url, null, getRefreshTokenParams(), "application/x-www-form-urlencoded", responseListner, new AccessToken());
	}

	private ArrayList<BasicNameValuePair> getRefreshTokenParams () {

		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair> ();
		params.add(new BasicNameValuePair("client_id", Utils.decodeFromBase64 (EnvironmentConstants.CLIENT_ID)));
		params.add (new BasicNameValuePair ("refresh_token", DataModel.getRefreshToken()));
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));

		return params;
	}


	private ArrayList<BasicNameValuePair> getParams (String username, String password) {

		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair> ();
		params.add (new BasicNameValuePair ("client_id", Utils.decodeFromBase64 (EnvironmentConstants.CLIENT_ID)));
		params.add(new BasicNameValuePair("username", username));
		params.add (new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("grant_type", "password"));

		return params;
	}

	public void logout(APIResponseListner responseListner) {

		String url = EnvironmentConstants.SERVICE_LOGOUT_URL;

		super.get(url, DataModel.getAccessToken(), responseListner);
	}
}
