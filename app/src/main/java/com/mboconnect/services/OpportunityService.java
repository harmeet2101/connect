package com.mboconnect.services;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.entities.Conversation;
import com.mboconnect.entities.GoogleDirection;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.ConversationMessageResponse;
import com.mboconnect.services.response.OpportunityResponse;
import com.mboconnect.services.response.OpportunitySearchResponse;
import com.mboconnect.services.response.PreferenceResponse;
import com.mboconnect.services.response.StringResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class OpportunityService extends BaseService {

	public OpportunityService (Context context) {

		super (context);
	}


	public void getOppotunities(int pagesize,int skipsize,APIResponseListner responseListner){

		String query=String .format("{\"skip\":%d,\"page_size\":%d}",skipsize,pagesize);
		try {

			query = URLEncoder.encode (query, "utf-8");
		}
		catch (Exception e) {

			e.printStackTrace ();
		}
		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_OPPORTUNITIES + query);
		super.get (url, DataModel.getAccessToken (), responseListner, new OpportunityResponse (url));

	}


	public void getOppotunities (String payload, APIResponseListner responseListner) {

		String query = null;
		try {

			query = URLEncoder.encode (payload, "utf-8");
		}
		catch (UnsupportedEncodingException e) {

			e.printStackTrace ();
		}

		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_OPPORTUNITIES + query);
		super.get (url, DataModel.getAccessToken (), responseListner, new OpportunityResponse (url));

	}

	public void getFavouriteOppotunities (int pageSize, int skipSize, APIResponseListner responseListner) {

		String query = String.format ("{\"skip\":%d,\"page_size\":%d}", skipSize, pageSize);

		try {
			query = URLEncoder.encode (query, "utf-8");
		}
		catch (Exception e) {

		}

		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_FAVOURITE_OPPORTUNITIES + query);

		super.get (url, DataModel.getAccessToken (), responseListner, new OpportunityResponse (url));

	}

	public void getOpportunityDetails (String id, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_GET_OPPORTUNITY_DETAILS, id));

		super.get (url, DataModel.getAccessToken (), responseListner, new Opportunity ());
	}

	public void deleteOpportunity (String id, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_DELETE_HIDE_OPPORTUNITY, id));
		super.delete (url, DataModel.getAccessToken (), responseListner);

	}

	public void markOpportunityFavorite (String id, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_POST_MARK_FAVORITES));

		super.post (url, DataModel.getAccessToken (), getParamsForMarkFavorite (id), responseListner, new Opportunity ());
	}

	public void markOpportunityUnFavorite (String id, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_DELETE_MARK_UNFAVORITES, id));

		super.delete (url, DataModel.getAccessToken (), responseListner, new Opportunity ());
	}

	private String getParamsForMarkFavorite (String id) {

		JsonObject jsonObject = new JsonObject ();
		jsonObject.addProperty ("opportunity_id", id);
		return jsonObject.toString ();
	}

	public void respondToOpportunity (String id, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_POST_CREATE_CONVERSATIONS));
		super.post (url, DataModel.getAccessToken (), getParamsForMarkFavorite (id), responseListner, new Opportunity ());

	}

	public void getPreferenceData (APIResponseListner responseListner) {

		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_PREFERENCES);

		super.get (url, DataModel.getAccessToken (), responseListner, new PreferenceResponse ());

	}

	public void getConversation (String opportunityId, APIResponseListner responseListner) {

		String url = super.getServiceURL (ServiceConstants.SERVICE_GET_CONVERSATION + opportunityId);

		super.get (url, DataModel.getAccessToken (), responseListner, new Conversation ());

	}

	public void getConversationMessages (String conversationId, int pageSkip, int pageSize, APIResponseListner responseListner) {

		String query = String.format ("{\"skip\":%d,\"page_size\":%d}", pageSkip, pageSize);

		try {
			query = URLEncoder.encode (query, "utf-8");
		}
		catch (Exception e) {

		}

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_GET_CONVERSATION_MESSAGES, conversationId) + query);

		super.get (url, DataModel.getAccessToken (), responseListner, new ConversationMessageResponse ());
	}

	public void postConversationMessages (String conversationId, String message, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_POST_CONVERSATION_MESSAGES, conversationId));
		JsonObject jsonObject = new JsonObject ();
		jsonObject.addProperty (KeyConstants.MESSAGE, message);
		super.post (url, DataModel.getAccessToken (), jsonObject.toString (), responseListner, new ConversationMessageResponse ());

	}

	public void markReadMessages (ArrayList messageList, String conversationId, APIResponseListner responseListner) {

		String url = super.getServiceURL (String.format (ServiceConstants.SERVICE_MARK_MESSAGES_READ, conversationId));
		JSONArray jsonArray = new JSONArray (messageList);
		JSONObject jsonObject = new JSONObject ();

		try {
			jsonObject.put (KeyConstants.MESSAGES, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}
		super.put (url, DataModel.getAccessToken (), jsonObject.toString (), responseListner, new StringResponse<Object> (null));

	}

	public void searchData (String payload, APIResponseListner responseListner) {

		String query = null;
		try {

			query = URLEncoder.encode (payload, "utf-8");
		}
		catch (UnsupportedEncodingException e) {

			e.printStackTrace ();
		}

		String url = super.getServiceURL (ServiceConstants.SERVICE_SEARCH + query);

		super.get (url, DataModel.getAccessToken (), responseListner, new OpportunitySearchResponse (url));

	}

	public void getDirections (LatLng origin, LatLng destination, APIResponseListner responseListner) {

		String url = getDirectionsUrl (origin, destination);

		super.get (url, null, responseListner, new GoogleDirection ());
	}

	private String getDirectionsUrl (LatLng origin, LatLng dest) {

		String originString = "origin=" + origin.latitude + "," + origin.longitude;
		String destString = "destination=" + dest.latitude + "," + dest.longitude;
		String sensor = "sensor=false";
		String parameters = originString + "&" + destString + "&" + sensor;
		String output = "json";

		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
	}
}
