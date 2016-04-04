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

import android.app.ActionBar;
import android.view.View;

import com.tenpearls.android.components.pulltorefresh.PullToRefreshAttacher;
import com.tenpearls.android.components.pulltorefresh.PullToRefreshAttacher.OnRefreshListener;
import com.tenpearls.android.components.pulltorefresh.PullToRefreshLayout;

/**
 * Subclass of {@link BaseFragment}, you must extend this class if you want to
 * show an {@link ActionBar} with a Pull to refresh UI in your fragment. It also
 * implements {@link OnRefreshListener}, which provides callbacks when user has
 * initiated a Pull to refresh gesture.
 * 
 * @author 10Pearls
 * 
 */
public abstract class BaseRefreshableListFragment extends BaseFragment implements com.tenpearls.android.components.pulltorefresh.PullToRefreshAttacher.OnRefreshListener {

	/**
	 * Object to assist in setting up Pull to refresh UI. For internal use only.
	 */
	PullToRefreshAttacher pullToRefreshAttacher;

	/**
	 * Object to assist in setting up Pull to refresh UI. For internal use only.
	 */
	PullToRefreshLayout   pullToRefreshLayout;

	/**
	 * This class must return an instance of.
	 * 
	 * @return Instance of {@link PullToRefreshLayout}.
	 *         {@code com.tenpearls.android.components.pulltorefresh.PullToRefreshLayout}
	 *         created in the context of your Fragment.
	 */
	protected abstract PullToRefreshLayout getPullToRefreshLayout ();

	/**
	 * Override this method to provide functionality on pull to refresh action.
	 */
	protected abstract void onRefresh ();

	/**
	 * Call this method to lay the groundwork for pull to refresh setup.
	 */
	protected void setupPullToRefresh () {

		pullToRefreshLayout = getPullToRefreshLayout ();
		pullToRefreshAttacher = PullToRefreshAttacher.get (getActivity ());
		pullToRefreshLayout.setPullToRefreshAttacher (pullToRefreshAttacher, this);
	}

	/**
	 * Hides the loading animation used in pull to refresh action.
	 * 
	 * NOTE: You do not need to call this method for a pull to refresh action.
	 * Instead, if you want to hide it for any other scenario, use this method.
	 */
	public void setRefreshComplete () {

		pullToRefreshAttacher.setRefreshComplete ();
	}

	/**
	 * Displays the loading animation used in pull to refresh action. The
	 * animation is shown on the ActionBar.
	 * 
	 * NOTE: You do not need to call this method for a pull to refresh action.
	 * Instead, if you want to show it for any other scenario, use this method.
	 */
	public void setRefreshStart () {

		if (!pullToRefreshAttacher.isRefreshing ())
			pullToRefreshAttacher.setRefreshing (true);
	}

	/**
	 * On refresh started.
	 * 
	 * @param view the view {@link OnRefreshListener} callback. Fired when user
	 *            has completed the pull to refresh gesture.
	 * 
	 *            NOTE: Do not override this method. Instead use
	 *            {@code onRefresh()} to provide implementation.
	 */
	@Override
	public void onRefreshStarted (View view) {

		onRefresh ();
	}
}
