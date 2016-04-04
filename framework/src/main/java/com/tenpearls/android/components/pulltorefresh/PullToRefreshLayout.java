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

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class PullToRefreshLayout.
 */
public class PullToRefreshLayout extends FrameLayout {

	/** The m pull to refresh attacher. */
	private PullToRefreshAttacher mPullToRefreshAttacher;

	/**
	 * Instantiates a new pull to refresh layout.
	 * 
	 * @param context the context
	 */
	public PullToRefreshLayout (Context context) {

		this (context, null);
	}

	/**
	 * Instantiates a new pull to refresh layout.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 */
	public PullToRefreshLayout (Context context, AttributeSet attrs) {

		this (context, attrs, 0);
	}

	/**
	 * Instantiates a new pull to refresh layout.
	 * 
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public PullToRefreshLayout (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
	}

	/**
	 * Set the {@link PullToRefreshAttacher} to be used with this layout. The
	 * view which is added to this layout will automatically be added as a
	 * refreshable-view in the attacher.
	 * 
	 * @param attacher the attacher
	 * @param refreshListener the refresh listener
	 */
	public void setPullToRefreshAttacher (PullToRefreshAttacher attacher, PullToRefreshAttacher.OnRefreshListener refreshListener) {

		View view;
		for (int i = 0, z = getChildCount (); i < z; i++) {
			view = getChildAt (i);

			if (mPullToRefreshAttacher != null) {
				mPullToRefreshAttacher.removeRefreshableView (view);
			}

			if (attacher != null) {
				attacher.addRefreshableView (view, null, refreshListener, false);
			}
		}

		mPullToRefreshAttacher = attacher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent (MotionEvent event) {

		if (mPullToRefreshAttacher != null && getChildCount () > 0) {
			return mPullToRefreshAttacher.onInterceptTouchEvent (getChildAt (0), event);
		}
		return super.onInterceptTouchEvent (event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent (MotionEvent event) {

		if (mPullToRefreshAttacher != null && getChildCount () > 0) {
			return mPullToRefreshAttacher.onTouchEvent (getChildAt (0), event);
		}
		return super.onTouchEvent (event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	protected void onConfigurationChanged (Configuration newConfig) {

		super.onConfigurationChanged (newConfig);

		if (mPullToRefreshAttacher != null) {
			mPullToRefreshAttacher.onConfigurationChanged (newConfig);
		}
	}
}
