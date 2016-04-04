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

import java.util.Set;
import java.util.WeakHashMap;

import com.tenpearls.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class PullToRefreshAttacher.
 */
public class PullToRefreshAttacher implements View.OnTouchListener {

	/** The Constant DEFAULT_HEADER_LAYOUT. */
	private static final int                    DEFAULT_HEADER_LAYOUT           = R.layout.default_header;

	/** The Constant DEFAULT_ANIM_HEADER_IN. */
	private static final int                    DEFAULT_ANIM_HEADER_IN          = R.anim.fade_in;

	/** The Constant DEFAULT_ANIM_HEADER_OUT. */
	private static final int                    DEFAULT_ANIM_HEADER_OUT         = R.anim.fade_out;

	/** The Constant DEFAULT_REFRESH_SCROLL_DISTANCE. */
	private static final float                  DEFAULT_REFRESH_SCROLL_DISTANCE = 0.5f;

	/** The Constant DEFAULT_REFRESH_ON_UP. */
	private static final boolean                DEFAULT_REFRESH_ON_UP           = false;

	/** The Constant DEFAULT_REFRESH_MINIMIZED_DELAY. */
	private static final int                    DEFAULT_REFRESH_MINIMIZED_DELAY = 3 * 1000;

	/** The Constant DEFAULT_REFRESH_MINIMIZE. */
	private static final boolean                DEFAULT_REFRESH_MINIMIZE        = true;

	/** The Constant DEBUG. */
	private static final boolean                DEBUG                           = false;

	/** The Constant LOG_TAG. */
	private static final String                 LOG_TAG                         = "PullToRefreshAttacher";

	/* Member Variables */

	/** The m environment delegate. */
	private final EnvironmentDelegate           mEnvironmentDelegate;

	/** The m header transformer. */
	private final HeaderTransformer             mHeaderTransformer;

	/** The m header view. */
	private final View                          mHeaderView;

	/** The m header view listener. */
	private HeaderViewListener                  mHeaderViewListener;

	/** The m header out animation. */
	private final Animation                     mHeaderInAnimation,
	        mHeaderOutAnimation;

	/** The m touch slop. */
	private final int                           mTouchSlop;

	/** The m refresh scroll distance. */
	private final float                         mRefreshScrollDistance;

	/** The m pull begin y. */
	private int                                 mInitialMotionY,
	        mLastMotionY, mPullBeginY;

	/** The m is handling touch event. */
	private boolean                             mIsBeingDragged,
	        mIsRefreshing,
	        mIsHandlingTouchEvent;

	/** The m refreshable views. */
	private final WeakHashMap<View, ViewParams> mRefreshableViews;

	/** The m enabled. */
	private boolean                             mEnabled                        = true;

	/** The m refresh on up. */
	private final boolean                       mRefreshOnUp;

	/** The m refresh minimize delay. */
	private final int                           mRefreshMinimizeDelay;

	/** The m refresh minimize. */
	private final boolean                       mRefreshMinimize;

	/** The m handler. */
	private final Handler                       mHandler                        = new Handler ();

	/**
	 * Get a PullToRefreshAttacher for this Activity. If there is already a
	 * PullToRefreshAttacher attached to the Activity, the existing one is
	 * returned, otherwise a new instance is created. This version of the method
	 * will use default configuration options for everything.
	 * 
	 * @param activity Activity to attach to.
	 * @return PullToRefresh attached to the Activity.
	 */
	public static PullToRefreshAttacher get (Activity activity) {

		return get (activity, new Options ());
	}

	/**
	 * Get a PullToRefreshAttacher for this Activity. If there is already a
	 * PullToRefreshAttacher attached to the Activity, the existing one is
	 * returned, otherwise a new instance is created.
	 * 
	 * @param activity Activity to attach to.
	 * @param options Options used when creating the PullToRefreshAttacher.
	 * @return PullToRefresh attached to the Activity.
	 */
	public static PullToRefreshAttacher get (Activity activity, Options options) {

		return new PullToRefreshAttacher (activity, options);
	}

