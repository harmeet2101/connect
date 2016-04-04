package com.mboconnect.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mboconnect.Application;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.managers.ArtisanHooksManager;
import com.mboconnect.model.DataModel;
import com.tenpearls.android.utilities.PreferenceUtility;

/**
 * Created by ali.mehmood on 8/7/2015.
 */
public class LoginAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive (Context context, Intent intent) {


		int position = PreferenceUtility.getInteger(Application.getInstance(), "environmentPosition", 0);
		if(position==0){
			EnvironmentConstants.SERVICE_LOGOUT_URL = ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PROD_LOGOUT_URL, ArtisanHooksManager.DEFAULT_PROD_AUTH_URL);
		}
		else{
			EnvironmentConstants.SERVICE_LOGOUT_URL = ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRE_PROD_LOGOUT_URL, ArtisanHooksManager.DEFAULT_PRE_PROD_LOGOUT_URL);
		}
		DataModel.setLoginCounter (0);
		Intent i = new Intent ();
		i.setAction (LoginActivity.ENABLE_LOGIN_ACTION);
		context.sendBroadcast (i);
	}
}