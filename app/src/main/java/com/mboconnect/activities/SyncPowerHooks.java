package com.mboconnect.activities;

import android.os.Bundle;

import com.artisan.activity.ArtisanActivity;
import com.artisan.manager.ArtisanManager;
import com.artisan.manager.ArtisanManagerCallback;
import com.artisan.powerhooks.PowerHookManager;
import com.mboconnect.R;

/**
 * Created by vishal.chhatwani on 10/2/2015.
 */
public class SyncPowerHooks extends ArtisanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_power_hooks_sync);

        ArtisanManager.onFirstPlaylistDownloaded(this, new ArtisanManagerCallback() {
            @Override
            public void execute() {
                // First playlist has been downloaded, now you can get the latest power hook experiment details
                PowerHookManager.getPowerHookBlockExperimentDetails();
                PowerHookManager.getPowerHookVariableExperimentDetails();
            }
        });
    }

}
