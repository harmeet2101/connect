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

// TODO: Auto-generated Javadoc
/**
 * The Class ScrollYDelegate.
 */
public class ScrollYDelegate extends PullToRefreshAttacher.ViewDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tenpearls.android.components.pulltorefresh.PullToRefreshAttacher.
	 * ViewDelegate#isScrolledToTop(android.view.View)
	 */
	@Override
	public boolean isScrolledToTop (View view) {

		return view.getScrollY () <= 0;
	}
}
