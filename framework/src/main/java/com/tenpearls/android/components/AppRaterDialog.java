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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.tenpearls.android.R;
import com.tenpearls.android.utilities.PreferenceUtility;
import com.tenpearls.android.utilities.StringUtility;

/**
 * {@link DialogFragment}'s subclass, used to present the rating dialog options.
 * 
 * @author 10Pearls
 */
public class AppRaterDialog extends DialogFragment implements OnClickListener {

	/** The rate later. */
	private Button   rate, rateLater;

	/** The txt message. */
	private TextView txtTitle,
	        txtMessage;

	/** The m context. */
	private Context  mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate (R.layout.apprater, container, false);
		String appName = getActivity ().getString (R.string.appirator_app_title);

		if (StringUtility.isEmptyOrNull (appName)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Please specify 'appirator_app_title' in AppRater settings xml.");
		}

		txtMessage = (TextView) v.findViewById (R.id.message);
		txtTitle = (TextView) v.findViewById (R.id.title);
		rate = (Button) v.findViewById (R.id.rate);
		rateLater = (Button) v.findViewById (R.id.rateLater);

		rate.setText (String.format (mContext.getString (R.string.rate), appName));
		txtTitle.setText (String.format (getActivity ().getString (R.string.rate_title), appName));
		txtMessage.setText (String.format (getActivity ().getString (R.string.rate_message), appName));

		rate.setOnClickListener (this);
		rateLater.setOnClickListener (this);

		this.setCancelable (false);

		return v;
	}

	/**
	 * On rate latter.
	 */
	private void onRateLatter () {

		PreferenceUtility.setLong (mContext, AppRater.PREF_DATE_REMINDER_PRESSED, System.currentTimeMillis ());
		this.dismiss ();
	}

	/**
	 * On rate.
	 */
	private void onRate () {

		String marketUrl = mContext.getString (R.string.appirator_market_url);

		if (StringUtility.isEmptyOrNull (marketUrl)) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Please specify 'appirator_market_url' in AppRater settings xml.");
		}

		mContext.startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse (String.format (mContext.getString (R.string.appirator_market_url), mContext.getPackageName ()))));
		PreferenceUtility.setBoolean (mContext, AppRater.PREF_RATE_CLICKED, true);
		this.dismiss ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick (View v) {

		int id = v.getId ();
		if (id == R.id.rate) {
			onRate ();
		}
		else if (id == R.id.rateLater) {
			onRateLatter ();
		}
		else {
		}
	}

	/**
	 * Sets the context.
	 * 
	 * @param mContext the new context
	 */
	public void setContext (Context mContext) {

		this.mContext = mContext;
	}

}
