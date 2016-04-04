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
package com.tenpearls.android.fragments.base;

import com.tenpearls.android.activities.base.BaseActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link Fragment}'s subclass, provides a base for implementing your fragments.
 * Always extend this class in your fragments.
 * 
 * @author 10Pearls
 */
public abstract class BaseFragment extends Fragment {

	/**
	 * You are required to return the text that you want to show as ActionBar
	 * title from this method.
	 * 
	 * @return The ActionBar title text
	 */
	protected abstract String getTitle ();

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
	 * You must call this implementation using {@code super.onCreateView()} from
	 * your subclass. It internally configures the action bar.
	 * 
	 * @param inflater the inflater
	 * @param container the container
	 * @param savedInstanceState the saved instance state
	 * @return the view
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		((BaseActionBarActivity) getActivity ()).getActionBar ().setTitle (getTitle ());
		return null;
	}

	/**
	 * Use this whenever you need to start an Activity. To provide a single
	 * point of change.
	 * 
	 * @param intent the intent
	 */
	@Override
	public void startActivity (Intent intent) {

		BaseActionBarActivity.isMovingToAnotherActivity = true;
		super.startActivity (intent);
	}

	/**
	 * Use this whenever you need to start an Activity. To provide a single
	 * point of change.
	 * 
	 * @param intent the intent
	 * @param requestCode the request code
	 */
	@Override
	public void startActivityForResult (Intent intent, int requestCode) {

		BaseActionBarActivity.isMovingToAnotherActivity = true;
		super.startActivityForResult (intent, requestCode);
	}
}
