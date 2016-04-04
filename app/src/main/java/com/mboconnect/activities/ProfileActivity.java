package com.mboconnect.activities;

import android.os.Bundle;
import android.view.WindowManager;

import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.ProfileView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;

/**
 * Created by vishal.chhatwani on 8/5/2015.
 */
public class ProfileActivity extends BaseActivity {


	@Override
	public BaseView getView (BaseActionBarActivity activity) {

		return new ProfileView (activity);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

		((ProfileView) view).populateData ();

	}

	@Override
	protected void onResume() {
		super.onResume();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
