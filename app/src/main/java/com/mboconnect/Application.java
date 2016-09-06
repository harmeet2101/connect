package com.mboconnect;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.artisan.application.ArtisanApplication;
import com.artisan.manager.ArtisanManager;
import com.crashlytics.android.Crashlytics;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.managers.ArtisanHooksManager;
import com.mboconnect.managers.ConfigurationManager;
import com.tenpearls.android.network.VolleyManager;

import io.fabric.sdk.android.Fabric;

public class Application extends ArtisanApplication {

    static Application applicationInstance;

    @Override
    public void onCreate() {

        super.onCreate();
        initArtisan();
        Fabric.with(this, new Crashlytics());
        applicationInstance = this;

        initializeVolley();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ConfigurationManager.getInstance().updateConfigsFromArtisan();
            }
        }, 3*1000);


    }

    @Override
    public void registerPowerhooks() {

        ArtisanHooksManager.registerPowerHooks();
    }

    @Override
    public void registerInCodeExperiments() {
    }

    @Override
    public void registerUserProfileVariables() {
    }


    @Override
    public void onTerminate() {

        super.onTerminate();
    }


    public static Application getInstance() {

        return applicationInstance;
    }

    private void initArtisan() {

        ArtisanManager.startArtisan(this, "55c51056369f8431b6000001");
    }

    private void initializeVolley() {

        int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = memoryClass * 1024 / 4;

        VolleyManager.initialize(getApplicationContext(), cacheSize);
    }

    public void onAuthenticationFailed() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        sendLogoutBroadcast();
    }

    private void sendLogoutBroadcast() {

        Intent i = new Intent();
        i.setAction(BaseActivity.LOGOUT_ACTION);
        sendBroadcast(i);
    }
}