	/**
	 * Instantiates a new pull to refresh attacher.
	 * 
	 * @param activity the activity
	 * @param options the options
	 */
	protected PullToRefreshAttacher (Activity activity, Options options) {

		if (options == null) {
			Log.i (LOG_TAG, "Given null options so using default options.");
			options = new Options ();
		}

		mRefreshableViews = new WeakHashMap<View, ViewParams> ();

		// Copy necessary values from options
		mRefreshScrollDistance = options.refreshScrollDistance;
		mRefreshOnUp = options.refreshOnUp;
		mRefreshMinimizeDelay = options.refreshMinimizeDelay;
		mRefreshMinimize = options.refreshMinimize;

		// EnvironmentDelegate
		mEnvironmentDelegate = options.environmentDelegate != null ? options.environmentDelegate : createDefaultEnvironmentDelegate ();

		// Header Transformer
		mHeaderTransformer = options.headerTransformer != null ? options.headerTransformer : createDefaultHeaderTransformer ();

		// Create animations for use later
		mHeaderInAnimation = AnimationUtils.loadAnimation (activity, options.headerInAnimation);
		mHeaderOutAnimation = AnimationUtils.loadAnimation (activity, options.headerOutAnimation);
		if (mHeaderOutAnimation != null || mHeaderInAnimation != null) {
			final AnimationCallback callback = new AnimationCallback ();
			if (mHeaderInAnimation != null)
				mHeaderInAnimation.setAnimationListener (callback);
			if (mHeaderOutAnimation != null)
				mHeaderOutAnimation.setAnimationListener (callback);
		}

		// Get touch slop for use later
		mTouchSlop = ViewConfiguration.get (activity).getScaledTouchSlop ();

		// Get Window Decor View
		final ViewGroup decorView = (ViewGroup) activity.getWindow ().getDecorView ();

		// Check to see if there is already a Attacher view installed
		if (decorView.getChildCount () == 1 && decorView.getChildAt (0) instanceof DecorChildLayout) {
			throw new IllegalStateException ("View already installed to DecorView. This shouldn't happen.");
		}

		// Create Header view and then add to Decor View
		mHeaderView = LayoutInflater.from (mEnvironmentDelegate.getContextForInflater (activity)).inflate (options.headerLayout, decorView, false);
		if (mHeaderView == null) {
			throw new IllegalArgumentException ("Must supply valid layout id for header.");
		}
		mHeaderView.setVisibility (View.GONE);

		// Create DecorChildLayout which will move all of the system's decor
		// view's children + the
		// Header View to itself. See DecorChildLayout for more info.
		DecorChildLayout decorContents = new DecorChildLayout (activity, decorView, mHeaderView);

		// Now add the DecorChildLayout to the decor view
		decorView.addView (decorContents, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		// Notify transformer
		mHeaderTransformer.onViewCreated (activity, mHeaderView);
		// TODO Remove the follow deprecated method call before v1.0
		mHeaderTransformer.onViewCreated (mHeaderView);
	}

	/**
	 * Add a view which will be used to initiate refresh requests and a listener
	 * to be invoked when a refresh is started. This version of the method will
	 * try to find a handler for the view from the built-in view delegates.
	 * 
	 * @param view View which will be used to initiate refresh requests.
	 * @param refreshListener Listener to be invoked when a refresh is started.
	 */
	public void addRefreshableView (View view, OnRefreshListener refreshListener) {

		addRefreshableView (view, null, refreshListener);
	}

	/**
	 * Add a view which will be used to initiate refresh requests, along with a
	 * delegate which knows how to handle the given view, and a listener to be
	 * invoked when a refresh is started.
	 * 
	 * @param view View which will be used to initiate refresh requests.
	 * @param viewDelegate delegate which knows how to handle <code>view</code>.
	 * @param refreshListener Listener to be invoked when a refresh is started.
	 */
	public void addRefreshableView (View view, ViewDelegate viewDelegate, OnRefreshListener refreshListener) {

		addRefreshableView (view, viewDelegate, refreshListener, true);
	}

	/**
	 * Add a view which will be used to initiate refresh requests, along with a
	 * delegate which knows how to handle the given view, and a listener to be
	 * invoked when a refresh is started.
	 * 
	 * @param view View which will be used to initiate refresh requests.
	 * @param viewDelegate delegate which knows how to handle <code>view</code>.
	 * @param refreshListener Listener to be invoked when a refresh is started.
	 * @param setTouchListener Whether to set this as the
	 *            {@link android.view.View.OnTouchListener}.
	 */
	void addRefreshableView (View view, ViewDelegate viewDelegate, OnRefreshListener refreshListener, final boolean setTouchListener) {

		// Check to see if view is null
		if (view == null) {
			Log.i (LOG_TAG, "Refreshable View is null.");
			return;
		}

		if (refreshListener == null) {
			throw new IllegalArgumentException ("OnRefreshListener not given. Please provide one.");
		}

		// ViewDelegate
		if (viewDelegate == null) {
			viewDelegate = InstanceCreationUtils.getBuiltInViewDelegate (view);
			if (viewDelegate == null) {
				throw new IllegalArgumentException ("No view handler found. Please provide one.");
			}
		}

		// View to detect refreshes for
		mRefreshableViews.put (view, new ViewParams (viewDelegate, refreshListener));
		if (setTouchListener) {
			view.setOnTouchListener (this);
		}
	}

	/**
	 * Remove a view which was previously used to initiate refresh requests.
	 * 
	 * @param view - View which will be used to initiate refresh requests.
	 */
	public void removeRefreshableView (View view) {

		if (mRefreshableViews.containsKey (view)) {
			mRefreshableViews.remove (view);
			view.setOnTouchListener (null);
		}
	}

	/**
	 * Clear all views which were previously used to initiate refresh requests.
	 */
	public void clearRefreshableViews () {

		Set<View> views = mRefreshableViews.keySet ();
		for (View view : views) {
			view.setOnTouchListener (null);
		}
		mRefreshableViews.clear ();
	}

	/**
	 * This method should be called by your Activity's or Fragment's
	 * onConfigurationChanged method.
	 * 
	 * @param newConfig - The new configuration
	 */
	public void onConfigurationChanged (Configuration newConfig) {

		mHeaderTransformer.onViewCreated (mHeaderView);
	}

	/**
	 * Manually set this Attacher's refreshing state. The header will be
	 * displayed or hidden as requested.
	 * 
	 * @param refreshing - Whether the attacher should be in a refreshing state,
	 */
	public final void setRefreshing (boolean refreshing) {

		setRefreshingInt (null, refreshing, false);
	}

	/**
	 * Checks if is refreshing.
	 * 
	 * @return true if this Attacher is currently in a refreshing state.
	 */
	public final boolean isRefreshing () {

		return mIsRefreshing;
	}

	/**
	 * Checks if is enabled.
	 * 
	 * @return true if this PullToRefresh is currently enabled (defaults to
	 *         <code>true</code>)
	 */
	public boolean isEnabled () {

		return mEnabled;
	}

	/**
	 * Allows the enable/disable of this PullToRefreshAttacher. If disabled when
	 * refreshing then the UI is automatically reset.
	 * 
	 * @param enabled - Whether this PullToRefreshAttacher is enabled.
	 */
	public void setEnabled (boolean enabled) {

		mEnabled = enabled;

		if (!enabled) {
			// If we're not enabled, reset any touch handling
			resetTouch ();

			// If we're currently refreshing, reset the ptr UI
			if (mIsRefreshing) {
				reset (false);
			}
		}
	}

	/**
	 * Call this when your refresh is complete and this view should reset itself
	 * (header view will be hidden).
	 * 
	 * This is the equivalent of calling <code>setRefreshing(false)</code>.
	 */
	public final void setRefreshComplete () {

		setRefreshingInt (null, false, false);
	}

	/**
	 * Set a {@link HeaderViewListener} which is called when the visibility
	 * state of the Header View has changed.
	 * 
	 * @param listener the new header view listener
	 */
	public final void setHeaderViewListener (HeaderViewListener listener) {

		mHeaderViewListener = listener;
	}

	/**
	 * Gets the header view.
	 * 
	 * @return The Header View which is displayed when the user is pulling, or
	 *         we are refreshing.
	 */
	public final View getHeaderView () {

		return mHeaderView;
	}

	/**
	 * Gets the header transformer.
	 * 
	 * @return The HeaderTransformer currently used by this Attacher.
	 */
	public HeaderTransformer getHeaderTransformer () {

		return mHeaderTransformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public final boolean onTouch (final View view, final MotionEvent event) {

		if (!mIsHandlingTouchEvent && onInterceptTouchEvent (view, event)) {
			mIsHandlingTouchEvent = true;
		}

		if (mIsHandlingTouchEvent) {
			onTouchEvent (view, event);
		}

		// Always return false as we only want to observe events
		return false;
	}

	/**
	 * On intercept touch event.
	 * 
	 * @param view the view
	 * @param event the event
	 * @return true, if successful
	 */
	final boolean onInterceptTouchEvent (View view, MotionEvent event) {

		if (DEBUG) {
			Log.d (LOG_TAG, "onInterceptTouchEvent: " + event.toString ());
		}

		// If we're not enabled or currently refreshing don't handle any touch
		// events
		if (!isEnabled () || isRefreshing ()) {
			return false;
		}

		final ViewParams params = mRefreshableViews.get (view);
		if (params == null) {
			return false;
		}

		switch (event.getAction ()) {
			case MotionEvent.ACTION_MOVE: {
				// We're not currently being dragged so check to see if the user
				// has
				// scrolled enough
				if (!mIsBeingDragged && mInitialMotionY > 0) {
					final int y = (int) event.getY ();
					final int yDiff = y - mInitialMotionY;

					if (yDiff > mTouchSlop) {
						mIsBeingDragged = true;
						onPullStarted (y);
					}
					else if (yDiff < -mTouchSlop) {
						resetTouch ();
					}
				}
				break;
			}

			case MotionEvent.ACTION_DOWN: {
				// If we're already refreshing, ignore
				if (canRefresh (true, params.onRefreshListener) && params.viewDelegate.isScrolledToTop (view)) {
					mInitialMotionY = (int) event.getY ();
				}
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				resetTouch ();
				break;
			}
		}

		return mIsBeingDragged;
	}

	/**
	 * On touch event.
	 * 
	 * @param view the view
	 * @param event the event
	 * @return true, if successful
	 */
	final boolean onTouchEvent (View view, MotionEvent event) {

		if (DEBUG) {
			Log.d (LOG_TAG, "onTouchEvent: " + event.toString ());
		}

		// If we're not enabled or currently refreshing don't handle any touch
		// events
		if (!isEnabled ()) {
			return false;
		}

		final ViewParams params = mRefreshableViews.get (view);
		if (params == null) {
			return false;
		}

		switch (event.getAction ()) {
			case MotionEvent.ACTION_MOVE: {
				// If we're already refreshing ignore it
				if (isRefreshing ()) {
					return false;
				}

				final int y = (int) event.getY ();

				if (mIsBeingDragged && y != mLastMotionY) {
					final int yDx = y - mLastMotionY;

					/**
					 * Check to see if the user is scrolling the right direction
					 * (down). We allow a small scroll up which is the check
					 * against negative touch slop.
					 */
					if (yDx >= -mTouchSlop) {
						onPull (view, y);
						// Only record the y motion if the user has scrolled
						// down.
						if (yDx > 0) {
							mLastMotionY = y;
						}
					}
					else {
						onPullEnded ();
						resetTouch ();
					}
				}
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				checkScrollForRefresh (view);
				if (mIsBeingDragged) {
					onPullEnded ();
				}
				resetTouch ();
				break;
			}
		}

		return true;
	}

	/**
	 * Reset touch.
	 */
	void resetTouch () {

		mIsBeingDragged = false;
		mIsHandlingTouchEvent = false;
		mInitialMotionY = mLastMotionY = mPullBeginY = -1;
	}

	/**
	 * On pull started.
	 * 
	 * @param y the y
	 */
	void onPullStarted (int y) {

		if (DEBUG) {
			Log.d (LOG_TAG, "onPullStarted");
		}
		showHeaderView ();
		mPullBeginY = y;
	}

	/**
	 * On pull.
	 * 
	 * @param view the view
	 * @param y the y
	 */
	void onPull (View view, int y) {

		if (DEBUG) {
			Log.d (LOG_TAG, "onPull");
		}

		final float pxScrollForRefresh = getScrollNeededForRefresh (view);
		final int scrollLength = y - mPullBeginY;

		if (scrollLength < pxScrollForRefresh) {
			mHeaderTransformer.onPulled (scrollLength / pxScrollForRefresh);
		}
		else {
			if (mRefreshOnUp) {
				mHeaderTransformer.onReleaseToRefresh ();
			}
			else {
				setRefreshingInt (view, true, true);
			}
		}
	}

	/**
	 * On pull ended.
	 */
	void onPullEnded () {

		if (DEBUG) {
			Log.d (LOG_TAG, "onPullEnded");
		}
		if (!mIsRefreshing) {
			reset (true);
		}
	}

	/**
	 * Show header view.
	 */
	void showHeaderView () {

		if (mHeaderView.getVisibility () != View.VISIBLE) {
			// Show Header
			if (mHeaderInAnimation != null) {
				// AnimationListener will call HeaderViewListener
				mHeaderView.startAnimation (mHeaderInAnimation);
			}
			else {
				// Call HeaderViewListener now as we have no animation
				if (mHeaderViewListener != null) {
					mHeaderViewListener.onStateChanged (mHeaderView, HeaderViewListener.STATE_VISIBLE);
				}
			}
			mHeaderView.setVisibility (View.VISIBLE);
		}
	}

	/**
	 * Hide header view.
	 */
	void hideHeaderView () {

		if (mHeaderView.getVisibility () != View.GONE) {
			// Hide Header
			if (mHeaderOutAnimation != null) {
				// AnimationListener will call HeaderTransformer and
				// HeaderViewListener
				mHeaderView.startAnimation (mHeaderOutAnimation);
			}
			else {
				// As we're not animating, hide the header + call the header
				// transformer now
				mHeaderView.setVisibility (View.GONE);
				mHeaderTransformer.onReset ();

				if (mHeaderViewListener != null) {
					mHeaderViewListener.onStateChanged (mHeaderView, HeaderViewListener.STATE_HIDDEN);
				}
			}
		}
	}

	/**
	 * Creates the default environment delegate.
	 * 
	 * @return the environment delegate
	 */
	protected EnvironmentDelegate createDefaultEnvironmentDelegate () {

		return new EnvironmentDelegate ();
	}

	/**
	 * Creates the default header transformer.
	 * 
	 * @return the header transformer
	 */
	protected HeaderTransformer createDefaultHeaderTransformer () {

		return new DefaultHeaderTransformer ();
	}

	/**
	 * Check scroll for refresh.
	 * 
	 * @param view the view
	 * @return true, if successful
	 */
	private boolean checkScrollForRefresh (View view) {

		if (mIsBeingDragged && mRefreshOnUp && view != null) {
			if (mLastMotionY - mPullBeginY >= getScrollNeededForRefresh (view)) {
				setRefreshingInt (view, true, true);
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the refreshing int.
	 * 
	 * @param view the view
	 * @param refreshing the refreshing
	 * @param fromTouch the from touch
	 */
	private void setRefreshingInt (View view, boolean refreshing, boolean fromTouch) {

		if (DEBUG) {
			Log.d (LOG_TAG, "setRefreshingInt: " + refreshing);
		}
		// Check to see if we need to do anything
		if (mIsRefreshing == refreshing) {
			return;
		}

		resetTouch ();

		if (refreshing && canRefresh (fromTouch, getRefreshListenerForView (view))) {
			startRefresh (view, fromTouch);
		}
		else {
			reset (fromTouch);
		}
	}

	/**
	 * Gets the refresh listener for view.
	 * 
	 * @param view the view
	 * @return the refresh listener for view
	 */
	private OnRefreshListener getRefreshListenerForView (View view) {

		if (view != null) {
			ViewParams params = mRefreshableViews.get (view);
			if (params != null) {
				return params.onRefreshListener;
			}
		}
		return null;
	}

	/**
	 * Can refresh.
	 * 
	 * @param fromTouch - Whether this is being invoked from a touch event
	 * @param listener the listener
	 * @return true if we're currently in a state where a refresh can be
	 *         started.
	 */
	private boolean canRefresh (boolean fromTouch, OnRefreshListener listener) {

		return !mIsRefreshing && (!fromTouch || listener != null);
	}

	/**
	 * Gets the scroll needed for refresh.
	 * 
	 * @param view the view
	 * @return the scroll needed for refresh
	 */
	private float getScrollNeededForRefresh (View view) {

		return view.getHeight () * mRefreshScrollDistance;
	}

	/**
	 * Reset.
	 * 
	 * @param fromTouch the from touch
	 */
	private void reset (boolean fromTouch) {

		// Update isRefreshing state
		mIsRefreshing = false;

		// Remove any minimize callbacks
		if (mRefreshMinimize) {
			mHandler.removeCallbacks (mRefreshMinimizeRunnable);
		}

		// Hide Header View
		hideHeaderView ();
	}

	/**
	 * Start refresh.
	 * 
	 * @param view the view
	 * @param fromTouch the from touch
	 */
	private void startRefresh (View view, boolean fromTouch) {

		// Update isRefreshing state
		mIsRefreshing = true;

		// Call OnRefreshListener if this call has originated from a touch event
		if (fromTouch) {
			OnRefreshListener listener = getRefreshListenerForView (view);
			if (listener != null) {
				listener.onRefreshStarted (view);
			}
		}

		// Call Transformer
		mHeaderTransformer.onRefreshStarted ();

		// Show Header View
		showHeaderView ();

		// Post a delay runnable to minimize the refresh header
		if (mRefreshMinimize) {
			mHandler.postDelayed (mRefreshMinimizeRunnable, mRefreshMinimizeDelay);
		}
	}

	/**
	 * Simple Listener to listen for any callbacks to Refresh.
	 * 
	 * @see OnRefreshEvent
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the user has initiated a refresh by pulling.
		 * 
		 * @param view - View which the user has started the refresh from.
		 */
		public void onRefreshStarted (View view);
	}

	/**
	 * The listener interface for receiving headerView events. The class that is
	 * interested in processing a headerView event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addHeaderViewListener<code> method. When
	 * the headerView event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see HeaderViewEvent
	 */
	public interface HeaderViewListener {
		/**
		 * The state when the header view is completely visible.
		 */
		public static int STATE_VISIBLE   = 0;

		/**
		 * The state when the header view is minimized. By default this means
		 * that the progress bar is still visible, but the rest of the view is
		 * hidden, showing the Action Bar behind.
		 * <p/>
		 * This will not be called in header minimization is disabled.
		 */
		public static int STATE_MINIMIZED = 1;

		/**
		 * The state when the header view is completely hidden.
		 */
		public static int STATE_HIDDEN    = 2;

		/**
		 * Called when the visibility state of the Header View has changed.
		 * 
		 * @param headerView HeaderView who's state has changed.
		 * @param state The new state. One of {@link #STATE_VISIBLE},
		 *            {@link #STATE_MINIMIZED} and {@link #STATE_HIDDEN}
		 */
		public void onStateChanged (View headerView, int state);
	}

	/**
	 * The Class HeaderTransformer.
	 */
	public static abstract class HeaderTransformer {

		/**
		 * Called whether the header view has been inflated from the resources
		 * defined in {@link Options#headerLayout}.
		 * 
		 * @param activity The {@link Activity} that the header view is attached
		 *            to.
		 * @param headerView The inflated header view.
		 */
		public void onViewCreated (Activity activity, View headerView) {

		}

		/**
		 * On view created.
		 * 
		 * @param headerView the header view
		 * @deprecated This will be removed before v1.0. Override
		 *             {@link #onViewCreated(android.app.Activity, android.view.View)}
		 *             instead.
		 */
		public void onViewCreated (View headerView) {

		}

		/**
		 * Called when the header should be reset. You should update any child
		 * views to reflect this.
		 * <p/>
		 * You should <strong>not</strong> change the visibility of the header
		 * view.
		 */
		public void onReset () {

		}

		/**
		 * Called the user has pulled on the scrollable view.
		 * 
		 * @param percentagePulled - value between 0.0f and 1.0f depending on
		 *            how far the user has pulled.
		 */
		public void onPulled (float percentagePulled) {

		}

		/**
		 * Called when a refresh has begun. Theoretically this call is similar
		 * to that provided from {@link OnRefreshListener} but is more suitable
		 * for header view updates.
		 */
		public void onRefreshStarted () {

		}

		/**
		 * Called when a refresh can be initiated when the user ends the touch
		 * event. This is only called when {@link Options#refreshOnUp} is set to
		 * true.
		 */
		public void onReleaseToRefresh () {

		}

		/**
		 * Called when the current refresh has taken longer than the time
		 * specified in {@link Options#refreshMinimizeDelay}.
		 */
		public void onRefreshMinimized () {

		}
	}

	/**
	 * FIXME.
	 */
	public static abstract class ViewDelegate {

		/**
		 * Allows you to provide support for View which do not have built-in
		 * support. In this method you should cast <code>view</code> to it's
		 * native class, and check if it is scrolled to the top.
		 * 
		 * @param view The view which has should be checked against.
		 * @return true if <code>view</code> is scrolled to the top.
		 */
		public abstract boolean isScrolledToTop (View view);
	}

	/**
	 * FIXME.
	 */
	public static class EnvironmentDelegate {

		/**
		 * Gets the context for inflater.
		 * 
		 * @param activity the activity
		 * @return Context which should be used for inflating the header layout
		 */
		public Context getContextForInflater (Activity activity) {

			if (Build.VERSION.SDK_INT >= 14) {
				return activity.getActionBar ().getThemedContext ();
			}
			else {
				return activity;
			}
		}
	}

	/**
	 * The Class Options.
	 */
	public static class Options {

		/**
		 * EnvironmentDelegate instance which will be used. If null, we will
		 * create an instance of the default class.
		 */
		public EnvironmentDelegate environmentDelegate   = null;

		/**
		 * The layout resource ID which should be inflated to be displayed above
		 * the Action Bar.
		 */
		public int                 headerLayout          = DEFAULT_HEADER_LAYOUT;

		/**
		 * The header transformer to be used to transfer the header view. If
		 * null, an instance of {@link DefaultHeaderTransformer} will be used.
		 */
		public HeaderTransformer   headerTransformer     = null;

		/**
		 * The anim resource ID which should be started when the header is being
		 * hidden.
		 */
		public int                 headerOutAnimation    = DEFAULT_ANIM_HEADER_OUT;

		/**
		 * The anim resource ID which should be started when the header is being
		 * shown.
		 */
		public int                 headerInAnimation     = DEFAULT_ANIM_HEADER_IN;

		/**
		 * The percentage of the refreshable view that needs to be scrolled
		 * before a refresh is initiated.
		 */
		public float               refreshScrollDistance = DEFAULT_REFRESH_SCROLL_DISTANCE;

		/**
		 * Whether a refresh should only be initiated when the user has finished
		 * the touch event.
		 */
		public boolean             refreshOnUp           = DEFAULT_REFRESH_ON_UP;

		/**
		 * The delay after a refresh is started in which the header should be
		 * 'minimized'. By default, most of the header is faded out, leaving
		 * only the progress bar signifying that a refresh is taking place.
		 */
		public int                 refreshMinimizeDelay  = DEFAULT_REFRESH_MINIMIZED_DELAY;

		/**
		 * Enable or disable the header 'minimization', which by default means
		 * that the majority of the header is hidden, leaving only the progress
		 * bar still showing.
		 * <p/>
		 * If set to true, the header will be minimized after the delay set in
		 * {@link #refreshMinimizeDelay}. If set to false then the whole header
		 * will be displayed until the refresh is finished.
		 */
		public boolean             refreshMinimize       = DEFAULT_REFRESH_MINIMIZE;
	}

	/**
	 * The Class AnimationCallback.
	 */
	private class AnimationCallback implements Animation.AnimationListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.animation.Animation.AnimationListener#onAnimationStart
		 * (android.view.animation.Animation)
		 */
		@Override
		public void onAnimationStart (Animation animation) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.animation.Animation.AnimationListener#onAnimationEnd
		 * (android.view.animation.Animation)
		 */
		@Override
		public void onAnimationEnd (Animation animation) {

			if (animation == mHeaderOutAnimation) {
				mHeaderView.setVisibility (View.GONE);
				mHeaderTransformer.onReset ();
				if (mHeaderViewListener != null) {
					mHeaderViewListener.onStateChanged (mHeaderView, HeaderViewListener.STATE_HIDDEN);
				}
			}
			else if (animation == mHeaderInAnimation) {
				if (mHeaderViewListener != null) {
					mHeaderViewListener.onStateChanged (mHeaderView, HeaderViewListener.STATE_VISIBLE);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.animation.Animation.AnimationListener#onAnimationRepeat
		 * (android.view.animation.Animation)
		 */
		@Override
		public void onAnimationRepeat (Animation animation) {

		}
	}

	/**
	 * This class allows us to insert a layer in between the system decor view
	 * and the actual decor. (e.g. Action Bar views). This is needed so we can
	 * receive a call to fitSystemWindows(Rect) so we can adjust the header view
	 * to fit the system windows too.
	 */
	final static class DecorChildLayout extends FrameLayout {

		/** The m header view wrapper. */
		private final ViewGroup mHeaderViewWrapper;

		/**
		 * Instantiates a new decor child layout.
		 * 
		 * @param context the context
		 * @param systemDecorView the system decor view
		 * @param headerView the header view
		 */
		DecorChildLayout (Context context, ViewGroup systemDecorView, View headerView) {

			super (context);

			// Move all children from decor view to here
			for (int i = 0, z = systemDecorView.getChildCount (); i < z; i++) {
				View child = systemDecorView.getChildAt (i);
				systemDecorView.removeView (child);
				addView (child);
			}

			/**
			 * Wrap the Header View in a FrameLayout and add it to this view. It
			 * is wrapped so any inset changes do not affect the actual header
			 * view.
			 */
			mHeaderViewWrapper = new FrameLayout (context);
			mHeaderViewWrapper.addView (headerView);
			addView (mHeaderViewWrapper, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
		 */
		@Override
		protected boolean fitSystemWindows (Rect insets) {

			if (DEBUG) {
				Log.d (LOG_TAG, "fitSystemWindows: " + insets.toString ());
			}

			// Adjust the Header View's padding to take the insets into account
			mHeaderViewWrapper.setPadding (insets.left, insets.top, insets.right, insets.bottom);

			// Call return super so that the rest of the
			return super.fitSystemWindows (insets);
		}
	}

	/**
	 * The Class ViewParams.
	 */
	private static final class ViewParams {

		/** The on refresh listener. */
		final OnRefreshListener onRefreshListener;

		/** The view delegate. */
		final ViewDelegate      viewDelegate;

		/**
		 * Instantiates a new view params.
		 * 
		 * @param _viewDelegate the _view delegate
		 * @param _onRefreshListener the _on refresh listener
		 */
		ViewParams (ViewDelegate _viewDelegate, OnRefreshListener _onRefreshListener) {

			onRefreshListener = _onRefreshListener;
			viewDelegate = _viewDelegate;
		}
	}

	/** The m refresh minimize runnable. */
	private final Runnable mRefreshMinimizeRunnable = new Runnable () {
		                                                @Override
		                                                public void run () {

			                                                mHeaderTransformer.onRefreshMinimized ();

			                                                if (mHeaderViewListener != null) {
				                                                mHeaderViewListener.onStateChanged (mHeaderView, HeaderViewListener.STATE_MINIMIZED);
			                                                }
		                                                }
	                                                };

}
