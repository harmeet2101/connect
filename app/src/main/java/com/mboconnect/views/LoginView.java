package com.mboconnect.views;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mboconnect.R;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.Button;
import com.tenpearls.android.components.EditText;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

/**
 * Created by tahir on 08/06/15.
 */
public class LoginView extends BaseView {

	Context        context;
	EditText       eTxtPassword, eTxtUserName;
	private Button btnForgot;
	private Button btnLogin;
	private TextView txtErrorMessage;
	public static TextView enterpriseUserError;
	public static boolean enterpriseUser=false;
	public static LinearLayout layoutLogin;
	public static android.widget.ImageView mboLogo;



	public LoginView (Context context) {

		super (context);
		this.context = context;
	}

	@Override
	public void onCreate () {

		initUi ();
	}

	@Override
	public void setActionListeners () {

		eTxtPassword.setOnEditorActionListener(eTxtPasswordActionListener);
		btnLogin.setOnClickListener(btnLoginListener);
		btnForgot.setOnClickListener(btnForgotPasswordListener);

	}

	@Override
	public int getViewLayout () {

		return R.layout.view_login;
	}

	private void initUi () {

		txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
		eTxtPassword = (EditText) view.findViewById (R.id.eTxtPassword);
		eTxtUserName = (EditText) view.findViewById(R.id.eTxtUserName);
		btnLogin = (Button) view.findViewById (R.id.btnLogin);
		btnForgot = (Button) view.findViewById (R.id.btnForgot);
		mboLogo=(ImageView)view.findViewById(R.id.imgMainLogo);
		layoutLogin=(LinearLayout)view.findViewById(R.id.layoutLogin);
		btnForgot.setOnClickListener(btnForgotPasswordListener);
		enterpriseUserError=(TextView)view.findViewById(R.id.enterpriseUserError);
		//checkIsEnterpriseUser();
		eTxtPassword.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
		eTxtPassword.setTransformationMethod(new PasswordTransformationMethod());
	}

	public void enableLogin () {

		hideErrorMessage();
		eTxtPassword.setEnabled(true);
		eTxtUserName.setEnabled(true);
		btnLogin.setEnabled(true);

		eTxtPassword.setAlpha(AppConstants.VIEW_ENABLE_ALPHA);
		eTxtUserName.setAlpha(AppConstants.VIEW_ENABLE_ALPHA);
		btnLogin.setAlpha(AppConstants.VIEW_ENABLE_ALPHA);
	}

	public void disableLogin () {

		eTxtPassword.setEnabled(false);
		eTxtUserName.setEnabled(false);
		btnLogin.setEnabled(false);

		eTxtPassword.setAlpha(AppConstants.VIEW_DISABLE_ALPHA);
		eTxtUserName.setAlpha(AppConstants.VIEW_DISABLE_ALPHA);
		btnLogin.setAlpha(AppConstants.VIEW_DISABLE_ALPHA);
	}

	// Listeners
	TextView.OnEditorActionListener eTxtPasswordActionListener = new TextView.OnEditorActionListener () {
		@Override
		public boolean onEditorAction (android.widget.TextView v, int actionId, KeyEvent event) {

			if (event != null && event.getAction () != KeyEvent.ACTION_DOWN) {
				return false;
			}
			else if ((actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event != null && event.getKeyCode () == KeyEvent.KEYCODE_ENTER)) {

				if (!DeviceUtility.isInternetConnectionAvailable (context)) {

					Utils.showInternetConnectionNotFoundMessage();

				}
				else if(areDataValid()){


					Boolean isBaseURLValid = URLUtil.isValidUrl(EnvironmentConstants.SERVICE_BASE_URL);
					Boolean isAuthURLValid = URLUtil.isValidUrl(EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL);
					if (isBaseURLValid && isAuthURLValid ) {

						((LoginActivity) controller).getAccessToken(eTxtUserName.getText().toString(), eTxtPassword.getText().toString());
					} else {
						Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
					}

//					ConfigurationManager.getInstance().loadConfigsForLoginScreen();


				}
			}

			if (!DeviceUtility.isInternetConnectionAvailable (context)) {

				Utils.showInternetConnectionNotFoundMessage ();
			}

			return false;
		}

	};

	OnClickListener                 btnLoginListener           = new OnClickListener () {
		@Override
		public void onClick (View v) {

			//enterpriseUser=false;
			//checkIsEnterpriseUser();

			if (!DeviceUtility.isInternetConnectionAvailable (context)) {

				Utils.showInternetConnectionNotFoundMessage();

			}
			else if(areDataValid()){


				Boolean isBaseURLValid = URLUtil.isValidUrl(EnvironmentConstants.SERVICE_BASE_URL);
				Boolean isAuthURLValid = URLUtil.isValidUrl(EnvironmentConstants.SERVICE_ACCESS_TOKEN_URL);
				if (isBaseURLValid && isAuthURLValid) {

					((LoginActivity) controller).getAccessToken(eTxtUserName.getText().toString(), eTxtPassword.getText().toString());
					InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(eTxtPassword.getWindowToken(), 0);

				} else {
					Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
				}

//				ConfigurationManager.getInstance().loadConfigs();

			}
		}
	};



	OnClickListener                 btnRegisterListener        = new OnClickListener () {
		@Override
		public void onClick (View v) {

		}
	};

	OnClickListener                 btnForgotPasswordListener  = new OnClickListener () {
		@Override
		public void onClick (View v) {

			//EnvironmentConstants.SERVICE_RESET_PASSWORD_URL = Utils.getForgotPassLink();

			Boolean isURLValid = URLUtil.isValidUrl(EnvironmentConstants.SERVICE_RESET_PASSWORD_URL);
			if (isURLValid) {

				Utils.openBrowserIntent(EnvironmentConstants.SERVICE_RESET_PASSWORD_URL, getContext());
			} else {
				Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
			}



		}
	};

	private boolean areDataValid(){

		if(StringUtility.isEmptyOrNull(eTxtUserName.getText().toString())){

			UIUtility.showLongToast(getContext().getString(R.string.enter_username), context);
			return false;
		}

		if(StringUtility.isEmptyOrNull(eTxtPassword.getText().toString())){

			UIUtility.showLongToast(getContext().getString(R.string.please_enter_password), context);
			return false;
		}

		return true;
	}


	public void checkIsEnterpriseUser(){

		if(enterpriseUser==true){
			enterpriseUserError.setVisibility(VISIBLE);
		}
		else{
			enterpriseUserError.setVisibility(INVISIBLE);
		}

	}

	public void showErrorMessage(){

		txtErrorMessage.setVisibility(View.VISIBLE);
	}

	public void hideErrorMessage(){

		txtErrorMessage.setVisibility(View.GONE);
	}
}
