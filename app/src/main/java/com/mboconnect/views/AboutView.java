package com.mboconnect.views;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.mboconnect.R;
import com.mboconnect.activities.AboutActivity;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.Button;
import com.tenpearls.android.components.EditText;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.Locale;

/**
 * Created by tahir on 08/06/15.
 */
public class AboutView extends BaseView {

	Context          context;
	private TextView txtVersion;

	public AboutView (Context context) {

		super (context);
		this.context = context;
	}

	@Override
	public void onCreate () {

		initUi ();
	}

	@Override
	public void setActionListeners () {

	}

	@Override
	public int getViewLayout () {

		return R.layout.view_about;
	}

	private void initUi () {

		txtVersion = (TextView) view.findViewById (R.id.txtVersion);
		txtVersion.setText (getVersionString ());
	}

	private String getVersionString () {

		String versionName = Utils.getVersionName (((AboutActivity) context));
		String versionCode = " (" + String.valueOf (Utils.getVersionCode (((AboutActivity) context))) + ")";
		String versionString = AppConstants.VERSION_INFO;
		String copyrightString = Utils.getString (R.string.rights_reserved);

		versionString = versionString.replace ("[[VERSION_NAME]]", versionName);
		versionString = versionString.replace ("[[VERSION_CODE]]", versionCode);
		return versionString;
	}
}
