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

import android.os.Bundle;
import android.view.MenuItem;

/**
 * {@link BaseActionBarActivity}'s subclass which must be used for an Activity
 * that needs to display an ActionBar, but is not a top-level Activity. In this
 * class, ActionBar is customized to show 'up' button etc.
 * 
 * @author 10Pearls
 */
public abstract class BaseLowerLevelActionBarActivity extends BaseActionBarActivity {

	/**
	 * You must call this implementation using {@code super.onCreate()} from
	 * your subclass. It internally configures the action bar.
	 * 
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);
		getActionBar ().setDisplayHomeAsUpEnabled (true);
		getActionBar ().setHomeButtonEnabled (true);
	}

	/**
	 * Allow this method to be called using {@code super.onOptionsItemSelected}
	 * from the derived class implementation. It invokes an abstract method
	 * 
	 * @param item the item
	 * @return true, if successful {@code onUpPressed()}, derived class can
	 *         provide functionality in that method.
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {

		switch (item.getItemId ()) {
			case android.R.id.home:
				onUpPressed ();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected (item);
	}

	/**
	 * Invoked when ActionBar's 'up' button is pressed. You must override this
	 * method to provide custom implementation on this action.
	 */
	public void onUpPressed () {

		isBackPressed = true;
	}

}
