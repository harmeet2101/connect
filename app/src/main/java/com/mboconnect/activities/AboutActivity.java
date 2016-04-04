package com.mboconnect.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.entities.Meta;
import com.mboconnect.helpers.DBHelper;
import com.mboconnect.model.DataModel;
import com.mboconnect.views.AboutView;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.LoginView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by tahir on 08/06/15.
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

	}

	@Override
	public BaseView getView (BaseActionBarActivity activity) {

		return new AboutView (activity);
	}

	public void startJobListActivity () {

		Intent intent = new Intent (this, OpportunityListActivity.class);
		this.startActivity (intent);
	}

}
