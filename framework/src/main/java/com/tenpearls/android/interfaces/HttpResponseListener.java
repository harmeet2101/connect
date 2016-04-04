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

import com.tenpearls.android.models.BaseWebServiceModel;
import com.tenpearls.android.network.CustomHttpResponse;

/**
 * Interface definition for callbacks to be invoked in response to a network
 * operation. Typically originated from a subclass of
 * {@link BaseWebServiceModel} using {@code fireSuccessListener} or
 * {@code fireErrorListener} methods.
 * 
 * @author 10Pearls
 * 
 */
public interface HttpResponseListener {

	/**
	 * Called when the network operation was completed successfully.
	 * 
	 * @param object An object of type {@link CustomHttpResponse} representing
	 *            server response.
	 */
	public void onHttpSuccess (CustomHttpResponse object);

	/**
	 * Called when there was an error contacting/processing the server response.
	 * 
	 * @param exception An object of type {@link CustomHttpError} representing
	 *            server/client error
	 */
	public void onHttpError (CustomHttpResponse error);
}
