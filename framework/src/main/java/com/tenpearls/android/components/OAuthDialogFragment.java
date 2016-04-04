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
package com.tenpearls.android.components;

import com.tenpearls.android.R;
import com.tenpearls.android.entities.base.BaseOAuthAPIConfig;
import com.tenpearls.android.interfaces.OAuth20TokenListener;
import com.tenpearls.android.managers.OAuth20Gateway;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * An OAuth20 Dialog class that used OAuth20Gateway class to implement the
 * OAuth20flow. The class mainly requires an OAuth20Api object to function and a
 * listener to receive the callback methods.
 * 
 * @author 10Pearls
 */
public class OAuthDialogFragment extends DialogFragment {

	/**
	 * Authorization code regular expression.
	 */
	static final String  CODE_REGEX   = "code=";

	/**
	 * Text to show in the loading dialog.
	 */
	static final String  LOADING_TEXT = "Loading...";

	/**
	 * WebView instance that does all the work.
	 */
	WebView              mWebView;

	/**
	 * If {@code true}, WebView is loading a page, {@code false} otherwise.
	 */
	boolean              mIsLoadingPage;

	/**
	 * The OAuth20Gateway to implement the OAuth 2.0 flow.
	 */
	OAuth20Gateway       mOAuth20Gateway;

	/**
	 * The listener to receive token receipt or failure events.
	 */
	OAuth20TokenListener mOAuth20TokenListener;

	/**
	 * The OAuth20API for which the flow is to be implemented.
	 */
	BaseOAuthAPIConfig         mOAuthAPIConfig;

	/**
	 * The Loading dialog for the WebView.
	 */
	ProgressDialog       mProgressDialog;

	/**
	 * Instantiates a new o auth dialog fragment.
	 */
	public OAuthDialogFragment () {

		mIsLoadingPage = true;
	}

	/**
	 * Set the OAuth20TokenListener that will receive the token receipt and
	 * failure methods.
	 * 
	 * @param oAuth20TokenListener The listener object that will receive the
	 *            token receipt and failure events
	 */
	public void setOAuth20TokenListener (OAuth20TokenListener oAuth20TokenListener) {

		this.mOAuth20TokenListener = oAuth20TokenListener;
	}

	/**
	 * Set the OAuth20Api for which the flow is being implemented.
	 * 
	 * @param oAuthAPI The OAuth20Api for which the flow is being implemented
	 */
	public void setOAuthApi (BaseOAuthAPIConfig oAuthAPIConfig) {

		this.mOAuthAPIConfig = oAuthAPIConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		checkForNullOAuthAPI ();

		View view = inflater.inflate (R.layout.fragment_oauth_dialog, container, false);
		getDialog ().getWindow ().requestFeature (Window.FEATURE_NO_TITLE);

		initWebView (view);
		initOAuthGateway ();
		loadURL ();

		return view;
	}

	/**
	 * This method initialized WebView of this Dialog.
	 * 
	 * @param view The View that contains WebView
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView (View view) {

		mWebView = (WebView) view.findViewById (R.id.webView);
		mWebView.getSettings ().setJavaScriptEnabled (true);
		mWebView.setWebViewClient (new OAuthWebViewClient ());

		CookieSyncManager.createInstance (getActivity ());
		CookieManager cookieManager = CookieManager.getInstance ();
		cookieManager.removeAllCookie ();
	}

	/**
	 * This method initializes OAuthGateway.
	 */
	private void initOAuthGateway () {

		mOAuth20Gateway = OAuth20Gateway.getInstance ();
		mOAuth20Gateway.initialize (mOAuthAPIConfig);
	}

	/**
	 * This method loads the Authorize Url from the given OAuth20Api.
	 */
	private void loadURL () {

		mIsLoadingPage = true;
		mWebView.loadUrl (mOAuth20Gateway.getAuthorizeUrl ());
		mWebView.clearCache (true);
	}

	/**
	 * This method asserts that the OAuth20API has been set. It throws an
	 * {@link NullPointerException} if the OAuth20API has not been set.
	 */
	private void checkForNullOAuthAPI () {

		if (mOAuthAPIConfig == null) {
			throw new NullPointerException ("OAuthDialogFragment >> OAuth20API can not be null. Set it using setOAuthApi method.");
		}
	}

	/**
	 * This method tells if the WebView is currently loading a page or not.
	 * 
	 * @return true, if is loading page
	 */
	public boolean isLoadingPage () {

		return mIsLoadingPage;
	}

	/**
	 * This is an inner class which subclass WebViewClient. It's object is used
	 * for the WebView of the Dialog.
	 * 
	 * @author 10Pearls
	 */
	class OAuthWebViewClient extends WebViewClient {

		/**
		 * This method has been overridden to check for the URL with the
		 * Authorization code.
		 * 
		 * @param view The Webview of this Client
		 * @param url The Url to be checked
		 * @return true, if successful
		 */
		@Override
		public boolean shouldOverrideUrlLoading (WebView view, String url) {

			if (url.contains (CODE_REGEX)) {
				try {
					showLoadingDialog ();

					OAuthDialogFragment.this.dismiss ();
					mOAuth20Gateway.getOAuthAccessToken (url, mOAuth20TokenListener);

				}
				catch (Exception e) {
					e.printStackTrace ();
				}
			}

			return super.shouldOverrideUrlLoading (view, url);
		}

		/**
		 * This method has been overridden to show the loading dialog when the
		 * Webview is about to load a resource.
		 * 
		 * @param view The Webview of this Client
		 * @param url The resource Url to be loaded
		 */
		@Override
		public void onLoadResource (WebView view, String url) {

			showLoadingDialog ();
		}

		/**
		 * This method has been overridden to dismiss the loading dialog when
		 * the Webview finished loading the page.
		 * 
		 * @param view The Webview of this Client
		 * @param url The page Url that has been loaded
		 */
		@Override
		public void onPageFinished (WebView view, String url) {

			dismissLoadingDialog ();
			mIsLoadingPage = false;
		}

	}

	/**
	 * This method is used to show the loading dialog on the WebView.
	 */
	private void showLoadingDialog () {

		try {
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog (getActivity ());
				mProgressDialog.setMessage (LOADING_TEXT);
				mProgressDialog.show ();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace ();
		}
	}

	/**
	 * This method is used to dismiss the loading dialog on the WebView.
	 */
	private void dismissLoadingDialog () {

		try {
			if (mProgressDialog.isShowing ()) {
				mProgressDialog.dismiss ();
				mProgressDialog = null;
			}
		}
		catch (Exception exception) {
			exception.printStackTrace ();
		}
	}
}
