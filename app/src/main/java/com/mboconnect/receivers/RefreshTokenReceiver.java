package com.mboconnect.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mboconnect.entities.AccessToken;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.AccessTokenService;
import com.tenpearls.android.network.CustomHttpResponse;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created by vishal.chhatwani on 9/11/2015.
 */
public class RefreshTokenReceiver extends BroadcastReceiver {
    APIResponseListner		   accessTokenListener;

    @Override
    public void onReceive(Context context, Intent intent) {


        makeText(context, "Refresh TOken Broadcast called...!", LENGTH_LONG).show();
        initListeners();

        AccessTokenService accessTokenService=new AccessTokenService(context);
        makeText(context,DataModel.getAccessToken(),LENGTH_LONG).show();
        accessTokenService.updateAccessToken(accessTokenListener);
        makeText(context, "Access Token Updated.: ", LENGTH_LONG).show();
        makeText(context,DataModel.getAccessToken(),LENGTH_LONG).show();
    }

    public void initListeners () {

        accessTokenListener = new APIResponseListner() {
            @Override
            public void onSuccess (CustomHttpResponse response) {

                handleAccessTokenSuccessResponse (response);
            }
            @Override
            public void onFailure (CustomHttpResponse response) {

                // DataModel.setLoginCounter(DataModel.getLoginCounter() + 1);

            }
        };
    }

    public void handleAccessTokenSuccessResponse (CustomHttpResponse response) {

        AccessToken token = (AccessToken) response.getResponse ();
        DataModel.setAccessToken(token);

    }
}
