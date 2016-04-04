package com.mboconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.mboconnect.R;
import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.GoogleDirection;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Preference;
import com.mboconnect.entities.Profile;
import com.mboconnect.entities.Search;
import com.mboconnect.helpers.DBHelper;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.managers.LocationManager;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.OpportunityResponse;
import com.mboconnect.services.response.OpportunitySearchResponse;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.OpportunityDetailsView;
import com.mboconnect.views.OpportunityListView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OpportunityDetailsActivity extends BaseActivity implements OpportunityDetailsView.OpportunityPageChangeListener {

    private int selectedIndex;
    private APIResponseListner opportunityDetailsResponseListener,
            opportunityDeleteResponseListener,
            opportunityMarkFavoriteResponseListener,
            opportunityMarkUnFavoriteResponseListener,
            respondToOpportunityListener,
            directionResponseListener,
            opportunitiesResponseListener,
            favouriteOpportunitiesResponseListener,
            searchResponseListener;
    Bundle savedInstanceState;
    Opportunity updatedOpportunity;
    public static Opportunity unmarkOpportunityFavorite;
    public static HashMap<String,Opportunity> unMarkFavoriteOpportunity=new HashMap<String,Opportunity>();
    private static boolean shouldUpdateList = false;
    String duration;
    boolean isOpportunityFavorite=true;
    private static HashMap<String, Opportunity> updatedOppMap = new HashMap<String, Opportunity>();

    @Override
    public BaseView getView(BaseActionBarActivity activity) {

        return new OpportunityDetailsView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        updatedOppMap = new HashMap<String, Opportunity>();

        getParcelableData();
        isOpportunityFavorite=true;
        ifErrorCloseActivity();

        initResponseListener();
        if (shouldMakeDetailsAPICall()) {

            populateData();
            showLoader();
            getDirectionsAPICall();
        } else {

            Utils.showInternetConnectionNotFoundMessage();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    populateData();
                }
            }, 10);
        }
    }

    private void ifErrorCloseActivity() {

        if (selectedIndex >= DataModel.getActiveOpportunities().size()) {

            onBackPressed();
        }
    }

    private void initResponseListener() {

        opportunityDetailsResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleOpportunityDetailsResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
                UIUtility.showShortToast(response.getErrorMessage(), OpportunityDetailsActivity.this);
            }
        };

        opportunityDeleteResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleDeleteOpportunityResponse();
                hideLoader();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
                UIUtility.showShortToast(response.getErrorMessage(), OpportunityDetailsActivity.this);
            }
        };

        opportunityMarkFavoriteResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleOpportunityFavoriteResponse(true);
                hideLoader();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
                UIUtility.showShortToast(response.getErrorMessage(), OpportunityDetailsActivity.this);
            }
        };

        opportunityMarkUnFavoriteResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleOpportunityFavoriteResponse(false);
                hideLoader();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
                UIUtility.showShortToast(response.getErrorMessage(), OpportunityDetailsActivity.this);
            }
        };

        respondToOpportunityListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleRespondOpportunityResponse();
                hideLoader();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
                UIUtility.showShortToast(response.getErrorMessage(), OpportunityDetailsActivity.this);
            }
        };

        directionResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                GoogleDirection direction = (GoogleDirection) response.getResponse();
                duration = StringUtility.validateEmptyString(direction.getDuration(), "--");
                getDetailsAPICall();
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                getDetailsAPICall();
            }
        };

        opportunitiesResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleOpportunitiesOnSuccessResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
            }
        };

        favouriteOpportunitiesResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleFavouriteOpportunitiesOnSuccessResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
            }
        };

        searchResponseListener = new APIResponseListner() {

            @Override
            public void onSuccess(CustomHttpResponse response) {

                handleSearchOpportunitiesOnSuccessResponse(response);
            }

            @Override
            public void onFailure(CustomHttpResponse response) {

                hideLoader();
            }
        };
    }

    private void handleOpportunityDetailsResponse(CustomHttpResponse response) {

        Opportunity opportunity = (Opportunity) response.getResponse();
        opportunity.setDuration(duration);
        DataModel.updateOpportunity(opportunity);
        if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.SEARCH) {

            DataModel.updateSearchedOpportunity(opportunity);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                populateData();
                hideLoader();
            }
        }, 100);
    }

    private void getDirectionsAPICall() {

        if (LocationManager.getInstance(this) != null && LocationManager.getInstance(this).getCurrentLocation() != null) {

            service.oppotunityService.getDirections(DataModel.getActiveOpportunities().get(selectedIndex).getAddress().getCoordinates(), new LatLng(LocationManager.getInstance(this).getCurrentLocation().getLatitude(), LocationManager.getInstance(this).getCurrentLocation().getLongitude()), directionResponseListener);
        } else {

            getDetailsAPICall();
        }
    }

    private void handleOpportunityFavoriteResponse(boolean isFavorite) {

        updatedOpportunity.setIsFavorite(isFavorite);
        DataModel.updateOpportunity(updatedOpportunity);

        if (isFavorite) {

            updatedOppMap.get("f_" + updatedOpportunity.getOpportunityId()).setIsFavorite(isFavorite);
            updatedOppMap.remove("uf_"+updatedOpportunity.getOpportunityId());
        } else {

            updatedOppMap.get("uf_" + updatedOpportunity.getOpportunityId()).setIsFavorite(isFavorite);
            unmarkOpportunityFavorite=updatedOpportunity;
            unMarkFavoriteOpportunity.put(updatedOpportunity.getOpportunityId(),updatedOpportunity);
            updatedOppMap.remove("f_" + updatedOpportunity.getOpportunityId());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                populateData();
            }
        }, 100);
    }

    private void populateData() {

        ((OpportunityDetailsView) view).populateData(selectedIndex);
        ((OpportunityDetailsView) view).setOpportunityPageChangeListener(this);
    }

    private boolean shouldMakeDetailsAPICall() {

        return DeviceUtility.isInternetConnectionAvailable(this);
    }

    private void getDetailsAPICall() {

        service.oppotunityService.getOpportunityDetails(DataModel.getActiveOpportunities().get(selectedIndex).getOpportunityId(), opportunityDetailsResponseListener);
        Integer i = selectedIndex;
    }

    private void getParcelableData() {

        if (getIntent().getExtras() != null) {

            selectedIndex = getIntent().getExtras().getInt("selected_position");
            ((OpportunityDetailsView) view).setSelectedIndex(selectedIndex);
        }
    }

    public Boolean isSelectedOpportunityIsResponded() {

        if (DataModel.getActiveOpportunities().get(getSelectedIndex()).isResponded()) {

            return true;
        }

        return false;
    }

    @Override
    public void onPageSelected(int index) {

        selectedIndex = index;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (shouldMakeDetailsAPICall()) {

                    getDirectionsAPICall();
                } else {

                    Utils.showInternetConnectionNotFoundMessage();
                    populateData();
                }
            }
        }, 200);
    }

    private void updateList() {

         //int favoriteListCount = DataModel.getFavouriteOpportunitites().size();
        int   favoriteListCount = DataModel.getOpportunityListFavouriteSize();
        Iterator it = updatedOppMap.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            String key = (String) pair.getKey();
            String firstTwoCharactersOfKey = key.substring(0, 2);

            if (firstTwoCharactersOfKey.equals("f_") && DBHelper.getInstance(this).getOpportunityById(((Opportunity)pair.getValue()).getOpportunityId(), DBHelper.TABLE_FAVORITE_OPPORTUNITY) == null) {

                DataModel.addOpportunity((Opportunity) pair.getValue(), DBHelper.TABLE_FAVORITE_OPPORTUNITY);
                favoriteListCount++;

            } else if (firstTwoCharactersOfKey.equals("uf") && DBHelper.getInstance(this).getOpportunityById(((Opportunity)pair.getValue()).getOpportunityId(), DBHelper.TABLE_FAVORITE_OPPORTUNITY) != null) {

                DataModel.deleteOpportunity((Opportunity) pair.getValue(), DBHelper.TABLE_FAVORITE_OPPORTUNITY);
                favoriteListCount--;
            }
        }
