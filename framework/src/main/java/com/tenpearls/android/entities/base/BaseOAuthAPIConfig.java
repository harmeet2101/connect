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
package com.tenpearls.android.entities.base;

import com.androauth.api.OAuth20Api;

/**
 * An OAuth 2.0 API abstract class that enforces implementation of all the OAuth 2.0 related methods.
 * 
 * @author 10Pearls
 */

public abstract class BaseOAuthAPIConfig implements OAuth20Api {
	
	/**
	 * This methods gets you the Client ID
	 * 
	 * @return String The Client ID
	 */

	abstract public String getClientID ();
	
	/**
	 * This methods gets you the Client Secret
	 * 
	 * @return String The Client Secret
	 */

	abstract public String getClientSecret ();
	
	/**
	 * This methods gets you the Redirect URI
	 * 
	 * @return String The Redirect URI
	 */

	abstract public String getRedirectURI ();
	
	/**
	 * This methods gets you the Grant Type for Code
	 * 
	 * @return String The Grant Type for Code
	 */

	abstract public String getGrantTypeCode ();
	
	/**
	 * This methods gets you the Grant Type for Refresh
	 * 
	 * @return String The Grant Type for Refresh
	 */

	abstract public String getGrantTypeRefresh ();

	/**
	 * This methods gets you the Service Scope
	 * 
	 * @return String The Service Scope
	 */
	
	abstract public String getServiceScope ();
	
	/**
	 * This methods gets you the Service Duration
	 * 
	 * @return String The Service Duration
	 */
	

	abstract public String getServiceDuration ();

}
