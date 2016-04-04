package com.mboconnect.model;

import com.google.gson.Gson;
import com.mboconnect.Application;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.AccessToken;
import com.mboconnect.entities.Meta;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Preference;
import com.mboconnect.entities.Profile;
import com.mboconnect.helpers.DBHelper;
import com.tenpearls.android.utilities.PreferenceUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ali.mehmood on 6/30/2015.
 */
public class DataModel {

	private static ArrayList<Opportunity> opportunities			= new ArrayList<Opportunity> ();
	private static ArrayList<Opportunity> favoriteOpportunities	= new ArrayList<Opportunity> ();
	private static ArrayList<Opportunity> searchResult			= new ArrayList<Opportunity> ();
	private static ArrayList<Preference>  preferences			= new ArrayList<Preference> ();
	private static AccessToken			  accessToken;
	private static int					  searchResultsCount	= 0;
	private static boolean				  isRefreshToken		= false;

	static EnumConstants.OpportunitiesType opportunityListType;

	public static ArrayList<Opportunity> getOpportunitites () {

		Thread thread = new Thread () {
			@Override
			public void run () {

				opportunities = DBHelper.getInstance (Application.getInstance ()).getAllOpportunities (DBHelper.TABLE_OPPORTUNITY);
			}
		};
		thread.start ();
		try {
			thread.join ();
		}
		catch (InterruptedException e) {
			e.printStackTrace ();
		}

		return opportunities;
	}

	public static ArrayList<Opportunity> getFavouriteOpportunitites () {

		Thread thread = new Thread () {
			@Override
			public void run () {

				favoriteOpportunities = DBHelper.getInstance (Application.getInstance ()).getAllOpportunities (DBHelper.TABLE_FAVORITE_OPPORTUNITY);
			}
		};
		thread.start ();
		try {
			thread.join ();
		}
		catch (InterruptedException e) {
			e.printStackTrace ();
		}

		return favoriteOpportunities;
	}

	public static void setSearchOpportunitites (ArrayList<Opportunity> opportunitites) {

		DataModel.searchResult.addAll (opportunitites);
	}

	public static ArrayList<Opportunity> getSearchOpportunitites () {

		return searchResult;
	}

	public static Profile getProfile () {

		return DBHelper.getInstance (Application.getInstance ()).getProfile ();
	}

