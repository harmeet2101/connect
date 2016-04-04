package com.mboconnect.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mboconnect.R;
import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.adapters.SuggestionCursorAdapter;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Preference;
import com.mboconnect.entities.Profile;
import com.mboconnect.entities.Search;
import com.mboconnect.entities.SearchResults;
import com.mboconnect.helpers.DBHelper;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.MessageResponse;
import com.mboconnect.services.response.OpportunityResponse;
import com.mboconnect.services.response.OpportunitySearchResponse;
import com.mboconnect.services.response.PreferenceResponse;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.OpportunityListView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tahir on 08/06/15.
 */
public class OpportunityListActivity extends BaseActivity {

	private MenuItem searchItem,
	        menuSettings, menuLogout;

	public static SearchView			searchView;
	private APIResponseListner			opportunitiesResponseListener,
	        profileResponseListener,
	        opportunityDeleteResponseListener,
	        opportunityMarkFavoriteResponseListener,
	        opportunityMarkUnFavoriteResponseListener,
	        preferenceResponseListener,
	        respondToOpportunityListener;
	public static int					searchListTotalSize;
	public static int					opportunityListPageSize;
	public static int					totalListSkipSize;
	public static int					favouriteListSkipSize;
	public static boolean				isLoading			  = false,
	        isDirectSearchInProgress = false;
	public static final int				RECORD_UPDATE_REQUEST = 100;
	private boolean						isRefreshing		  = false;
	RecyclerView.OnItemTouchListener	disabler			  = new RecyclerViewDisabler ();
	private SuggestionCursorAdapter		preferenceAdapter;
	public ArrayList<Preference>		items				  = new ArrayList<Preference> ();
	Opportunity							updatedOpportunity;
	private APIResponseListner			searchResponseListener;
	private static boolean				isNewSearch;
	private APIResponseListner			favouriteOpportunitiesResponseListener;
	private APIResponseListner			messageResponseListener;
	public static ArrayList<Preference>	preferenceItems;
	private static boolean				isFirstRoundOfCall	  = true;

	@Override
	public BaseView getView (BaseActionBarActivity activity) {

		return new OpportunityListView (activity);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		initializeListPagination();
		initResponseListener();
		showLoader();
		isFirstRoundOfCall = true;

		if (DeviceUtility.isInternetConnectionAvailable (this)) {

			initGetMessagesAPICall ();
			DataModel.setOpportunityListType (EnumConstants.OpportunitiesType.ALL);
		}
		else {

			Utils.showInternetConnectionNotFoundMessage ();
			initWithCachedData ();
		}
	}

