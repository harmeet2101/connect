package com.mboconnect.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.adapters.OpportunityListAdapter;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.model.DataModel;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.DeviceUtility;

import java.util.HashMap;

/**
 * Created by tahir on 08/06/15.
 */
public class OpportunityListView extends BaseView {

	private OpportunityListAdapter   opportunityListAdapter;
	private RecyclerView             opportunityListRecyclerView;
	private SwipeRefreshLayout       swipeRefreshLayout;
	private LinearLayoutManager      opportunityListLayoutManager;
	private ImageView                imgToolbarIcon;
	private ImageButton              btnBackToolBar;
	private TextView                 txtMsgNumber;
	public static Boolean            isLoadMore;
	private ProgressBar              progressBarHeader;
	private HashMap<Integer, String> headerMessages       = new HashMap<Integer, String> ();
	int                              headerMessageCounter = 1;
	private RelativeLayout           relLayoutHeaderMessage;
	private TextView                 txtHeaderMessage;
	public ImageView                 imgFlashFavourite;

	private Toolbar                  toolBarOpportunityList;
	private ImageButton              btnSearch;
    private EditText etxtDummy;
	public static EnumConstants.OpportunitiesType opportunitiesType=EnumConstants.OpportunitiesType.ALL;

    public OpportunityListView (Context context) {

		super (context);
	}

	@Override
	public void onCreate () {

		initUI();
		prepareHeaderMessages();

	}

	@Override
	public void setActionListeners () {

	}

	@Override
	public int getViewLayout () {

		return R.layout.view_opportunity_list;
	}

	public void setRefreshComplete () {

		swipeRefreshLayout.setRefreshing (false);
	}

	public void enableSwipeLayout () {

		swipeRefreshLayout.setEnabled (true);
	}

	public void disableSwipeLayout () {

		swipeRefreshLayout.setEnabled (false);
	}

