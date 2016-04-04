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

import android.app.Application;
import android.content.Context;
import android.app.DialogFragment;

import com.tenpearls.android.R;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.utilities.PreferenceUtility;

/**
 * A dialog which shows options to rate the application on Play Store.
 * 
 * <p>
 * For details see <a
 * href="https://github.com/sbstrm/appirater-android">Appirater GitHub Page</a>
 * </p>
 * 
 * @author 10Pearls
 */
public class AppRater {

	/** The Constant PREF_LAUNCH_COUNT. */
	public static final String PREF_LAUNCH_COUNT          = "launch_count";

	/** The Constant PREF_APP_VERSION_CODE. */
	public static final String PREF_APP_VERSION_CODE      = "versioncode";

	/** The Constant PREF_DATE_FIRST_LAUNCHED. */
	public static final String PREF_DATE_FIRST_LAUNCHED   = "date_firstlaunch";

	/** The Constant PREF_RATE_CLICKED. */
	public static final String PREF_RATE_CLICKED          = "rateclicked";

	/** The Constant PREF_DATE_REMINDER_PRESSED. */
	public static final String PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed";

	/** The Constant PREF_DONT_SHOW. */
	public static final String PREF_DONT_SHOW             = "dontshow";

	/**
	 * Call this method inside {@link Application}'s {@code onCreate()} method.
	 * 
	 * @param mContext A valid context.
	 */
	public static void appLaunched (Context mContext) {

		int launchCount = PreferenceUtility.getInteger (mContext, PREF_LAUNCH_COUNT, 0);
		launchCount++;
		PreferenceUtility.setInteger (mContext, PREF_LAUNCH_COUNT, launchCount);

		boolean testMode = mContext.getResources ().getBoolean (R.bool.appirator_test_mode);
		boolean isDontShow = PreferenceUtility.getBoolean (mContext, PREF_DONT_SHOW, false);
		boolean isRateClicked = PreferenceUtility.getBoolean (mContext, PREF_RATE_CLICKED, false);

		if (!testMode && (isDontShow || isRateClicked)) {
			return;
		}

		if (testMode) {
			showRateDialog (mContext);
			return;
		}

		long firstLaunchDate = PreferenceUtility.getLong (mContext, PREF_DATE_FIRST_LAUNCHED, 0);
		long dateReminderPressed = PreferenceUtility.getLong (mContext, PREF_DATE_REMINDER_PRESSED, 0);

		try {
			int appVersionCode = mContext.getPackageManager ().getPackageInfo (mContext.getPackageName (), 0).versionCode;
			int appVersionCodeInPrefs = PreferenceUtility.getInteger (mContext, PREF_APP_VERSION_CODE, 0);

			if (appVersionCodeInPrefs != appVersionCode) {
				launchCount = 0;
			}

			PreferenceUtility.setInteger (mContext, PREF_APP_VERSION_CODE, appVersionCode);
		}
		catch (Exception e) {
			// do nothing
		}

		if (firstLaunchDate == 0) {
			firstLaunchDate = System.currentTimeMillis ();
			PreferenceUtility.setLong (mContext, PREF_DATE_FIRST_LAUNCHED, firstLaunchDate);
		}

		// Wait at least n days before opening
		if (launchCount >= mContext.getResources ().getInteger (R.integer.appirator_launches_until_prompt)) {
			long millisecondsToWait = mContext.getResources ().getInteger (R.integer.appirator_days_until_prompt) * 24 * 60 * 60 * 1000L;
			if (System.currentTimeMillis () >= (firstLaunchDate + millisecondsToWait)) {
				if (dateReminderPressed == 0) {
					showRateDialog (mContext);
				}
				else {
					long remindMillisecondsToWait = mContext.getResources ().getInteger (R.integer.appirator_days_before_reminding) * 24 * 60 * 60 * 1000L;
					if (System.currentTimeMillis () >= (remindMillisecondsToWait + dateReminderPressed)) {
						showRateDialog (mContext);
					}
				}
			}
		}
	}

	/**
	 * Called when it is determined that we do need to show a dialog. For
	 * internal use only.
	 * 
	 * @param mContext A valid context.
	 */
	private static void showRateDialog (final Context mContext) {

		AppRaterDialog appRaterDialog = new AppRaterDialog ();
		appRaterDialog.setContext (mContext);
		appRaterDialog.setStyle (DialogFragment.STYLE_NO_TITLE, 0);
		appRaterDialog.show (((BaseActionBarActivity) mContext).getFragmentManager (), "dialog");
	}
}