	public static void updateOpportunity (final Opportunity opportunity) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				DBHelper.getInstance (Application.getInstance ()).updateOpportunity (opportunity);
			}
		};
		thread.start ();
	}

	public static void deleteOpportunity (final Opportunity opportunity) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				DBHelper.getInstance (Application.getInstance ()).deleteOpportunityById(opportunity.getOpportunityId());
			}
		};
		thread.start ();
	}

	public static void deleteOpportunity (final Opportunity opportunity, final String table) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				DBHelper.getInstance (Application.getInstance ()).deleteOpportunityById (opportunity.getOpportunityId (), table);
			}
		};
		thread.start ();
	}

	public static void addOpportunity (final Opportunity opportunity, final String table) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				if (table.equals (DBHelper.TABLE_FAVORITE_OPPORTUNITY)) {

					DBHelper.getInstance (Application.getInstance ()).addFavoriteOpportunity (opportunity);
				}
				else {
					DBHelper.getInstance (Application.getInstance ()).addOpportunity (opportunity);
				}
			}
		};
		thread.start ();
	}

	public static void clear () {

		DBHelper.getInstance (Application.getInstance ()).deleteAllRows (DBHelper.TABLE_OPPORTUNITY);
		DBHelper.getInstance (Application.getInstance ()).deleteAllRows (DBHelper.TABLE_FAVORITE_OPPORTUNITY);
		DBHelper.getInstance (Application.getInstance ()).deleteAllRows (DBHelper.TABLE_PROFILE);
		DBHelper.getInstance (Application.getInstance ()).deleteAllRows (DBHelper.TABLE_META);
		searchResult.clear ();
		accessToken = null;
		OpportunityListActivity.isDirectSearchInProgress = false;
	}

	public static int getOpportunityListFavouriteSize () {

		return DBHelper.getInstance (Application.getInstance ()).getMetaById ().getFavoriteOpportunitiesCount ();
	}

	public static int getOpportunityListTotalSize () {

		return DBHelper.getInstance (Application.getInstance ()).getMetaById ().getAllOpportunitiesCount();
	}

	public static int getMessageTotalSize () {

		return DBHelper.getInstance (Application.getInstance ()).getMetaById ().getMessagesCount();
	}

	public static void setOpportunityListTotalSize (final int opportunityListTotalSize) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				Meta meta = new Meta ();
				meta.setAllOpportunitiesCount (opportunityListTotalSize);
				meta.setFavoriteOpportunitiesCount (-1);
				meta.setMessagesCount (-1);
				DBHelper.getInstance (Application.getInstance ()).updateMeta (meta);
			}
		};
		thread.start ();
	}

	public static void setOpportunityListFavouriteSize (final int opportunityListFavouriteSize) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				Meta meta = new Meta ();
				meta.setAllOpportunitiesCount (-1);
				meta.setMessagesCount(-1);
				meta.setFavoriteOpportunitiesCount(opportunityListFavouriteSize);
				DBHelper.getInstance (Application.getInstance ()).updateMeta (meta);
			}
		};
		thread.start ();
	}

	public static void setMessagesCount (final int messagesCount) {

		Thread thread = new Thread () {
			@Override
			public void run () {

				Meta meta = new Meta ();
				meta.setAllOpportunitiesCount (-1);
				meta.setFavoriteOpportunitiesCount(-1);
				meta.setMessagesCount(messagesCount);
				DBHelper.getInstance (Application.getInstance ()).updateMeta (meta);
			}
		};
		thread.start ();
	}

	public static ArrayList<Preference> getPreferences () {

		return preferences;
	}

	public static void setPreferences (ArrayList<Preference> preferences) {

		DataModel.preferences = preferences;
	}

	public static EnumConstants.OpportunitiesType getOpportunityListType () {

		return opportunityListType;
	}

	public static void setOpportunityListType (EnumConstants.OpportunitiesType opportunityListType) {

		DataModel.opportunityListType = opportunityListType;
	}

	public static boolean deductListCount (Opportunity opportunity) {


			DataModel.setOpportunityListTotalSize (DataModel.getOpportunityListTotalSize () - 1);
		
		if (opportunity.isFavorite () && DBHelper.getInstance (Application.getInstance ()).getOpportunityById (opportunity.getOpportunityId (), DBHelper.TABLE_FAVORITE_OPPORTUNITY) != null) {

			DataModel.setOpportunityListFavouriteSize (DataModel.getOpportunityListFavouriteSize () - 1);
		}

		Iterator it = OpportunityDetailsActivity.unMarkFavoriteOpportunity.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			String key = (String) pair.getKey();
			//String firstTwoCharactersOfKey = key.substring(0, 2);

			if (key.equals(opportunity.getOpportunityId())) {

				return false;
			}
		}

		return true;
	}

	public static ArrayList<Opportunity> getActiveOpportunities () {

		if (DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.ALL) {

			return getOpportunitites ();
		}
		else if ((DataModel.getOpportunityListType () == EnumConstants.OpportunitiesType.SEARCH)) {

			return getSearchOpportunitites ();
		}

		return getFavouriteOpportunitites ();
	}

	public static void setProfile (Profile profile) {

		DBHelper.getInstance (Application.getInstance ()).addProfile (profile);
	}

	public static String getAccessToken () {

		if (accessToken == null) {

			Gson gson = new Gson ();
			accessToken = gson.fromJson (PreferenceUtility.getString (Application.getInstance (), AppConstants.KEY_ACCESS_TOKEN, ""), AccessToken.class);
		}
		return (accessToken == null ? null : accessToken.getAccessToken ());
	}

	public static String getRefreshToken () {

		if (accessToken == null) {

			Gson gson = new Gson ();
			accessToken = gson.fromJson (PreferenceUtility.getString (Application.getInstance (), AppConstants.KEY_ACCESS_TOKEN, ""), AccessToken.class);
		}
		return (accessToken == null ? null : accessToken.getRefreshToken ());
	}

	public static void setAccessToken (AccessToken accessToken) {

		DataModel.accessToken = accessToken;

		Gson gson = new Gson ();
		String json = gson.toJson (DataModel.accessToken);

		PreferenceUtility.setString (Application.getInstance (), AppConstants.KEY_ACCESS_TOKEN, json);
	}

	public static void setLoginCounter (int count) {

		PreferenceUtility.setInteger (Application.getInstance (), AppConstants.KEY_LOGIN_COUNT, count);
	}

	public static int getLoginCounter () {

		return PreferenceUtility.getInteger (Application.getInstance (), AppConstants.KEY_LOGIN_COUNT, 0);
	}

	public static int getSearchResultsCount () {

		return searchResultsCount;
	}

	public static void setSearchResultsCount (int searchResultsCount) {

		DataModel.searchResultsCount = searchResultsCount;
	}

	public static void updateSearchedOpportunity (Opportunity updatedOpportunity) {

		for (int i = 0; i < getSearchOpportunitites ().size (); i++) {

			if (getSearchOpportunitites ().get (i).getOpportunityId ().equalsIgnoreCase (updatedOpportunity.getOpportunityId ())) {

				getSearchOpportunitites ().set (i, updatedOpportunity);
			}
		}
	}

	public static void setIsRefreshToken (boolean isRefreshToken) {

		DataModel.isRefreshToken = isRefreshToken;
	}

	public static boolean isIsRefreshToken () {

		return isRefreshToken;
	}
}