if(isOpportunityFavorite) {
    DataModel.setOpportunityListFavouriteSize(favoriteListCount);
}
        else {
    DataModel.setOpportunityListFavouriteSize(favoriteListCount-1);
}
    }

    public void markFavorite(Opportunity opportunity) {

        showLoader();
        updatedOpportunity = opportunity;
        updatedOppMap.put("f_" + opportunity.getOpportunityId(), opportunity);
        markFavoriteAPICall(opportunity.getOpportunityId());
        ((OpportunityDetailsView) view).markFavorite();
        shouldUpdateList = true;
    }

    private void markFavoriteAPICall(String opportunityId) {

        service.oppotunityService.markOpportunityFavorite(opportunityId, opportunityMarkFavoriteResponseListener);
    }

    public void markUnFavorite(Opportunity opportunity) {

        showLoader();
        updatedOpportunity = opportunity;
        updatedOppMap.put("uf_" + opportunity.getOpportunityId(), opportunity);
        markUnFavoriteAPICall(opportunity.getOpportunityId());
        shouldUpdateList = true;
    }

    private void markUnFavoriteAPICall(String opportunityId) {

        service.oppotunityService.markOpportunityUnFavorite(opportunityId, opportunityMarkUnFavoriteResponseListener);
    }

    private void respondAPICall(String opportunityId) {

        service.oppotunityService.respondToOpportunity(opportunityId, respondToOpportunityListener);
    }

    public void setCurrentPage() {

        // int currentPage = ((OpportunityDetailsView) view).getCurrentPage ();
        // ((OpportunityDetailsView) view).setCurrentPage (currentPage + 1);
    }

    public void deleteOpportunity(Opportunity opportunity) {

        updatedOpportunity = opportunity;
        updatedOppMap.put("d_" + opportunity.getOpportunityId(), opportunity);
        deleteOpportunityAPICall(opportunity);
    }

    private void deleteOpportunityAPICall(Opportunity opportunity) {

        service.oppotunityService.deleteOpportunity(opportunity.getOpportunityId(), opportunityDeleteResponseListener);
    }

    private void handleDeleteOpportunityResponse() {

        isOpportunityFavorite=DataModel.deductListCount(updatedOpportunity);
        DataModel.deleteOpportunity(updatedOpportunity);
        finishActivity(true);
    }

    private void finishActivity(Boolean isDeleteOperation) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                updateList();

                Intent intent = new Intent();
                intent.putExtra("deleted_object", updatedOpportunity);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 100);
    }

    public void respondToOpportunity(Opportunity opportunity) {

        showLoader();
        updatedOpportunity = opportunity;
        respondAPICall(opportunity.getOpportunityId());
    }

    private void handleRespondOpportunityResponse() {

        shouldUpdateList = true;
        updatedOpportunity.setIsResponded(true);
        DataModel.updateOpportunity(updatedOpportunity);
        populateData();
    }

    public void startConversationMessageActivity() {

        Intent intent = new Intent(this, OpportunityMessageActivity.class);
        intent.putExtra(getString(R.string.selected_position), getSelectedIndex());
        intent.putExtra(getString(R.string.is_message_list), false);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (shouldUpdateList) {

            finishActivity(true);
        } else {

            super.onBackPressed();
        }
    }

    public int getSelectedIndex() {

        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {

        this.selectedIndex = selectedIndex;
    }

    public void expandMap() {

        ((OpportunityDetailsView) view).expandMap();
    }

    public void loadMoreOpportunities() {

        if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.ALL) {

            if (shouldMakeOpportunityAPICall()) {

                showLoader();
               // getOpportunitiesAPICall();
                service.oppotunityService.getOppotunities(OpportunityListActivity.opportunityListPageSize, OpportunityListActivity.totalListSkipSize,opportunitiesResponseListener);
                //getOpportunitiesAPICall ();
                //isLoading = true;
                OpportunityListView.isLoadMore = false;
            }

        } else if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.FAVOURITE) {

            loadMoreFavouriteOpportunities();

        } else {

            loadMoreSearchedOpportunities();
        }
    }

    private void getOpportunitiesAPICall() {

        Profile profile = DataModel.getProfile();
        if (profile == null) {

            return;
        }

        ArrayList<Preference> preferences = profile.getPreferences();

        if (CollectionUtility.isEmptyOrNull(preferences)) {

            preferences = new ArrayList<Preference>();

            Preference pref = new Preference();
            pref.setType("title");
            pref.setValue(profile.getDesignation());
            preferences.add(pref);

            pref = new Preference();
            pref.setType("location");
            pref.setValue(profile.getPreferredLocation());
            preferences.add(pref);

            if (!CollectionUtility.isEmptyOrNull(profile.getSkills())) {

                for (String skill : profile.getSkills()) {

                    pref = new Preference();
                    pref.setType("skills");
                    pref.setValue(skill);
                    preferences.add(pref);
                }
            }
        }

        Search search = new Search();
        service.oppotunityService.getOppotunities(search.getSearchPayload(OpportunityListActivity.opportunityListPageSize, OpportunityListActivity.totalListSkipSize, null, preferences), opportunitiesResponseListener);
    }

    private boolean shouldMakeOpportunityAPICall() {

        return (DataModel.getOpportunitites().size() < DataModel.getOpportunityListTotalSize());
    }

    private void handleOpportunitiesOnSuccessResponse(CustomHttpResponse response) {

        OpportunityResponse opportunityResponse = (OpportunityResponse) response.getResponse();
        updateOpportunityListMetaData(opportunityResponse);
        incrementTotalListSkip();
        ((OpportunityDetailsView) view).selectNextPage();
        shouldUpdateList = true;
    }

    private void updateOpportunityListMetaData(OpportunityResponse opportunityResponse) {

        DataModel.setOpportunityListTotalSize(opportunityResponse.getMeta().getTotal());
    }

    private void incrementTotalListSkip() {

        if (DataModel.getOpportunitites().size() < DataModel.getOpportunityListTotalSize()) {

            OpportunityListActivity.totalListSkipSize = DataModel.getOpportunitites().size() - 1;
        }
    }

    public void loadMoreFavouriteOpportunities() {

        if (shouldMakeFavouriteOpportunityAPICall()) {

            showLoader();
            service.oppotunityService.getFavouriteOppotunities(OpportunityListActivity.opportunityListPageSize, OpportunityListActivity.favouriteListSkipSize, favouriteOpportunitiesResponseListener);
        }
    }

    private boolean shouldMakeFavouriteOpportunityAPICall() {

        return (DataModel.getFavouriteOpportunitites().size() < DataModel.getOpportunityListFavouriteSize() || DataModel.getOpportunityListFavouriteSize() == -1);
    }

    private void handleFavouriteOpportunitiesOnSuccessResponse(CustomHttpResponse response) {

        OpportunityResponse opportunityResponse = (OpportunityResponse) response.getResponse();
        updateFavouriteOpportunityListMetaData(opportunityResponse);
        incrementFavouriteListSkip();
        ((OpportunityDetailsView) view).selectNextPage();
        shouldUpdateList = true;
    }

    private void updateFavouriteOpportunityListMetaData(OpportunityResponse opportunityResponse) {

        DataModel.setOpportunityListFavouriteSize(opportunityResponse.getMeta().getTotal());
    }

    private void incrementFavouriteListSkip() {

        if (DataModel.getFavouriteOpportunitites().size() < DataModel.getOpportunityListFavouriteSize()) {

            OpportunityListActivity.favouriteListSkipSize = DataModel.getFavouriteOpportunitites().size() - 1;
        }
    }

    public void loadMoreSearchedOpportunities() {

        if (shouldMakeSearchOpportunityAPICall()) {

            showLoader();
            Search searchObject = new Search();
            searchDataAPICall(searchObject.getSearchPayload(OpportunityListActivity.opportunityListPageSize, DataModel.getSearchOpportunitites().size(), OpportunityListActivity.searchView.getQuery().toString(), OpportunityListActivity.preferenceItems));
        }
    }

    private boolean shouldMakeSearchOpportunityAPICall() {

        return DataModel.getSearchOpportunitites().size() < DataModel.getSearchResultsCount();
    }

    private void searchDataAPICall(String payload) {

        service.oppotunityService.searchData(payload, searchResponseListener);
    }

    private void handleSearchOpportunitiesOnSuccessResponse(CustomHttpResponse response) {

        OpportunitySearchResponse opportunityResponse = (OpportunitySearchResponse) response.getResponse();
        DataModel.getSearchOpportunitites().addAll(opportunityResponse.getList());
        updateSearchOpportunityListMetaData(opportunityResponse);
        DataModel.setSearchResultsCount(opportunityResponse.getMeta().getTotal());
        ((OpportunityDetailsView) view).selectNextPage();
        shouldUpdateList = true;
        hideLoader();
    }

    private void updateSearchOpportunityListMetaData(OpportunitySearchResponse opportunityResponse) {

        OpportunityListActivity.searchListTotalSize = (opportunityResponse.getMeta().getTotal());
    }
}
