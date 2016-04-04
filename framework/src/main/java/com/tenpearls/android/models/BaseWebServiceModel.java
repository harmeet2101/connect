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
package com.tenpearls.android.models;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.Request;
import com.tenpearls.android.R;
import com.tenpearls.android.interfaces.HttpResponseListener;
import com.tenpearls.android.managers.ConfigurationManager;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.LogUtility;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Blue-print for implementing a Web service model/layer. All API/Network
 * requests must originate from a subclass of {@code BaseWebServiceModel}.
 * 
 * @author 10Pearls
 * 
 */
public abstract class BaseWebServiceModel {

	private final String CONFIGURATION_KEY_API_BASE_URL = "api_base_url";

	/**
	 * Implementer must return a context.
	 * 
	 * @return A valid context
	 */
	protected abstract Context getContext ();

	/**
	 * To show a view to indicate that the internet is not working, return.
	 * 
	 * @return A boolean to indicate if a view should be displayed. {@code true}
	 *         .
	 */
	protected abstract boolean shouldShowInternetNotWorkingView ();

	/** The internet not found view. */
	private View internetNotFoundView = null;

	/**
	 * Network call must be dispatched using this method. It does some
	 * house-keeping like checking a valid network connection etc, and adds the
	 * network call to the request queue.
	 * 
	 * @param request Volley {@link Request} object
	 */
	protected void executeRequest (Request<?> request) {

		if (DeviceUtility.isInternetConnectionAvailable (getContextInternal ())) {
			VolleyManager.getInstance ().getRequestQueue ().add (request);
		}
		else if (shouldShowInternetNotWorkingView ()) {
			showInternetConnectionNotFoundMessage ();
		}
	}

	/**
	 * Call this method to prefix the string passed with the Web service base
	 * url defined in 'application_configurations.json' file.
	 * 
	 * @param method API method
	 * @return Complete URL by prefixing Web service base url to the API method.
	 */
	protected String getCompleteUrlForMethod (String method) {

		String baseUrl = ConfigurationManager.getInstance ().getValueForKey (CONFIGURATION_KEY_API_BASE_URL);

		return baseUrl + method;
	}

	/**
	 * Call this method to prefix the string passed with the Web service base
	 * url defined in 'application_configurations.json' file.
	 * 
	 * @param method API method
	 * @param baseUrlConfigurationKey The json key which defines the base API
	 *            URL value.
	 * @return Complete URL by prefixing Web service base url to the API method.
	 */
	protected String getCompleteUrlForMethod (String baseUrlConfigurationKey, String method) {

		if (StringUtility.isEmptyOrNull (baseUrlConfigurationKey)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> API base url configuration key not provided in application_configurations.json");
		}

		String baseUrl = ConfigurationManager.getInstance ().getValueForKey (baseUrlConfigurationKey);

		return baseUrl + method;
	}

	/**
	 * You must call this method to notify implementors of the network success.
	 * Do not call {@code HttpResponseListener.onHttpSuccess} directly.
	 * 
	 * @param listener An object which implements {@link HttpResponseListener}.
	 * @param response A {@link CustomHttpResponse} object which stores data
	 *            returned from the network call.
	 */
	protected void fireSuccessListener (HttpResponseListener listener, CustomHttpResponse response) {

		LogUtility.log (Log.ERROR, this.getClass ().getSimpleName (), response.getErrorMessage ());

		if (listener != null) {
			listener.onHttpSuccess (response);
		}
	}

	/**
	 * You must call this method to notify implementors of the network errors.
	 * Do not call {@code HttpResponseListener.onHttpError} directly.
	 * 
	 * @param listener An object which implements {@link HttpResponseListener}.
	 * @param error A {@link CustomHttpError} object which stores the error
	 *            while executing the network call.
	 */
	protected void fireErrorListener (HttpResponseListener listener, CustomHttpResponse error) {

		if (listener != null) {
			listener.onHttpError (error);
		}
	}

	/**
	 * Gets the context internal.
	 * 
	 * @return the context internal
	 */
	private Context getContextInternal () {

		Context context = getContext ();

		if (context == null) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Please provide a Context before issuing any web requests");
		}

		return context;
	}

	/**
	 * Return the layout ID of the view to be shown to indicate Internet
	 * problems.
	 * 
	 * @return the internet not found view layout id
	 */
	protected int getInternetNotFoundViewLayoutId () {

		return R.layout.internet_not_found;
	}

	/**
	 * This method is responsible for showing a view to represent failure to
	 * reach internet. You must override this method if you want to provide a
	 * custom implementation.
	 * 
	 * Note: This method will only be executed if
	 * {@code shouldShowInternetNotWorkingView} returns {@code true}
	 */
	protected void showInternetConnectionNotFoundMessage () {

		if (internetNotFoundView != null)
			return;

		LayoutInflater layoutInflator = (LayoutInflater) getContextInternal ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		internetNotFoundView = layoutInflator.inflate (getInternetNotFoundViewLayoutId (), null);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

		params.y = DeviceUtility.getPixelsFromDps (47, getContextInternal ());
		params.gravity = Gravity.RIGHT | Gravity.TOP;
		WindowManager wm = (WindowManager) getContextInternal ().getSystemService (Context.WINDOW_SERVICE);
		wm.addView (internetNotFoundView, params);

		Runnable mRunnable;
		Handler mHandler = new Handler ();

		mRunnable = new Runnable () {

			@Override
			public void run () {

				internetNotFoundView.setVisibility (View.INVISIBLE);
				internetNotFoundView.setVisibility (View.GONE);
				internetNotFoundView = null;
			}
		};

		mHandler.postDelayed (mRunnable, 5 * 1000);
	}

}
