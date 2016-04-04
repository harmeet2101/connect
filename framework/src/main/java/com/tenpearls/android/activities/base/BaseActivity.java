/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.activities.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * {@link Activity}'s subclass. Should be used as a blue print for Activity
 * classes that do not need to display any ActionBar.
 * 
 * @author 10Pearls
 */
public abstract class BaseActivity extends Activity {

	/**
	 * This method should contain all the work related to initialization of the
	 * user interface elements. For example, inflating layouts, setting up
	 * action bar, create view objects using {@code findViewById} and other
	 * related tasks.
	 */
	protected abstract void initUI ();

	/**
	 * Wiring up of listeners on different objects must be done in this method.
	 */
	protected abstract void setListeners ();

	/**
	 * This method is responsible for loading data that is to be displayed on
	 * the screen. The data can come from different sources. For example:
	 * Network, Database, SharedPreferences etc. In case of network, the API
	 * call must be dispatched from here.
	 */
	protected abstract void loadData ();

	/**
	 * You are required to update user interface elements in this method. For
	 * example, updating UI controls based on the arrival of data from an API
	 * call.
	 */
	protected abstract void updateUI ();

	/**
	 * You must call this implementation using {@code super.onCreate()} from
	 * your subclass. It internally configures the action bar.
	 * 
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);
	}

	/**
	 * Use this whenever you need to start an Activity. To provide a single
	 * point of change.
	 * 
	 * @param intent the intent
	 */
	@Override
	public void startActivity (Intent intent) {

		super.startActivity (intent);
	}

	/**
	 * Use this whenever you need to start an Activity for result. To provide a
	 * single point of change.
	 * 
	 * @param intent the intent
	 * @param requestCode the request code
	 */
	@Override
	public void startActivityForResult (Intent intent, int requestCode) {

		super.startActivityForResult (intent, requestCode);
	}

}
