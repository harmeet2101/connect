package com.mboconnect.services;

import android.content.Context;

import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.entities.Profile;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.MessageResponse;

import java.net.URLEncoder;

/**
 * Created by ali.mehmood on 6/25/2015.
 */
public class ProfileService extends BaseService {

	public ProfileService (Context context) {

		super (context);

	}

	public void getProfileData (APIResponseListner responseListner) {

		//new ProfileService(getContext());
		//new BaseService(getContext());
		//ConfigurationManager.getInstance().loadConfigs();
		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_PROFILE);

		super.get (url, DataModel.getAccessToken (), responseListner, new Profile ());

	}

	public void getMessages (int pageSize, int pageSkip, APIResponseListner responseListner) {

		String query = String.format ("{\"skip\":%d,\"page_size\":%d}", pageSkip, pageSize);

		try {

			query = URLEncoder.encode (query, "utf-8");
		}
		catch (Exception e) {

			e.printStackTrace ();
		}
		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_MESSAGES + query);

		super.get (url, DataModel.getAccessToken (), responseListner, new MessageResponse ());
	}
}
