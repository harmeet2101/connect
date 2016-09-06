package com.mboconnect.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.artisan.activity.ArtisanActivity;
import com.artisan.manager.ArtisanManager;
import com.artisan.manager.ArtisanManagerCallback;
import com.artisan.powerhooks.PowerHookManager;
import com.artisan.powerhooks.PowerHookVariable;
import com.mboconnect.R;
import com.mboconnect.constants.EnvironmentConstants;
import com.mboconnect.entities.AccessToken;
import com.mboconnect.entities.Meta;
import com.mboconnect.entities.Profile;
import com.mboconnect.helpers.DBHelper;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.managers.ConfigurationManager;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.ServiceFactory;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.LoginView;
import com.tenpearls.android.components.Loader;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.PreferenceUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;


/**
 * Created by tahir on 08/06/15.
 */
public class LoginActivity extends ArtisanActivity {


    final Context context = this;


    public static final String ENABLE_LOGIN_ACTION = "enableLoginAction";
    EnableLoginReceiver enabelLoginReceiver;
    APIResponseListner accessTokenListener;
    public static boolean isLoginMode = false;
    int position = 0;
    Object checkedItem;
    private int count = 0;
    public static boolean isLoading = false;
    private PowerHookVariable startScreenTextHook;
    private long startMillis = 0;
    String[] environments = new String[]{
            "Prod", "Staging", "Demo"
    };

    public BaseView view;
    public ServiceFactory service;
    private MenuItem menuSettings;
    private MenuItem menuLgout;
    LogoutReceiver logoutReceiver;
    public static final String LOGOUT_ACTION = "actionLogout";
    private APIResponseListner logoutResponseListener;
    private APIResponseListner
            profileResponseListener;
    private Boolean isRoleEnterprise = false;

