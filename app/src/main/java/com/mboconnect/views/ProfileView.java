package com.mboconnect.views;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.mboconnect.R;
import com.mboconnect.activities.ProfileActivity;
import com.mboconnect.adapters.ProfileAdapter;
import com.mboconnect.components.TabbedViewPager;
import com.mboconnect.entities.Profile;
import com.mboconnect.model.DataModel;
import com.tenpearls.android.components.Button;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.StringUtility;

import java.util.ArrayList;

/**
 * Created by vishal.chhatwani on 8/5/2015.
 */
public class ProfileView extends BaseView {

    TextView txtName;
    NetworkImageView imgProfile;
    TextView txtDesignation;
    TextView txtEmail;
    TextView txtPhone;
    TextView txtMobile;
    Context context;
    Profile profile;
    FrameLayout phoneBar;

    public ProfileView(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public int getViewLayout() {

        return R.layout.view_profile;
    }

    @Override
    public void onCreate() {

        initUI();

    }

    private void initUI() {

        setupToolBar();
        imgProfile = (NetworkImageView) view.findViewById(R.id.imgProfile);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtDesignation = (TextView) view.findViewById(R.id.txtDesignation);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        phoneBar = (FrameLayout) view.findViewById(R.id.phoneBar);
        profile = DataModel.getProfile();
        txtName.setText(profile.getDisplayName());
        txtDesignation.setText(profile.getDesignation());

        txtEmail.setText(Html.fromHtml("<b>e: </b>" + profile.getEmail()));

        //String phone="771.231.654";
        //String mobile="142.765.223";
        //String mobile=null;

        txtPhone.setVisibility(StringUtility.isEmptyOrNull(profile.getPhone()) ? View.GONE : View.VISIBLE);
        txtMobile.setVisibility(StringUtility.isEmptyOrNull(profile.getMobile()) ? View.GONE : View.VISIBLE);
        phoneBar.setVisibility(StringUtility.isEmptyOrNull(profile.getPhone()) ? View.GONE : View.VISIBLE);

        if (!StringUtility.isEmptyOrNull(profile.getPhone())) {

            txtPhone.setText(Html.fromHtml("<b>m: </b>" + profile.getPhone()));
        }

        if (!StringUtility.isEmptyOrNull(profile.getMobile())) {

            txtMobile.setText(Html.fromHtml("<b>h: </b>" + profile.getMobile()));
        }
        imgProfile.setImageUrl(profile.getImageURL(), VolleyManager.getInstance().getImageLoader());
    }

    public void populateData() {

        TabbedViewPager profileTab = (TabbedViewPager) view.findViewById(R.id.PagerTab);
        profileTab.setShouldUpdateLastTabColor(false);
        ArrayList<String> tabsTitles = new ArrayList<String>();
        tabsTitles.add(getContext().getString(R.string.summary_caps));
        tabsTitles.add(getContext().getString(R.string.education_caps));
        tabsTitles.add(getContext().getString(R.string.skills_caps));
        tabsTitles.add(getContext().getString(R.string.resume_caps));
        profileTab.setTabsTitles(tabsTitles);

        profileTab.setAdapter(new ProfileAdapter(context, DataModel.getProfile(), tabsTitles));
    }

    @Override
    public void setActionListeners() {

    }

    private void setupToolBar() {

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbarProfile);
        ((ProfileActivity) controller).setSupportActionBar(mToolbar);
        ((ProfileActivity) controller).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView imgToolbarIcon = (ImageView) view.findViewById(R.id.imgToolbarIcon);
        Button btnRespond = (Button) view.findViewById(R.id.btnRespond);
        TextView txtMsgNumber = (TextView) view.findViewById(R.id.txtMsgNumber);
        ImageButton btnBackToolBar = (ImageButton) view.findViewById(R.id.btnBackToolBar);
        btnRespond.setVisibility(View.GONE);
        txtMsgNumber.setVisibility(View.GONE);
        imgToolbarIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ProfileActivity) controller).onBackPressed();
            }
        });

        btnBackToolBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ProfileActivity) controller).onBackPressed();
            }
        });
    }
}
