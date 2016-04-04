package com.mboconnect.views;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mboconnect.R;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.adapters.OpportunityDetailsTabAdapter;
import com.mboconnect.components.TabbedViewPager;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.helpers.SwipeDetector;
import com.mboconnect.model.DataModel;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.Button;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

/**
 * Created by ali.mehmood on 6/12/2015.
 */
public class OpportunityDetailsView extends BaseView implements ViewPager.OnPageChangeListener {

    private Context context;
    private TextView txtMsgNumber,
            txtStatus, txtTimestamp;
    OpportunityPageChangeListener opportunityPageChangeListener;
    public ImageView imgFlashFavourite;
    Opportunity opportunity;
    private SlidingDrawer slidingDrawer;
    private Button btnRespond;
    private ImageView imgFavourite;
    com.mboconnect.components.ImageButton imgBtnFavourite;
    private GoogleMap googleMap;
    private RelativeLayout layoutPanel;
    private int currentPage = -1;
    private TextView txtOpportunityTitle;
    private TextView txtCompanyName;
    private TextView txtJobDate;
    private TextView txtJobLocation;
    private TextView txtJobBudget;
    private com.mboconnect.components.ImageButton imgBtnLocation;
    private com.mboconnect.components.ImageButton imgBtnExpandPanel;
    private TextView txtNext;
    private TextView txtMapCompanyName;
    private TextView txtDuration;
    private RelativeLayout relLayoutPage;
    private View mapView;
    RelativeLayout opportunityRlayout;
    private int totalListCount;

    public OpportunityDetailsView(Context context) {

        super(context);
        this.context = context;
    }

    @Override
    public int getViewLayout() {

        return R.layout.view_opportunity_details;
    }

    @Override
    public void onCreate() {

        initUI();
    }

    @Override
    public void setActionListeners() {

    }

    public void setOpportunityPageChangeListener(OpportunityPageChangeListener opportunityPageChangeListener) {

        this.opportunityPageChangeListener = opportunityPageChangeListener;
    }

    private void initUI() {

        setupToolBar();

        txtMsgNumber = (TextView) view.findViewById(R.id.txtNumber);
        relLayoutPage = (RelativeLayout) view.findViewById(R.id.relLayoutPage);
        txtTimestamp = (TextView) view.findViewById(R.id.txtTimestamp);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        imgFlashFavourite = (ImageView) view.findViewById(R.id.imgFlashFavourite);

        txtOpportunityTitle = (TextView) view.findViewById(R.id.txtOpportunityTitle);
        txtCompanyName = (TextView) view.findViewById(R.id.txtCompanyName);
        txtJobDate = (TextView) view.findViewById(R.id.txtJobDate);
        txtJobLocation = (TextView) view.findViewById(R.id.txtJobLocation);
        txtJobBudget = (TextView) view.findViewById(R.id.txtJobBudget);
        imgFavourite = (ImageView) view.findViewById(R.id.imgFavourite);
        imgBtnFavourite = (com.mboconnect.components.ImageButton) view.findViewById(R.id.imgBtnFavourite);
        imgBtnLocation = (com.mboconnect.components.ImageButton) view.findViewById(R.id.imgBtnLocation);
        imgBtnExpandPanel = (com.mboconnect.components.ImageButton) view.findViewById(R.id.imgBtnExpandPanel);
        txtNext = (TextView) view.findViewById(R.id.txtNext);
        txtMapCompanyName = (TextView) view.findViewById(R.id.txtMapCompanyName);
        txtDuration = (TextView) view.findViewById(R.id.txtDuration);
        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.slidingDrawer);
        layoutPanel = (RelativeLayout) view.findViewById(R.id.layoutPanel);
        layoutPanel.setAlpha(0);
        opportunityRlayout = (RelativeLayout) view.findViewById(R.id.opportunityRelativeLayout);

        initMap();