    private void registerLogoutReceiver() {

        if (logoutReceiver == null) {

            logoutReceiver = new LogoutReceiver();
        }

        registerReceiver(logoutReceiver, new IntentFilter(LOGOUT_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        ArtisanActivity.artisanOnCreate(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initUI();
        registerLogoutReceiver();

        initListeners();
        registerReceivers();
        initMeta();
        setupUI();
        service = new ServiceFactory(this);
        ArtisanManager.onFirstPlaylistDownloaded(this, new ArtisanManagerCallback() {
            @Override
            public void execute() {
                // First playlist has been downloaded, now you can get the latest power hook experiment details
                PowerHookManager.getPowerHookBlockExperimentDetails();
                PowerHookManager.getPowerHookVariableExperimentDetails();
            }
        });
        if (!StringUtility.isEmptyOrNull(DataModel.getAccessToken())) {

            ConfigurationManager.getInstance().loadConfigs();
            startJobListActivity();
        }


        LoginView.mboLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                if (eventaction == MotionEvent.ACTION_DOWN) {

                    //get system current milliseconds
                    long time = System.currentTimeMillis();


                    //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                    if (startMillis == 0 || (time - startMillis > 2000)) {
                        startMillis = time;
                        count = 1;
                    }
                    //it is not the first, and it has been  less than 3 seconds since the first
                    else { //  time-startMillis< 3000
                        count++;
                    }

                    if (count == 5) {
                        //do whatever you need
                        position = PreferenceUtility.getInteger(context, "environmentPosition", position);
                        chooseEnvironment();
                        // Intent i=new Intent(LoginActivity.this,SyncPowerHooks.class);
                        //startActivity(i);
                    }
                    return true;
                }


                return false;
            }
        });

    }


    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            DataModel.setAccessToken(null);
            DataModel.clear();
            finish();
        }
    }


    protected void initUI() {

        this.view = getView(this);
        this.view.controller = this;
        setContentView(view.getView());
//        addContentView(this.view.getView(), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        this.view.onCreate();
        this.view.setActionListeners();

        logoutResponseListener = new APIResponseListner() {
            @Override
            public void onSuccess(CustomHttpResponse response) {

                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sendLogoutBroadcast();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                UIUtility.showLongToast("logout fails", LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sendLogoutBroadcast();
            }
        };
    }

    private void sendLogoutBroadcast() {

        Intent i = new Intent();
        i.setAction(LOGOUT_ACTION);
        sendBroadcast(i);
    }

    private void chooseEnvironment() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Select Environment");

        // set dialog message
        alertDialogBuilder

                .setSingleChoiceItems(environments, position, null)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ListView lw = ((AlertDialog) dialog).getListView();
                        checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());

                        dialog.dismiss();

                        if (checkedItem.equals("Prod")) {

                            Toast.makeText(context, "You selected Prod Environment", Toast.LENGTH_SHORT).show();
                            position = 0;
                        } else if (checkedItem.equals("Staging")) {
                            Toast.makeText(context, "You selected Staging Environment", Toast.LENGTH_SHORT).show();
                            position = 1;
                        } else {
                            Toast.makeText(context, "You selected Demo Environment", Toast.LENGTH_SHORT).show();
                            position = 2;
                        }

                        PreferenceUtility.setInteger(context, "environmentPosition", position);
                        ConfigurationManager.getInstance().loadConfigs();
                        service = new ServiceFactory(context);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();


    }

    public void initGetProfileAPICall() {

        service.profileService.getProfileData(profileResponseListener);
        isLoading = true;
    }


    private void checkDefaultEnvironemt() {

        if (EnvironmentConstants.SERVICE_BASE_URL.equals(EnvironmentConstants.URL_WEB_PROD)) {
            position = 0;
        } else if (EnvironmentConstants.SERVICE_BASE_URL.equals(EnvironmentConstants.URL_WEB_STAGGING)) {
            position = 1;
        } else {
            position = 2;
        }
    }

    private void registerReceivers() {

        if (enabelLoginReceiver == null) {

            enabelLoginReceiver = new EnableLoginReceiver();
        }

        registerReceiver(enabelLoginReceiver, new IntentFilter(ENABLE_LOGIN_ACTION));
    }

    private void unRegisterReceivers() {

        unregisterReceiver(enabelLoginReceiver);
        unregisterReceiver(logoutReceiver);
    }

    private void setupUI() {

        ((LoginView) view).enableLogin();

// Removed the lockout

//        if (DataModel.getLoginCounter() < 3) {
//
//            ((LoginView) view).enableLogin();
//        } else {
//
//            Utils.setLoginAlarm(LoginActivity.this);
//            ((LoginView) view).disableLogin();
//        }
    }

    @Override
    protected void onDestroy() {

        unRegisterReceivers();
        ArtisanActivity.artisanOnDestroy();
        super.onDestroy();
    }

    private void initMeta() {

        Meta meta = new Meta();
        meta.setFavoriteOpportunitiesCount(-1);
        meta.setAllOpportunitiesCount(-1);
        meta.setMessagesCount(-1);
        DBHelper.getInstance(this).addMeta(meta);
    }

    private void initListeners() {

        accessTokenListener = new APIResponseListner() {
            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleAccessTokenSuccessResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                DataModel.setLoginCounter(DataModel.getLoginCounter() + 1);
                setupUI();
                hideLoader();
                ((LoginView) view).showErrorMessage();
            }
        };

        profileResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleProfileOnSuccessResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                handleFailureResponse(response);
            }
        };
    }

    protected void hideLoader() {

        Loader.hideLoader();
    }

    private void handleProfileOnSuccessResponse(CustomHttpResponse response) {


        final Profile profile = (Profile) response.getResponse();
        if (profile.userRole.equals("enterprise")) {
            isRoleEnterprise = true;
        }

        if (isRoleEnterprise) {
            // Toast.makeText(context, "No Enterprise User allowed", Toast.LENGTH_SHORT).show();
            DataModel.setLoginCounter(DataModel.getLoginCounter() + 1);
            setupUI();
            ((LoginView) view).showErrorMessage();
            DataModel.setAccessToken(null);
            isRoleEnterprise = false;
        } else {
            startJobListActivity();
        }
    }

    private void handleFailureResponse(CustomHttpResponse response) {

        UIUtility.showShortToast(response.getErrorMessage(), LoginActivity.this);
        hideLoader();
    }

    private void handleAccessTokenSuccessResponse(CustomHttpResponse response) {

        AccessToken token = (AccessToken) response.getResponse();
        //Log.d("Access Token","This is my Refresh Token: "+ token);
        DataModel.setAccessToken(token);
        //String refreshToken=DataModel.getRefreshToken();
        hideLoader();
        initGetProfileAPICall();
        //startJobListActivity();

    }

    public BaseView getView(LoginActivity activity) {

        return new LoginView(activity);
    }

    protected void showLoader(String message) {

        try {
            Loader.showLoader(this, message, null);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void showLoader() {

        showLoader(getString(R.string.loading));
    }


    public void getAccessToken(String username, String password) {

        isLoginMode = true;
        ((LoginView) view).hideErrorMessage();
        showLoader();
        service.accessTokenService.getAccessToken(accessTokenListener, username, password);

    }

    private void startJobListActivity() {

        isLoginMode = false;
        DataModel.setLoginCounter(0);
        Intent intent = new Intent(this, OpportunityListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {

        super.onResume();
        ConfigurationManager.getInstance().loadConfigs();
    }

    class EnableLoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ((LoginView) view).enableLogin();
        }
    }
}
