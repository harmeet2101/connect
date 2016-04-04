package com.mboconnect.activities;

import android.os.Bundle;

import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.SettingsView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;

/**
 * Created by tahir on 08/06/15.
 */
public class SettingsActivity extends BaseActivity {

	private long startMillis = 0;
	private int count = 0;

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

	}

	@Override
	public BaseView getView (BaseActionBarActivity activity) {

		return new SettingsView(activity);
	}
}
