package com.mboconnect.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mboconnect.R;
import com.mboconnect.constants.AppConstants;

/**
 * Created by ali.mehmood on 6/19/2015.
 */
public class SplashActivity extends Activity {

	@Override
	public void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_splash);

		if (isTaskRoot ()) {
			navigateToMainActivity ();
		}
		else {
			finish ();
		}
	}

	private void navigateToMainActivity () {

		new Handler ().postDelayed (new Runnable () {
			@Override
			public void run () {

				startActivity (new Intent (getApplicationContext (), LoginActivity.class));
				finish ();
			}
		}, AppConstants.SPLASH_TIME_OUT_IN_MILLI);
	}
}