        new SwipeDetector(relLayoutPage).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {

                if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT) {

                    selectPreviousPage();
                } else if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {

                    selectNextPage();
                }
            }
        });


        if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.FAVOURITE) {

            totalListCount = DataModel.getOpportunityListFavouriteSize();

        } else if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.ALL) {

            totalListCount = DataModel.getOpportunityListTotalSize();

        } else {

            totalListCount = DataModel.getSearchResultsCount();
        }
    }

    private void initMap() {

        ImageButton imgBtnDirection = (ImageButton) view.findViewById(R.id.imgBtnDirection);
        googleMap = ((MapFragment) ((Activity) getContext()).getFragmentManager().findFragmentById(R.id.mapView)).getMap();
        mapView = (((Activity) getContext()).getFragmentManager().findFragmentById(R.id.mapView)).getView();


        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {

        }
        if (googleMap == null)
            return;

        googleMap.setMyLocationEnabled(true);
        final View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

        try {
            googleMap.clear();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
        } catch (Exception e) {

        }

        imgBtnDirection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                locationButton.performClick();
            }
        });
    }

    public void setSelectedIndex(int index) {

        updateHeaderCount(index);
    }

    public void populateData(int index) {

try {

    this.opportunity = DataModel.getActiveOpportunities().get(index);
    currentPage = index;

    updateHeaderCount(index);

    txtTimestamp.setText(opportunity.getTimestamp());
    setStatus(opportunity.getStatusToDisplay());

    btnRespond.setSelected(opportunity.isResponded());
    if (opportunity.isResponded()) {

        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) opportunityRlayout.getLayoutParams();
        lp.setMargins(0, 0, DeviceUtility.getPixelsFromDps(10, context), 0);
        opportunityRlayout.setLayoutParams(lp);

        btnRespond.setText(context.getString(R.string.responded));
    } else {

        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) opportunityRlayout.getLayoutParams();
        lp.setMargins(0, 0, DeviceUtility.getPixelsFromDps(1, context), 0);
        opportunityRlayout.setLayoutParams(lp);

        btnRespond.setText(context.getString(R.string.respond));
    }

    TabbedViewPager pgrJobTabs = (TabbedViewPager) view.findViewById(R.id.pgrJobTabs);
    com.mboconnect.components.ImageButton imgBtnTrash = (com.mboconnect.components.ImageButton) view.findViewById(R.id.imgBtnTrash);
    com.mboconnect.components.ImageButton imgBtnMessage = (com.mboconnect.components.ImageButton) view.findViewById(R.id.imgBtnMessage);

    pgrJobTabs.setTabsTitles(prepareTabsListItems());
    pgrJobTabs.setAdapter(new OpportunityDetailsTabAdapter(context, opportunity));
    txtDuration.setText(opportunity.getDuration());
    imgFavourite.setVisibility(opportunity.isFavorite() ? View.VISIBLE : View.GONE);


    if (txtOpportunityTitle != null) {

        Layout layout = txtOpportunityTitle.getLayout();
        if (layout != null) {

            int ellipsisCount = layout.getEllipsisCount(1);

            if (ellipsisCount > 0) {

                txtOpportunityTitle.setTextSize(17);
            }
        }
    }

    txtOpportunityTitle.setText(opportunity.getTitle());
    txtJobDate.setText(opportunity.getFormattedDateString());
    txtJobLocation.setText(opportunity.getAddress().getShortFormattedAddress());
    txtJobBudget.setText(opportunity.getBudget());
    if (txtJobBudget != null) {

        Layout layout = txtJobBudget.getLayout();
        if (layout != null) {

            int ellipsisCount = layout.getEllipsisCount(1);

            if (ellipsisCount > 0) {

                txtJobBudget.setTextSize(34);
            }
        }

    }
    String address = "--";
    if (DeviceUtility.isInternetConnectionAvailable(context)) {

        address = Utils.getLocationAddress(opportunity.getAddress().getCoordinates(), context);
        String[] addressPieces = address.split("\\,");
        address = addressPieces[0];

        if(address.contains("null")){

            address = Utils.removeWords(address, "null");
        }

        if(address.length() > 0 && address.charAt(address.length() -1 ) == ','){

            address = address.substring(0, address.length() - 1);
        }
    }

    txtMapCompanyName.setText(address);

    if (txtMapCompanyName != null) {

        Layout layout = txtMapCompanyName.getLayout();
        if (layout != null) {

            int ellipsisCount = layout.getEllipsisCount(1);

            if (ellipsisCount > 0) {

                txtMapCompanyName.setTextSize(10);
            }
        }

    }
    imgBtnFavourite.setImageResource(opportunity.isFavorite() ? R.drawable.selected_favorite_icon : R.drawable.favorite_grey_icon);

    //imgBtnMessage.setEnabled(opportunity.isResponded());
    imgBtnMessage.setAlpha(opportunity.isResponded() ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);

