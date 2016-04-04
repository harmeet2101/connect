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

import com.tenpearls.android.entities.OAuth20TokenExt;
import com.tenpearls.android.network.CustomHttpResponse;

/**
 * Interface definition for callback methods to be invoked in OAuth 2.0 flow.
 * These events are invoked from inside the OAuth20Gateway class.
 * 
 * @author 10Pearls
 * 
 */
public interface OAuth20TokenListener {

	/**
	 * Called when the Access Token is received successfully.
	 * 
	 * @param token The OAuthTokenExt object containing the access token.
	 */
	public void onOAuthAccessTokenReceived (OAuth20TokenExt token);

	/**
	 * Called when there is an error in receiving the Access token.
	 * 
	 * @param httpError The CustomHttpError object containing error details.
	 */
	public void onAccessTokenRequestFailed (CustomHttpResponse httpError);

}
