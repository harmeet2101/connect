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

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;


/**
 * Abstract class which extends {@link ActionBarActivity}, any activity which
 * needs to display an action bar must extend from this class.
 * 
 * @author 10Pearls
 * 
 */
public abstract class BaseActionBarActivity extends AppCompatActivity {

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

	/** The is app went to bg. */
	protected static boolean isAppWentToBg             = false;

	/** The is window focused. */
	protected static boolean isWindowFocused           = false;

	/** The is back pressed. */
	protected static boolean isBackPressed             = false;

	/** The is moving to another activity. */
	public static boolean    isMovingToAnotherActivity = false;

	/**
	 * You must call this implementation using {@code super.onCreate()} from
	 * your subclass. It internally configures the action bar.
	 * 
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);
		setupActionBar ();
	}

	/**
	 * Override this method to customize the appearance of the {@link ActionBar}
	 * .
	 */
	protected void setupActionBar () {

		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart () {

		super.onStart ();
		if (isAppWentToBg) {
			isAppWentToBg = false;
			onEnterForeground ();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onStop()
	 */
	@Override
	protected void onStop () {

		super.onStop ();
		if (!isWindowFocused) {
			isAppWentToBg = true;
			onEnterBackground ();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {

		isWindowFocused = hasFocus;

		if ((isBackPressed || isMovingToAnotherActivity) && !hasFocus) {
			isBackPressed = false;
			isMovingToAnotherActivity = false;
			isWindowFocused = true;
		}

		super.onWindowFocusChanged (hasFocus);
	}

	/**
	 * Use this whenever you need to start an Activity. To provide a single
	 * point of change.
	 * 
	 * @param intent the intent
	 */
	@Override
	public void startActivity (Intent intent) {

		isMovingToAnotherActivity = true;
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

		isMovingToAnotherActivity = true;
		super.startActivityForResult (intent, requestCode);
	}

	/**
	 * Override this method if you want to provide a custom implementation when
	 * application goes into background.
	 */
	protected void onEnterBackground () {

		// TODO
	}

	protected void onEnterForeground () {

		// TODO
	}

}