//		imgBtnTrash.setEnabled(!(opportunity.isResponded() || opportunity.isAccepted()));
    imgBtnTrash.setAlpha(!(opportunity.isResponded() || opportunity.isAccepted()) ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
    txtCompanyName.setText(opportunity.isCompanyInfoVisible() ? opportunity.getCompanyName() : context.getString(R.string.confidential));

    imgBtnMessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!opportunity.isResponded()) {

                UIUtility.showLongToast("You can't chat until you respond to the opportunity", context);
                return;
            }

            ((OpportunityDetailsActivity) context).startConversationMessageActivity();
        }
    });

    imgBtnFavourite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            toggleFavorite(opportunity);
        }
    });

    imgBtnLocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            expandMap();
        }
    });

    txtJobLocation.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

            expandMap();
        }
    });
    txtNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            selectNextPage();
        }
    });

    imgBtnTrash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (opportunity.isResponded() || opportunity.isAccepted()) {


                UIUtility.showLongToast("You can't delete the opportunity you have responded to", context);
                return;
            }

            UIUtility.showAlert(null, context.getString(R.string.you_want_to_delete_opportunity), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ((OpportunityDetailsActivity) context).deleteOpportunity(opportunity);

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }

            }, context);

        }
    });

    if (googleMap != null) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(opportunity.getAddress().getCoordinates()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(opportunity.getAddress().getCoordinates()));
            }
        }, 100);
    }

    View dummy = view.findViewById(R.id.dummy);
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
    params.height = (dummy.getHeight() - DeviceUtility.getPixelsFromDps(90, context));
    mapView.setLayoutParams(params);
}
catch (IndexOutOfBoundsException e){

}

    }

    public void expandMap() {

        imgBtnExpandPanel.performClick();

    }

    private void toggleFavorite(Opportunity opportunity) {

        if (!opportunity.isFavorite()) {

            ((OpportunityDetailsActivity) context).markFavorite(opportunity);
        } else {

            ((OpportunityDetailsActivity) context).markUnFavorite(opportunity);
        }
    }

    private ArrayList<String> prepareTabsListItems() {

        ArrayList<String> titles = new ArrayList<String>();
        titles.add(context.getString(R.string.description_caps));
        titles.add(context.getString(R.string.skills_caps));
        titles.add(context.getString(R.string.company_info_caps));
        titles.add(context.getString(R.string.connect_caps));

        return titles;
    }

    private void setStatus(EnumConstants.OpportunitiesStatus value) {

        switch (value) {
            case ACCEPTED:
                txtStatus.setText(context.getString(R.string.accepted));
                txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_green));
                break;

            case RESPONDED:
                txtStatus.setText(context.getString(R.string.responded_caps));
                txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_blue));
                break;

            case NEW:
                txtStatus.setText(context.getString(R.string.new_caps));
                txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_red));
                break;

            default:
                txtStatus.setText("");
                txtStatus.setBackgroundColor(context.getResources().getColor(R.color.md__transparent));
                break;
        }
        txtStatus.setVisibility(View.VISIBLE);
    }

    private void updateHeaderCount(int position) {

        txtMsgNumber.setText(String.format("%d of %d", position + 1, totalListCount));
    }

    private void setupToolBar() {

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolBarJobDetail);
        ((OpportunityDetailsActivity) controller).setSupportActionBar(mToolbar);
        ((OpportunityDetailsActivity) controller).getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnRespond = (Button) view.findViewById(R.id.btnRespond);
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBackToolBar);
        ImageView imgToolbarIcon = (ImageView) view.findViewById(R.id.imgToolbarIcon);
        imgToolbarIcon.setOnClickListener(btnBackListener);
        btnBack.setOnClickListener(btnBackListener);
        btnRespond.setOnClickListener(btnRespondListener);
    }

    OnClickListener btnBackListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            ((OpportunityDetailsActivity) controller).onBackPressed();
        }
    };

    OnClickListener btnRespondListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            onRespondPressed();
        }
    };

    private void onRespondPressed() {

        if (opportunity.isResponded()) {

            return;
        }

        Utils.showAlert(getContext().getString(R.string.are_you_sure), getContext().getString(R.string.by_responding_opportunity), getContext().getString(R.string.continue_caps), getContext().getString(R.string.cancel_caps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((OpportunityDetailsActivity) getContext()).respondToOpportunity(opportunity);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }, getContext());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        updateHeaderCount(position);
        if (opportunityPageChangeListener != null) {

            opportunityPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OpportunityPageChangeListener {

        public void onPageSelected(int index);
    }

    public void markFavorite() {

        imgFlashFavourite.setVisibility(View.VISIBLE);

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(imgFlashFavourite, "alpha", 1.0f, 0.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                imgFlashFavourite.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alphaAnimation.start();
    }

    private void changePageAnimation(boolean toLeft) {

        if (toLeft) {

            ObjectAnimator animation = ObjectAnimator.ofFloat(relLayoutPage, "translationX", view.getWidth(), 0);
            animation.setDuration(100);
            animation.start();
        } else {

            ObjectAnimator animation = ObjectAnimator.ofFloat(relLayoutPage, "translationX", -view.getWidth(), 0);
            animation.setDuration(100);
            animation.start();
        }
    }

    public void selectNextPage() {

        int index = currentPage;
        index++;
        if (index < DataModel.getActiveOpportunities().size()) {

            currentPage = index;

            if (slidingDrawer.isOpened()) {

                expandMap();
            }
            txtOpportunityTitle.setTextSize(22);
            populateData(currentPage);
            changePageAnimation(true);
            ((OpportunityDetailsActivity) context).onPageSelected(currentPage);

        } else {

            ((OpportunityDetailsActivity) context).loadMoreOpportunities();
        }
    }

    private void selectPreviousPage() {

        int index = currentPage;
        index--;
        if (index >= 0) {

            currentPage = index;
            if (slidingDrawer.isOpened()) {

                expandMap();
            }
            txtOpportunityTitle.setTextSize(22);
            populateData(currentPage);
            changePageAnimation(false);
            ((OpportunityDetailsActivity) context).onPageSelected(currentPage);
        }
    }
}
