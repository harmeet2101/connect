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
 * Interface definition for callback methods to be invoked in Facebook Helper when opening Facebook Session.
 *
 * @author 10Pearls
 * 
 */

public interface FacebookSessionStatusChangeListener<T> {
	
	/**
	 * Called when the Facebook Session Status changes
	 * 
	 * @param session The Session object from the Facebook SDK
	 * @param exception The Exception object if any occurred when opening Session
	 */
	
	public abstract void onSessionStatusChange(T session, Exception exception);

}
