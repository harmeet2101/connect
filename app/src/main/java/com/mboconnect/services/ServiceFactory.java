package com.mboconnect.services;

import android.content.Context;

public class ServiceFactory {

	public OpportunityService oppotunityService;
	public ProfileService	  profileService;
	public AccessTokenService accessTokenService;

	public ServiceFactory (Context context) {

		oppotunityService = new OpportunityService (context);
		profileService = new ProfileService (context);
		accessTokenService = new AccessTokenService (context);
	}
}
