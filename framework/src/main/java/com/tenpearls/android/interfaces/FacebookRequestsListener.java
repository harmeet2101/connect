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

import java.util.List;

/**
 * Interface definition for callback methods to be invoked in Facebook Helper on completion of requests.
 * T is the GraphUser type from Facebook SDK
 * @author 10Pearls
 * 
 */

public interface FacebookRequestsListener<T> {
	
	/**
	 * Called when the Facebook User Data Request is completed
	 * 
	 * @param graphUser The GraphUser object returned by the Facebook SDK	 
	 */

	public void onGetUserCallCompleted(T graphUser);
	
	/**
	 * Called when the Facebook Friends Request is completed
	 * 
	 * @param friendsList The List of GraphUser objects returned by the Facebook SDK	 
	 */
	
	public void onGetFriendsCallCompleted(List<T> friendsList);
}
