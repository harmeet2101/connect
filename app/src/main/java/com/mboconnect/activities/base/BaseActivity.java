package com.mboconnect.activities.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout.LayoutParams;

import com.artisan.activity.ArtisanActivity;
import com.artisan.services.ArtisanBoundActivity;
import com.artisan.services.ArtisanService;
import com.mboconnect.R;
import com.mboconnect.activities.LoginActivity;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.activities.SettingsActivity;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.ServiceFactory;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.BaseView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.components.Loader;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.UIUtility;

public abstract class BaseActivity extends BaseActionBarActivity implements ArtisanBoundActivity {

    public BaseView view;
    public ServiceFactory service;
    private MenuItem menuSettings;
    private MenuItem menuLgout;
    LogoutReceiver logoutReceiver;
    public static final String LOGOUT_ACTION = "actionLogout";
    private APIResponseListner logoutResponseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        ArtisanActivity.artisanOnCreate(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        service = new ServiceFactory(this);

        initUI();
        registerLogoutReceiver();

    }

    private void registerLogoutReceiver() {

        if (logoutReceiver == null) {

            logoutReceiver = new LogoutReceiver();
        }

        registerReceiver(logoutReceiver, new IntentFilter(LOGOUT_ACTION));
    }

    @Override
    public void onBackPressed() {

        Utils.hideSoftKeyboard(this);
        super.onBackPressed();
    }

    @Override
    protected void initUI() {

        this.view = getView(this);
        this.view.controller = this;
        addContentView(this.view.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.view.onCreate();
        this.view.setActionListeners();

        logoutResponseListener = new APIResponseListner() {
            @Override
            public void onSuccess(CustomHttpResponse response) {

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sendLogoutBroadcast();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                UIUtility.showLongToast("logout fails", BaseActivity.this);
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sendLogoutBroadcast();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!(this instanceof OpportunityListActivity) && !(this instanceof SettingsActivity) && !(this instanceof OpportunityDetailsActivity)) {

            getMenuInflater().inflate(R.menu.menu_toolbar_job_list, menu);
            menuSettings = menu.findItem(R.id.menuSettings);
            menuLgout = menu.findItem(R.id.menuLgout);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuSettings:

                startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
                return true;

            case R.id.menuLgout:

                onLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLogout() {

        Utils.showAlert(getString(R.string.logout), getString(R.string.are_you_sure_logout), getString(R.string.logout_caps), getString(R.string.cancel_caps), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                service.accessTokenService.logout(logoutResponseListener);
            }

        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }, this);
    }

    private void sendLogoutBroadcast() {

        Intent i = new Intent();
        i.setAction(LOGOUT_ACTION);
        sendBroadcast(i);
    }

    @Override
    protected void setListeners() {

        // TODO Auto-generated method stub

    }

    @Override
    protected void loadData() {

        // TODO Auto-generated method stub
    }

    @Override
    protected void updateUI() {

        // TODO Auto-generated method stub
    }

    public abstract BaseView getView(BaseActionBarActivity activity);

    public void switchActivity(Class<?> activity) {

        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected void switchActivityForResult(Class<?> activity, int requestCode) {

        Intent intent = new Intent(this, activity);
        startActivityForResult(intent, requestCode);
    }

    protected void onServiceFailure(CustomHttpResponse response) {

        hideLoader();
        showLongToast(response.getErrorMessage());
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

    protected void hideLoader() {

        Loader.hideLoader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArtisanActivity.artisanOnStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ArtisanActivity.artisanOnStop(this);

    }

    protected void showToast(String string) {

        view.showToast(string);
    }

    protected void showLongToast(String string) {

        view.showLongToast(string);
    }

    public MenuItem getMenuSettings() {

        return menuSettings;
    }

    public void setMenuSettings(MenuItem menuSettings) {

        this.menuSettings = menuSettings;
    }

    public MenuItem getMenuLgout() {

        return menuLgout;
    }

    public void setMenuLgout(MenuItem menuLgout) {

        this.menuLgout = menuLgout;
    }

    class LogoutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            DataModel.setAccessToken(null);
            DataModel.clear();
            finish();
        }
    }

    @Override
    protected void onDestroy() {

        ArtisanActivity.artisanOnDestroy();
        unregisterReceiver(logoutReceiver);
        super.onDestroy();
    }

    @Override
    public ArtisanService getArtisanService() {

        return ArtisanActivity._getArtisanService();
    }

    @Override
    public void setContentView(int layoutResID) {

        View contentView = ArtisanActivity.artisanGetContentView(layoutResID, this);
        super.setContentView(contentView);
    }


}
