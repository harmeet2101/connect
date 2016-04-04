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
package com.tenpearls.android.components.pulltorefresh;

import android.view.View;
import android.widget.AbsListView;

// TODO: Auto-generated Javadoc
/**
 * The Class AbsListViewDelegate.
 */
public class AbsListViewDelegate extends PullToRefreshAttacher.ViewDelegate {

	/** The Constant SUPPORTED_VIEW_CLASS. */
	public static final Class<AbsListView> SUPPORTED_VIEW_CLASS = AbsListView.class;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tenpearls.android.components.pulltorefresh.PullToRefreshAttacher.
	 * ViewDelegate#isScrolledToTop(android.view.View)
	 */
	@Override
	public boolean isScrolledToTop (View view) {

		AbsListView absListView = (AbsListView) view;
		if (absListView.getCount () == 0) {
			return true;
		}
		else if (absListView.getFirstVisiblePosition () == 0) {
			final View firstVisibleChild = absListView.getChildAt (0);
			return firstVisibleChild != null && firstVisibleChild.getTop () >= 0;
		}
		return false;
	}
}