	private void initUI () {

		toolBarOpportunityList = (Toolbar) view.findViewById (R.id.toolBarOpportunityList);
		((OpportunityListActivity) controller).setSupportActionBar(toolBarOpportunityList);
		((OpportunityListActivity) controller).getSupportActionBar ().setDisplayShowTitleEnabled(false);

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById (R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.grey_light, R.color.blue, R.color.orange);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (DeviceUtility.isInternetConnectionAvailable(getContext())) {

                    ((OpportunityListActivity) controller).onRefresh();
                } else {

                    Utils.showInternetConnectionNotFoundMessage();
                    setRefreshComplete();
                }
            }
        });

        etxtDummy = (EditText)view.findViewById(R.id.etxtDumy);
		imgToolbarIcon = (ImageView) view.findViewById (R.id.imgToolbarIcon);
		btnBackToolBar = (ImageButton) view.findViewById (R.id.btnBackToolBar);
		btnSearch = (ImageButton) view.findViewById (R.id.btnSearch);
		txtMsgNumber = (TextView) view.findViewById (R.id.txtMsgNumber);
		relLayoutHeaderMessage = (RelativeLayout) view.findViewById (R.id.relLayoutHeaderMessage);
		txtHeaderMessage = (TextView) view.findViewById (R.id.txtHeaderMessage);
		relLayoutHeaderMessage.setAlpha (0);

		progressBarHeader = (ProgressBar) view.findViewById (R.id.progressBarHeader);

		opportunityListRecyclerView = (RecyclerView) view.findViewById (R.id.opportunityListRecyclerView);
		opportunityListLayoutManager = new LinearLayoutManager (context);
		opportunityListRecyclerView.setLayoutManager (opportunityListLayoutManager);
		opportunityListRecyclerView.setOnScrollListener (opportunityListScrollListener);
		btnBackToolBar.setOnClickListener (btnBackToolBarListener);
		btnSearch.setOnClickListener (btnSearchOnClickListener);
		opportunityListAdapter = new OpportunityListAdapter (context);
		opportunityListRecyclerView.setAdapter (opportunityListAdapter);
		imgToolbarIcon.setOnClickListener (btnBackToolBarListener);
		imgFlashFavourite = (ImageView) view.findViewById (R.id.imgFlashFavourite);

		isLoadMore = true;
	}

	public void notifyDataSetChanged () {

		if (opportunityListAdapter != null) {

			opportunityListAdapter.notifyDataSetChanged ();
		}
	}

	private void prepareHeaderMessages () {

		headerMessages.put (1, getContext ().getString (R.string.header_text_one));
		headerMessages.put (2, getContext ().getString (R.string.header_text_one));
		headerMessages.put (3, getContext ().getString (R.string.header_text_two));
	}

	private void showHeaderMessage () {

		relLayoutHeaderMessage.setVisibility (View.VISIBLE);
		relLayoutHeaderMessage.setAlpha (0);
		txtHeaderMessage.setText (headerMessages.get ((int) headerMessageCounter));
		headerMessageCounter++;
		int[] loc = new int[2];
		relLayoutHeaderMessage.getLocationInWindow (loc);

		final AnimatorSet as = new AnimatorSet ();
		as.playTogether (ObjectAnimator.ofFloat (relLayoutHeaderMessage, "alpha", 0.0f, 1.0f).setDuration (800), ObjectAnimator.ofFloat (txtHeaderMessage, "translationX", loc[0], -(txtHeaderMessage.getWidth () + 300)).setDuration (5000));
		as.addListener (new Animator.AnimatorListener () {
			@Override
			public void onAnimationStart (Animator animation) {

			}

			@Override
			public void onAnimationEnd (Animator animation) {

				relLayoutHeaderMessage.setVisibility (View.GONE);
			}

			@Override
			public void onAnimationCancel (Animator animation) {

			}

			@Override
			public void onAnimationRepeat (Animator animation) {

			}
		});

		if (headerMessageCounter > 3) {

			headerMessageCounter = 1;
		}

		as.start ();
	}

	OnClickListener               imgToolbarIconListener        = new OnClickListener () {
		                                                            @Override
		                                                            public void onClick (View v) {

			                                                            showHeaderMessage ();
		                                                            }
	                                                            };

	OnClickListener               btnBackToolBarListener        = new OnClickListener () {
		                                                            @Override
		                                                            public void onClick (View v) {

																		onBackBtnPressed();
		                                                            }
	                                                            };

	public void onBackBtnPressed() {
		if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.SEARCH) {
            DataModel.setOpportunityListType (opportunitiesType);
					((OpportunityListActivity) context).loadActiveList();
            opportunityListRecyclerView.smoothScrollToPosition(0);
            return;
        }

		opportunityListRecyclerView.smoothScrollToPosition(0);

		if (DataModel.getOpportunityListType () != EnumConstants.OpportunitiesType.SEARCH && OpportunityListAdapter.isStatusNumberPressed) {

            OpportunityListAdapter.isStatusNumberPressed = false;
        }
	}

	

	OnClickListener               btnSearchOnClickListener      = new OnClickListener () {
		                                                            @Override
		                                                            public void onClick (View v) {


			                                                            if (!((OpportunityListActivity) controller).getSearchItem ().isActionViewExpanded ()) {

				                                                            ((OpportunityListActivity) controller).getSearchItem ().expandActionView ();
			                                                            }
			                                                            if (!((OpportunityListActivity) controller).getSearchView ().isFocused ()) {

				                                                            ((OpportunityListActivity) controller).getSearchView ().setIconified (false);
			                                                            }
		                                                            }


	                                                            };

	RecyclerView.OnScrollListener opportunityListScrollListener = new RecyclerView.OnScrollListener () {
		                                                            @Override
		                                                            public void onScrolled (RecyclerView recyclerView, int dx, int dy) {

			                                                            super.onScrolled (recyclerView, dx, dy);

			                                                            callOnScrollWithDelay ();

		                                                            }

		                                                            @Override
		                                                            public void onScrollStateChanged (RecyclerView rv, int state) {

			                                                            int pastVisiblesItems = opportunityListLayoutManager.findFirstVisibleItemPosition ();
			                                                            if (state == RecyclerView.SCROLL_STATE_IDLE && pastVisiblesItems == 0 && DataModel.getOpportunityListType () != EnumConstants.OpportunitiesType.SEARCH) {

				                                                            getOpportunityListAdapter ().notifyDataSetChanged ();
			                                                            }
		                                                            }
	                                                            };

	private void callOnScrollWithDelay () {

		Handler handler = new Handler ();
		handler.postDelayed (new Runnable () {
			@Override
			public void run () {

				int pastVisiblesItems = opportunityListLayoutManager.findFirstVisibleItemPosition ();
				int visibleItemCount = opportunityListLayoutManager.getChildCount ();
				int totalItemCount = opportunityListLayoutManager.getItemCount ();
				refreshHeader(pastVisiblesItems);
				updateToolbar(pastVisiblesItems);
				updateViewSearchIcon(pastVisiblesItems);

				checkLoadMore(pastVisiblesItems, visibleItemCount, totalItemCount);
			}
		}, 50);
	}

	public void updateViewSearchIcon (int pastVisiblesItems) {

		if (pastVisiblesItems == 0 && btnSearch.getVisibility () == View.VISIBLE && DataModel.getOpportunityListType () != EnumConstants.OpportunitiesType.SEARCH) {
			btnSearch.setVisibility (View.INVISIBLE);
			btnBackToolBar.setVisibility(View.INVISIBLE);
		}
		else if (pastVisiblesItems >= 1 && btnSearch.getVisibility () == View.INVISIBLE || DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

			btnSearch.setVisibility (View.VISIBLE);
			btnBackToolBar.setVisibility(View.VISIBLE);
		}
	}

	private void refreshHeader (int pastVisiblesItems) {

		if (pastVisiblesItems == 0 && OpportunityListAdapter.isStatusNumberPressed) {

			getOpportunityListAdapter ().notifyDataSetChanged ();
			OpportunityListAdapter.isStatusNumberPressed = false;
		}
	}

	private void checkLoadMore (int pastVisiblesItems, int visibleItemCount, int totalItemCount) {

		if (isLoadMore && (visibleItemCount + pastVisiblesItems + 3) >= totalItemCount) {

			if (!DeviceUtility.isInternetConnectionAvailable (context)) {

				Utils.showInternetConnectionNotFoundMessage ();
				return;
			}

			if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

				opportunitiesType= EnumConstants.OpportunitiesType.ALL;
				((OpportunityListActivity) context).loadMoreOpportunities ();


			}
			else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.FAVOURITE) {

				opportunitiesType= EnumConstants.OpportunitiesType.FAVOURITE;
				((OpportunityListActivity) context).loadMoreFavouriteOpportunities ();

			}
			else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

				((OpportunityListActivity) context).loadMoreSearchedOpportunities ();
			}
		}
	}

	private void updateToolbar (int pastVisiblesItems) {

		if (pastVisiblesItems >= 1 || DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

			txtMsgNumber.setText ("3 of " + (opportunityListRecyclerView.getAdapter ().getItemCount () - 1));

		}

		if (pastVisiblesItems == 1 && !((OpportunityListActivity) controller).getSearchItem ().isVisible ()) {

			// onItemZeroToolBar(true, false);
			onItemZeroBackBtn (true, false);

		}
		else if (pastVisiblesItems == 0 && ((OpportunityListActivity) context).getSearchView () != null && (((OpportunityListActivity) context).getSearchView ().getQuery () == null || ((OpportunityListActivity) context).getSearchView ().getQuery ().length () == 0) && (((OpportunityListActivity) controller).getSearchItem () != null && !((OpportunityListActivity) controller).getSearchItem ().isActionViewExpanded ())) {

			// onItemZeroToolBar (false, true);
			onItemZeroBackBtn (false, true);
		}
	}

	public void onItemZeroBackBtn (boolean visible, boolean visible2) {

		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

			imgToolbarIcon.setVisibility (View.VISIBLE);
			//btnBackToolBar.setVisibility (View.INVISIBLE);
			txtMsgNumber.setVisibility (View.INVISIBLE);// TODO: mark visible to
			                                            // show counter
			return;
		}

		if (visible) {
		//	btnBackToolBar.setVisibility (View.INVISIBLE);
			txtMsgNumber.setVisibility (View.INVISIBLE);// TODO: mark visible to
			                                            // show counter
		}
		else {
			//btnBackToolBar.setVisibility (View.INVISIBLE);
			txtMsgNumber.setVisibility (View.INVISIBLE);
		}

		if (visible2)
			imgToolbarIcon.setVisibility (View.VISIBLE);
		else
			imgToolbarIcon.setVisibility (View.VISIBLE);

	}

	public LinearLayoutManager getOpportunityListLayoutManager () {

		return opportunityListLayoutManager;
	}

	public void setOpportunityListLayoutManager (LinearLayoutManager opportunityListLayoutManager) {

		this.opportunityListLayoutManager = opportunityListLayoutManager;
	}

	public ImageButton getBtnBackToolBar () {

		return btnBackToolBar;
	}

	public void setBtnBackToolBar (ImageButton btnBackToolBar) {

		this.btnBackToolBar = btnBackToolBar;
	}

	public OpportunityListAdapter getOpportunityListAdapter () {

		return opportunityListAdapter;
	}

	public void setOpportunityListAdapter (OpportunityListAdapter opportunityListAdapter) {

		this.opportunityListAdapter = opportunityListAdapter;
	}

	public ProgressBar getProgressBarHeader () {

		return progressBarHeader;
	}

	public void setProgressBarHeader (ProgressBar progressBarHeader) {

		this.progressBarHeader = progressBarHeader;
	}

	public RecyclerView getOpportunityListRecyclerView () {

		return opportunityListRecyclerView;
	}

	public void setOpportunityListRecyclerView (RecyclerView opportunityListRecyclerView) {

		this.opportunityListRecyclerView = opportunityListRecyclerView;
	}

	public Toolbar getToolBarOpportunityList () {

		return toolBarOpportunityList;
	}

	public void setToolBarOpportunityList (Toolbar toolBarOpportunityList) {

		this.toolBarOpportunityList = toolBarOpportunityList;
	}

	public void markFavorite () {

		imgFlashFavourite.setVisibility (View.VISIBLE);

		ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat (imgFlashFavourite, "alpha", 1.0f, 0.0f);
		alphaAnimation.setDuration (1000);
		alphaAnimation.addListener (new Animator.AnimatorListener () {
			@Override
			public void onAnimationStart (Animator animation) {

			}

			@Override
			public void onAnimationEnd (Animator animation) {

				imgFlashFavourite.setVisibility (View.GONE);
			}

			@Override
			public void onAnimationCancel (Animator animation) {

			}

			@Override
			public void onAnimationRepeat (Animator animation) {

			}
		});
		alphaAnimation.start ();
	}

    public EditText getEtxtDummy() {
        return etxtDummy;
    }
}
