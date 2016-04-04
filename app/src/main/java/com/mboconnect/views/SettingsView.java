package com.mboconnect.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mboconnect.R;
import com.mboconnect.activities.AboutActivity;
import com.mboconnect.activities.ProfileActivity;
import com.mboconnect.activities.SettingsActivity;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.managers.ArtisanHooksManager;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;

/**
 * Created by tahir on 08/06/15.
 */
public class SettingsView extends BaseView {

	Context			  context;
	RelativeLayout	  itemAbout,
	        itemProfile, itemTerms,
	        itemPolicy;
	public static RelativeLayout settingsLayout;
	private ImageView btnBackToolBar;

	public SettingsView (Context context) {

		super(context);
		this.context = context;
	}

	@Override
	public void onCreate () {

		initUi ();
	}

	@Override
	public void setActionListeners () {

		btnBackToolBar.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View v) {

				((SettingsActivity) controller).onBackPressed ();
			}
		});

		itemAbout.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View v) {

				navigateToActivity(AboutActivity.class);
			}
		});

		itemProfile.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View v) {

				navigateToActivity (ProfileActivity.class);
			}
		});

		itemTerms.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View v) {

				EnvironmentConstants.URL_TERMS_CONDITION= ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_TERMS_AND_CONDITIONS,ArtisanHooksManager.DEFAULT_TERMS_AND_CONDITIONS);
				Boolean isURLValid=URLUtil.isValidUrl(EnvironmentConstants.URL_TERMS_CONDITION);
				if(isURLValid){
				Utils.openBrowserIntent(EnvironmentConstants.URL_TERMS_CONDITION, context);
				}
				else{
					Toast.makeText(context,"Invalid URL",Toast.LENGTH_SHORT).show();
				}
			}
		});

        itemPolicy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EnvironmentConstants.URL_PRIVACY_POLICY = ArtisanHooksManager.getHookForKeyWithDefaultValue(ArtisanHooksManager.HOOK_PRIVACY_POLICY, ArtisanHooksManager.DEFAULT_PRIVACY_POLICY);
				Boolean isURLValid = URLUtil.isValidUrl(EnvironmentConstants.URL_PRIVACY_POLICY);
				if (isURLValid) {

					Utils.openBrowserIntent(EnvironmentConstants.URL_PRIVACY_POLICY, context);
				} else {
					Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public int getViewLayout () {

		return R.layout.view_settings;
	}

	private void initUi () {

		setupToolBar ();
		itemAbout = (RelativeLayout) view.findViewById (R.id.itemAbout);
		itemProfile = (RelativeLayout) view.findViewById (R.id.itemProfile);
		itemTerms = (RelativeLayout) view.findViewById (R.id.itemTerms);
		itemPolicy = (RelativeLayout) view.findViewById (R.id.itemPolicy);
		settingsLayout=(RelativeLayout)view.findViewById(R.id.settingsLayout);
	}

	private void setupToolBar () {

		Toolbar toolbarSettings = (Toolbar) view.findViewById (R.id.toolbarSettings);
		((SettingsActivity) controller).setSupportActionBar (toolbarSettings);
		((SettingsActivity) controller).getSupportActionBar ().setDisplayShowTitleEnabled (false);
		btnBackToolBar = (ImageView) view.findViewById (R.id.btnBackToolBar);
		TextView txtScreenName = (TextView) view.findViewById (R.id.txtScreenName);
		txtScreenName.setText (context.getResources ().getString (R.string.settings));
	}

	private void navigateToActivity (Class<?> className) {

		Intent intent = new Intent (context, className);
		context.startActivity (intent);
	}
}