	private void initWithCachedData () {

		DataModel.setOpportunityListType(EnumConstants.OpportunitiesType.ALL);
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (0, DataModel.getProfile ());
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (1, new SearchResults (DataModel.getOpportunityListTotalSize ()));
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll(DataModel.getOpportunitites());
		((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged();
		hideLoader();
	}

	public void onRefresh () {

		if (((OpportunityListView) view).getEtxtDummy () != null) {

			InputMethodManager mgr = (InputMethodManager) (getSystemService (Context.INPUT_METHOD_SERVICE));
			mgr.hideSoftInputFromWindow (((OpportunityListView) view).getEtxtDummy ().getWindowToken (), 0);
		}

		((OpportunityListView) view).getOpportunityListRecyclerView ().addOnItemTouchListener (disabler);

		// On pull to refresh if active is search, then load all data
		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {
			DataModel.setOpportunityListType (EnumConstants.OpportunitiesType.ALL);
		}

		DBHelper.getInstance (this).deleteAllRows (DBHelper.TABLE_PROFILE);
		isDirectSearchInProgress = false;
		isRefreshing = true;
		initializeListPagination ();
		initGetMessagesAPICall();
		((OpportunityListView) view).updateViewSearchIcon(0);
	}

	@Override
	protected void onResume () {

		super.onResume ();

		this.getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void updateList () {

		if (DataModel.getProfile () != null) {

			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();

			if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

				((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (0, new SearchResults (searchListTotalSize));
				((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (DataModel.getActiveOpportunities ());
			}
			else {

				((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (DataModel.getProfile ());
				((OpportunityListView) view).getOpportunityListAdapter ().setMessageCount (DataModel.getMessageTotalSize ());

				if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

					((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (1, new SearchResults (DataModel.getOpportunityListTotalSize ()));
					((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (DataModel.getOpportunitites ());
				}
				else {
					((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (1, new SearchResults (DataModel.getOpportunityListFavouriteSize ()));
					((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (DataModel.getFavouriteOpportunitites ());
				}
			}

			((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
		}

	}

	private void initializeListPagination () {

		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL || DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

			totalListSkipSize = 0;
		}
		else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.FAVOURITE) {

			favouriteListSkipSize = 0;
		}
		opportunityListPageSize = AppConstants.LIST_SIZE_TEN;
		searchListTotalSize = 0;
	}

	private void searchDataAPICall (String payload) {

		service.oppotunityService.searchData(payload, searchResponseListener);
		runOnUiThread (new Runnable () {
			@Override
			public void run () {

				((OpportunityListView) view).disableSwipeLayout ();
			}
		});
	}

	public void initGetOpportunitiesAPICall () {

		DBHelper.getInstance (this).deleteAllRows(DBHelper.TABLE_OPPORTUNITY);
		service.oppotunityService.getOppotunities(opportunityListPageSize,totalListSkipSize,opportunitiesResponseListener);
		//getOpportunitiesAPICall();
		isLoading = true;
	}

	public void initGetFavouriteOpportunitiesAPICall () {

		DBHelper.getInstance (this).deleteAllRows (DBHelper.TABLE_FAVORITE_OPPORTUNITY);
		service.oppotunityService.getFavouriteOppotunities (opportunityListPageSize, favouriteListSkipSize, favouriteOpportunitiesResponseListener);
		isLoading = true;
	}

	public void initGetMessagesAPICall () {

		service.profileService.getMessages (10, 0, messageResponseListener);
	}

	public void loadMoreOpportunities () {

		if (shouldMakeOpportunityAPICall ()) {

			((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.VISIBLE);
			service.oppotunityService.getOppotunities(opportunityListPageSize,totalListSkipSize,opportunitiesResponseListener);
			//getOpportunitiesAPICall ();
			isLoading = true;
			OpportunityListView.isLoadMore = false;
		}
		else {

			OpportunityListView.isLoadMore = true;
		}
	}

	private void getOpportunitiesAPICall () {

		Profile profile = DataModel.getProfile ();
		if (profile == null) {

			return;
		}

		ArrayList<Preference> preferences = profile.getPreferences ();

		if (CollectionUtility.isEmptyOrNull (preferences)) {

			preferences = new ArrayList<Preference> ();

			Preference pref = new Preference ();
			pref.setType ("title");
			pref.setValue (profile.getDesignation ());
			preferences.add (pref);

			pref = new Preference ();
			pref.setType ("location");
			pref.setValue (profile.getPreferredLocation ());
			preferences.add (pref);

			if (!CollectionUtility.isEmptyOrNull (profile.getSkills ())) {

				for (String skill : profile.getSkills ()) {

					pref = new Preference ();
					pref.setType ("skills");
					pref.setValue (skill);
					preferences.add (pref);
				}
			}
		}

		Search search = new Search ();
		service.oppotunityService.getOppotunities (search.getSearchPayload (opportunityListPageSize, totalListSkipSize, null, preferences), opportunitiesResponseListener);
	}

	public void loadMoreFavouriteOpportunities () {

		if (shouldMakeFavouriteOpportunityAPICall()) {

			((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.VISIBLE);
			service.oppotunityService.getFavouriteOppotunities(opportunityListPageSize, favouriteListSkipSize, favouriteOpportunitiesResponseListener);
			OpportunityListView.isLoadMore = false;
			isLoading = true;
		}
		else {

			OpportunityListView.isLoadMore = true;
		}
	}

	public void loadMoreSearchedOpportunities () {

		if (shouldMakeSearchOpportunityAPICall ()) {

			showLoader ();
			((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.VISIBLE);
			isNewSearch = false;
			Search searchObject = new Search ();
			searchDataAPICall (searchObject.getSearchPayload (opportunityListPageSize, ((OpportunityListView) view).getOpportunityListAdapter ().itemsList.size () - 1, searchView.getQuery ().toString (), preferenceItems));
			OpportunityListView.isLoadMore = false;
			isLoading = true;
		}
		else {

			OpportunityListView.isLoadMore = true;
		}
	}

	private boolean shouldMakeOpportunityAPICall () {

		return (((OpportunityListView) view).getOpportunityListAdapter ().itemsList.size () - 1) < DataModel.getOpportunityListTotalSize() || DataModel.getOpportunityListTotalSize() == -1;
	}

	private boolean shouldMakeFavouriteOpportunityAPICall () {

		return (((OpportunityListView) view).getOpportunityListAdapter ().itemsList.size () - 1) < DataModel.getOpportunityListFavouriteSize () || DataModel.getOpportunityListFavouriteSize () == -1;
	}

	private boolean shouldMakeSearchOpportunityAPICall () {

		return (((OpportunityListView) view).getOpportunityListAdapter ().itemsList.size ()) < searchListTotalSize;
	}

	private void initGetProfileAPICall () {

		service.profileService.getProfileData (profileResponseListener);
		isLoading = true;
	}

	private void initResponseListener () {

		opportunitiesResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleOpportunitiesOnSuccessResponse (response);
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				isLoading = false;
				((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
				((OpportunityListView) view).getOpportunityListRecyclerView ().removeOnItemTouchListener (disabler);
			}
		};

		favouriteOpportunitiesResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleFavouriteOpportunitiesOnSuccessResponse (response);
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				if (isFirstRoundOfCall) {

					initGetOpportunitiesAPICall ();
					isFirstRoundOfCall = false;
				}

				isLoading = false;
				((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
				((OpportunityListView) view).getOpportunityListRecyclerView ().removeOnItemTouchListener (disabler);
			}
		};

		profileResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleProfileOnSuccessResponse (response);
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				handleFailureResponse (response);
			}
		};

		opportunityDeleteResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handelDeleteOpportunityResponse ();
				hideLoader ();
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				handleFailureResponse (response);
			}
		};

		preferenceResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				DataModel.getPreferences ().clear ();
				PreferenceResponse preferenceResponse = (PreferenceResponse) response.getResponse ();
				DataModel.getPreferences ().addAll (preferenceResponse.getList ());
				populateAdapter ();

			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				handleFailureResponse (response);
			}
		};

		respondToOpportunityListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleRespondOpportunityResponse ();
				hideLoader ();
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				handleRespondOpportunityFailureResponse (response);
				hideLoader ();
			}
		};

		searchResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleSearchOpportunitiesOnSuccessResponse (response);
				hideLoader ();
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				isLoading = false;
				handleFailureResponse (response);
				isDirectSearchInProgress = false;
				((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
			}
		};

		messageResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				MessageResponse messageResponse = (MessageResponse) response.getResponse ();
				updateMessagesCount (messageResponse.getMeta ().getNewMessages ());
				((OpportunityListView) view).getOpportunityListAdapter ().setMessageCount (messageResponse.getMeta ().getNewMessages ());
				((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
				initGetProfileAPICall ();
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				initGetProfileAPICall ();
			}
		};

	}

	private void handleFailureResponse (CustomHttpResponse response) {

		UIUtility.showShortToast(response.getErrorMessage(), OpportunityListActivity.this);
		hideLoader();
	}

	public void loadActiveList () {

		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (DataModel.getProfile ());
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add(new SearchResults(0));
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll(DataModel.getActiveOpportunities());
		((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged();
		((OpportunityListView) view).onItemZeroBackBtn (false, true);
		((OpportunityListView) view).updateViewSearchIcon (0);
		((OpportunityListView) view).enableSwipeLayout();

	}

	private void handleSearchOpportunitiesOnSuccessResponse (CustomHttpResponse response) {

		DataModel.setOpportunityListType (EnumConstants.OpportunitiesType.SEARCH);
		OpportunitySearchResponse opportunityResponse = (OpportunitySearchResponse) response.getResponse ();
		updateSearchOpportunityListMetaData (opportunityResponse);
		DataModel.setSearchResultsCount (opportunityResponse.getMeta ().getTotal ());

		if (isNewSearch) {
			DataModel.getSearchOpportunitites ().clear ();
			DataModel.setSearchOpportunitites (opportunityResponse.getList ());
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (new SearchResults (opportunityResponse.getMeta ().getTotal ()));
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (DataModel.getSearchOpportunitites ());
			((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
			((OpportunityListView) view).getOpportunityListRecyclerView ().smoothScrollToPosition (0);
			isNewSearch = false;
			hideLoader ();
			clearSearchViewFocusWithDelay ();

		}
		else {
			DataModel.getSearchOpportunitites ().addAll (opportunityResponse.getList ());
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (opportunityResponse.getList ());
			((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
			OpportunityListView.isLoadMore = true;
			hideLoader ();

		}
		isLoading = false;
		isDirectSearchInProgress = false;
		((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
	}

	private void clearSearchViewFocusWithDelay () {

		if (isDirectSearchInProgress) {
			final Handler handler = new Handler ();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {

					getSearchView().clearFocus();

				}
			}, 50);
		}
	}

	private void handleOpportunitiesOnSuccessResponse (CustomHttpResponse response) {

		OpportunityResponse opportunityResponse = (OpportunityResponse) response.getResponse ();
		updateOpportunityListMetaData(opportunityResponse);
		incrementTotalListSkip();

		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add(0, DataModel.getProfile());
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (1, new SearchResults (opportunityResponse.getMeta ().getTotal ()));
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll(DataModel.getOpportunitites());
			((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged();
			OpportunityListView.isLoadMore = true;
			hideLoader ();
			((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
			isLoading = false;
			((OpportunityListView) view).setRefreshComplete ();
			((OpportunityListView) view).getOpportunityListRecyclerView ().removeOnItemTouchListener (disabler);
		}
	}

	private void handleFavouriteOpportunitiesOnSuccessResponse (CustomHttpResponse response) {

		OpportunityResponse opportunityResponse = (OpportunityResponse) response.getResponse ();
		updateFavouriteOpportunityListMetaData (opportunityResponse);
		incrementFavouriteListSkip ();

		if (isFirstRoundOfCall) {

			initGetOpportunitiesAPICall();
			isFirstRoundOfCall = false;
		}

		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.FAVOURITE) {

			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (0, DataModel.getProfile ());
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (1, new SearchResults (opportunityResponse.getMeta ().getTotal ()));
			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.addAll (DataModel.getFavouriteOpportunitites ());
			((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
			OpportunityListView.isLoadMore = true;
			((OpportunityListView) view).getProgressBarHeader ().setVisibility (View.GONE);
			isLoading = false;
			((OpportunityListView) view).setRefreshComplete ();
			((OpportunityListView) view).getOpportunityListRecyclerView ().removeOnItemTouchListener (disabler);
		}
	}

	private void handleProfileOnSuccessResponse (CustomHttpResponse response) {



		if (isRefreshing) {

			((OpportunityListView) view).getOpportunityListAdapter ().itemsList.clear ();
			isRefreshing = false;
		}
		final Profile profile = (Profile) response.getResponse ();

			VolleyManager.getInstance ().getImageLoader ().get (profile.getImageURL (), new ImageLoader.ImageListener () {

			@Override
			public void onErrorResponse (VolleyError error) {

				Profile temp = profile;
				temp.setImageData (new byte[] {});
				DataModel.setProfile (temp);

				((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (0, profile);

				if (isFirstRoundOfCall) {

					initGetFavouriteOpportunitiesAPICall ();
					return;
				}

				if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

					initGetOpportunitiesAPICall ();
				}
				else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.FAVOURITE) {

					initGetFavouriteOpportunitiesAPICall ();
				}
			}

			@Override
			public void onResponse (ImageLoader.ImageContainer response, boolean isImmediate) {

				if (response.getBitmap () != null) {

					try {

						Profile temp = profile;
						temp.setImageData (Utils.getBytesFromBitmap (response.getBitmap ()));
						DataModel.setProfile (temp);

						((OpportunityListView) view).getOpportunityListAdapter ().itemsList.add (0, profile);

						if (isFirstRoundOfCall) {

							initGetFavouriteOpportunitiesAPICall ();
							return;
						}
						if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

							initGetOpportunitiesAPICall ();
						}
						else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.FAVOURITE) {

							initGetFavouriteOpportunitiesAPICall ();
						}

					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
	}

	private void incrementTotalListSkip () {

		if (DataModel.getOpportunitites ().size () < DataModel.getOpportunityListTotalSize ()) {

			totalListSkipSize = DataModel.getOpportunitites ().size () - 1;

		}
	}

	private void incrementFavouriteListSkip () {

		if (DataModel.getFavouriteOpportunitites ().size () < DataModel.getOpportunityListFavouriteSize ()) {

			favouriteListSkipSize = DataModel.getFavouriteOpportunitites ().size () - 1;

		}
	}

	private void updateOpportunityListMetaData (OpportunityResponse opportunityResponse) {

		DataModel.setOpportunityListTotalSize (opportunityResponse.getMeta ().getTotal ());
	}

	private void updateFavouriteOpportunityListMetaData (OpportunityResponse opportunityResponse) {

		DataModel.setOpportunityListFavouriteSize (opportunityResponse.getMeta ().getTotal ());
	}

	private void updateSearchOpportunityListMetaData (OpportunitySearchResponse opportunityResponse) {

		searchListTotalSize = (opportunityResponse.getMeta ().getTotal ());
	}

	private void updateMessagesCount (int count) {

		DataModel.setMessagesCount (count);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {

		getMenuInflater ().inflate (R.menu.menu_toolbar_job_list, menu);
		setMenuSettings (menu.findItem (R.id.menuSettings));
		setMenuLgout (menu.findItem (R.id.menuLgout));
		setupSearch (menu);
		return super.onCreateOptionsMenu (menu);
	}

	@Override
	public void onBackPressed () {

		if (getSearchItem ().isActionViewExpanded ()) {

			getSearchItem ().collapseActionView ();
		}
		else if (((OpportunityListView) view).getBtnBackToolBar ().getVisibility () == View.VISIBLE && DataModel.getOpportunityListType () != EnumConstants.OpportunitiesType.SEARCH) {

			((OpportunityListView) view).getBtnBackToolBar ().performClick ();
		}
		else if (((OpportunityListView) view).getOpportunityListAdapter () != null && ((OpportunityListView) view).getOpportunityListAdapter ().geteTxtHeaderSearch () != null && ((OpportunityListView) view).getOpportunityListAdapter ().geteTxtHeaderSearch ().getVisibility () == View.VISIBLE) {

			((OpportunityListView) view).getOpportunityListAdapter ().hideHeaderSearch ();
		}
		else if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH || ((OpportunityListView) view).getOpportunityListLayoutManager ().findFirstVisibleItemPosition () != 0) {

			((OpportunityListView) view).onBackBtnPressed ();
		}
		else {

			// DataModel.clearOpportunities ();
			super.onBackPressed ();

		}
	}

	private void setupSearch (Menu menu) {

		menuSettings = menu.findItem (R.id.menuSettings);
		menuLogout = menu.findItem (R.id.menuLgout);

		searchItem = menu.findItem (R.id.action_search);
		SearchManager searchManager = (SearchManager) this.getSystemService (Context.SEARCH_SERVICE);
		searchView = null;

		if (searchItem != null) {

			searchView = (SearchView) MenuItemCompat.getActionView (searchItem);
		}
		if (searchView != null) {

			searchView.setSearchableInfo (searchManager.getSearchableInfo (this.getComponentName ()));
			searchView.setOnQueryTextListener (searchQueryTextListener);
			searchView.setImeOptions (EditorInfo.IME_ACTION_SEARCH);
			searchView.setOnSuggestionListener (searchViewOnSuggestionListener);
			MenuItemCompat.setOnActionExpandListener (searchItem, searchExpandableListener);
		}
	}

	SearchView.OnSuggestionListener searchViewOnSuggestionListener = new SearchView.OnSuggestionListener () {
		@Override
		public boolean onSuggestionClick (int position) {

			return true;
		}

		@Override
		public boolean onSuggestionSelect (int position) {

			return true;
		}
	};

	private void populateAdapter () {

		String[] columns = new String[] { "_id" };
		Object[] temp = new Object[] { 0 };

		preferenceItems = new ArrayList<Preference> ();
		preferenceItems.addAll (DataModel.getPreferences ());

		MatrixCursor cursor = new MatrixCursor (columns);

		for (int i = 0; i < DataModel.getPreferences ().size (); i++) {

			temp[0] = i;
			cursor.addRow (temp);
		}

		preferenceAdapter = new SuggestionCursorAdapter (this, cursor, preferenceItems);
		searchView.setSuggestionsAdapter (preferenceAdapter);
		preferenceAdapter.notifyDataSetChanged ();
	}

	public void updateSuggestionList () {

		preferenceAdapter.notifyDataSetChanged ();

	}

	// Action Listner // To detect search
	MenuItemCompat.OnActionExpandListener searchExpandableListener = new MenuItemCompat.OnActionExpandListener () {
		@Override
		public boolean onMenuItemActionExpand (MenuItem item) {

			setSettingMenuVisibility (false);
			return true;
		}

		@Override
		public boolean onMenuItemActionCollapse (MenuItem item) {

			searchView.setQuery ("", false);

			setSettingMenuVisibility (true);

			if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH || ((OpportunityListView) view).getOpportunityListLayoutManager ().findFirstVisibleItemPosition () == 1 && ((OpportunityListView) view).getBtnBackToolBar ().getVisibility () == View.INVISIBLE) {

				((OpportunityListView) view).onItemZeroBackBtn (true, false);
			}

			if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH) {

				((OpportunityListView) view).onBackBtnPressed ();
			}

			return true;

		}
	};

	private TimerTask			   task;
	private Timer				   timer;
	SearchView.OnQueryTextListener searchQueryTextListener = new SearchView.OnQueryTextListener () {

		@Override
		public boolean onQueryTextSubmit (String query) {

			if (!DeviceUtility.isInternetConnectionAvailable (OpportunityListActivity.this)) {

				Utils.showInternetConnectionNotFoundMessage ();
				return true;
			}

			if (task != null) {

				task.cancel ();

			}
			callSearch ();
			getSearchView ().clearFocus ();
			showLoader ();
			isDirectSearchInProgress = true;
			return true;

		}

		@Override
		public boolean onQueryTextChange (String newText) {

			if (!DeviceUtility.isInternetConnectionAvailable (OpportunityListActivity.this)) {

				Utils.showInternetConnectionNotFoundMessage ();
				return true;
			}

			if (newText.length () == 0) {

				initGetPreferenceAPICall ();

			}
			else if (!isDirectSearchInProgress) {

				callSearchWithDelay ();

			}
			return true;
		}
	};

	private void callSearchWithDelay () {

		if (task != null) {

			task.cancel ();

		}

		task = new TimerTask () {
			@Override
			public void run () {

				callSearch ();

			}
		};

		timer = new Timer ();
		timer.schedule (task, 3000);
	}

	private static int number = 0;

	private void callSearch () {

		if (!isDirectSearchInProgress) {

			searchListTotalSize = 0;
			isNewSearch = true;
			Search searchObject = new Search ();
			searchDataAPICall (searchObject.getSearchPayload (opportunityListPageSize, 0, searchView.getQuery ().toString (), preferenceItems));
		}
	}

	// Getter Setters

	public void openSearch () {

		MenuItemCompat.expandActionView (searchItem);
	}

	private void initGetPreferenceAPICall () {

		service.oppotunityService.getPreferenceData (preferenceResponseListener);
	}

	public void setSettingMenuVisibility (boolean visibility) {

		menuSettings.setVisible (visibility);
		menuLogout.setVisible (visibility);
	}

	// Getter Setters
	public MenuItem getSearchItem () {

		return searchItem;
	}

	public void setSearchItem (MenuItem searchItem) {

		this.searchItem = searchItem;
	}

	public SearchView getSearchView () {

		return searchView;
	}

	public void setSearchView (SearchView searchView) {

		this.searchView = searchView;
	}

	public void scrollToPosition (int number) {

		if (((OpportunityListView) view).getOpportunityListRecyclerView ().getAdapter ().getItemCount () >= 2) {

			((OpportunityListView) view).getOpportunityListLayoutManager ().scrollToPositionWithOffset (number, 0);
		}
	}

	public void deleteOpportunity (Opportunity opportunity) {

		updatedOpportunity = opportunity;
		deleteOpportunityAPICall (updatedOpportunity);
	}

	private void handelDeleteOpportunityResponse () {

		DataModel.deductListCount (updatedOpportunity);
		int index = ((OpportunityListView) view).getOpportunityListAdapter ().itemsList.indexOf (updatedOpportunity);
		((OpportunityListView) view).getOpportunityListAdapter ().itemsList.remove (index);
		((OpportunityListView) view).getOpportunityListAdapter ().notifyItemRemoved (index);
		((OpportunityListView) view).getOpportunityListAdapter ().notifyItemRangeChanged (index, ((OpportunityListView) view).getOpportunityListAdapter ().itemsList.size ());
		DataModel.getActiveOpportunities ().remove (updatedOpportunity);
		DataModel.deleteOpportunity (updatedOpportunity);
		((OpportunityListView) view).getOpportunityListAdapter ().notifyDataSetChanged ();
	}

	private void handleRespondOpportunityResponse () {

		updatedOpportunity.setIsResponded (true);
		DataModel.updateOpportunity (updatedOpportunity);
		((OpportunityListView) view).getOpportunityListAdapter ().notifyItemChanged (((OpportunityListView) view).getOpportunityListAdapter ().itemsList.indexOf (updatedOpportunity));
	}

	private void handleRespondOpportunityFailureResponse (CustomHttpResponse response) {

		((OpportunityListView) view).getOpportunityListAdapter ().notifyItemChanged (((OpportunityListView) view).getOpportunityListAdapter ().itemsList.indexOf (updatedOpportunity));
		UIUtility.showShortToast (response.getErrorMessage (), OpportunityListActivity.this);
	}

	private void deleteOpportunityAPICall (Opportunity opportunity) {

		service.oppotunityService.deleteOpportunity (opportunity.getOpportunityId (), opportunityDeleteResponseListener);
	}

	public void markFavorite (String opportunityId) {

		initMarkFavoriteAPICall (opportunityId);
		((OpportunityListView) view).markFavorite ();
	}

	private void initMarkFavoriteAPICall (String opportunityId) {

		service.oppotunityService.markOpportunityFavorite (opportunityId, opportunityMarkFavoriteResponseListener);
	}

	public void markUnFavorite (String opportunityId) {

		((OpportunityListView) view).notifyDataSetChanged ();
		initMarkUnFavoriteAPICall (opportunityId);

	}

	private void initMarkUnFavoriteAPICall (String opportunityId) {

		service.oppotunityService.markOpportunityUnFavorite (opportunityId, opportunityMarkUnFavoriteResponseListener);
	}

	public void respondToOpportunity (Opportunity opportunity) {

		showLoader ();
		respondAPICall (opportunity.getOpportunityId ());
		updatedOpportunity = opportunity;
	}

	private void respondAPICall (String opportunityId) {

		service.oppotunityService.respondToOpportunity (opportunityId, respondToOpportunityListener);
	}

	@Override
	protected void setListeners () {

		super.setListeners ();
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {

		if (requestCode == RECORD_UPDATE_REQUEST && resultCode == RESULT_OK && data.getExtras ().getParcelable ("deleted_object") != null) {

			updateList ();
		}
		super.onActivityResult (requestCode, resultCode, data);
	}

	public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

		@Override
		public boolean onInterceptTouchEvent (RecyclerView rv, MotionEvent e) {

			return true;
		}

		@Override
		public void onTouchEvent (RecyclerView rv, MotionEvent e) {

		}
	}
}
