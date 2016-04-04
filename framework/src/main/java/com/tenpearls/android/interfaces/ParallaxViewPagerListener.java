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
package com.tenpearls.android.interfaces;

/**
 * The listener interface for receiving parallaxViewPager Page scrolled and
 * changed events. The class that is interested in processing a
 * parallaxViewPager event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addParallaxViewPagerListener</code> method. When the parallaxViewPager
 * event occurs, that object's appropriate method is invoked.
 * 
 * @see ParallaxViewPagerEvent
 */
public interface ParallaxViewPagerListener {

	/**
	 * Called when page scrolling state changes.
	 * 
	 * @param state The scrolling states of a page, i.e. 1 for page scrolling
	 */
	public void onScrollStarted (int state);

	/**
	 * Called when a page selected.
	 * 
	 * @param index The index of the selected page
	 */
	public void onPageSelected (int index);
}
