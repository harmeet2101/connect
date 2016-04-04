/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood   - ali.mehmood@tenpearls.com
 * Arsalan Ahmed - arsalan.ahmed@tenpearls.com
 * M. AzfarSiddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.components;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tenpearls.android.R;
import com.tenpearls.android.entities.GooglePlace;
import com.tenpearls.android.interfaces.GooglePlaceAutoCompleteTextViewListener;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

/**
 * Displays list of places against a search term by querying Google's Places.
 * After a user selects a place, the component will fetch details of that place
 * from the API and fire the listener callback.
 * 
 * @author 10Pearls
 */
public class GooglePlacesAutoCompleteTextView extends RelativeLayout implements OnItemClickListener, TextWatcher {

	/** The Google Places Search API URL. */
	private static final String                     URL_GOOGLE_PLACE_SEARCH_API  = "https://maps.googleapis.com/maps/api/place/queryautocomplete/json";

	/** The Google Place Detail API URL. */
	private static final String                     URL_GOOGLE_PLACE_DETAILS_API = "https://maps.googleapis.com/maps/api/place/details/json";

	/** Results will be displayed inside this AutoCompleteTextView. */
	private AutoCompleteTextView                    txtGoogleSearch;

	/**
	 * This progress bar is used to let the user know when API call is in
	 * flight.
	 */
	private ProgressBar                             progressBar;

	/** String array to hold the place names returned from the API. */
	private ArrayList<String>                       googlePlaceNames;

	/** Typed array to store place objects as returned from the server. */
	private ArrayList<GooglePlace>                  googlePlaces;

	/**
	 * This timer is used to automatically hit the API after user is done with
	 * typing the search query.
	 */
	private Timer                                   searchQueryTimer;

	/** Set this listener to get events. */
	private GooglePlaceAutoCompleteTextViewListener listener;

	/** Set latitude to get results which are nearby to your location. */
	private double                                  latitude;

	/** Set longitude to get results which are nearby to your location. */
	private double                                  longitude;

	/** This must be set in order to successfully query the Google Places API. */
	private String                                  googleMapsKey;

	/**
	 * Set radius if you want to restrict the results based on the region formed
	 * by the radius value and your provided latitude and longitude.
	 */
	private int                                     radius;

	/** Flag to hold whether user provided a lat/long pair or not. */
	private boolean                                 didProvideLocation;

	/**
	 * The text to display as placeholder in the AutoCompleteTextView when user
	 * has not entered anything.
	 */
	private String                                  hint;

	/**
	 * Instantiates a new google places auto complete text view.
	 * 
	 * @param context A valid context
	 */
	public GooglePlacesAutoCompleteTextView (Context context) {

		super (context);
	}

	/**
	 * Instantiates a new google places auto complete text view.
	 * 
	 * @param context A valid context
	 * @param attrs The attribute set
	 */
	public GooglePlacesAutoCompleteTextView (Context context, AttributeSet attrs) {

		super (context, attrs);
	}

	/**
	 * Instantiates a new google places auto complete text view.
	 * 
	 * @param context A valid context
	 * @param attrs The attribute set
	 * @param defStyle The def style
	 */
	public GooglePlacesAutoCompleteTextView (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
	}

	/**
	 * Call this when you are done with configuring the control. Make sure to
	 * set the Google Maps Key before, else it will throw an exception.
	 */
	public void initialize () {

		if (googleMapsKey == null || googleMapsKey.length () == 0) {
			throw new IllegalStateException (this.getClass ().getName () + " >> Did you forget to set Google Maps Key?");
		}

		initUI ();
		setSearchQueryChangedListener ();
	}

	/**
	 * Resets the component's state i.e. empties the AutoCompleteTextView, and
	 * internally used arrays.
	 */
	public void clear () {

		txtGoogleSearch.setText ("");

		if (googlePlaceNames != null)
			googlePlaceNames.clear ();

		if (googlePlaces != null)
			googlePlaces.clear ();
	}

	/**
	 * Inflates layout and creates objects for different UI controls.
	 */
	private void initUI () {

		LayoutInflater inflater = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate (R.layout.component_google_places_search, null);
		txtGoogleSearch = (AutoCompleteTextView) rootView.findViewById (R.id.txtGoogleSearch);
		progressBar = (ProgressBar) rootView.findViewById (R.id.progressBar);

		txtGoogleSearch.setThreshold (1);
		txtGoogleSearch.setOnItemClickListener (this);

		if (!StringUtility.isEmptyOrNull (this.hint)) {
			txtGoogleSearch.setHint (this.hint);
		}

		addView (rootView);
	}

	/**
	 * We need to be able to determine when the user typed query changes.
	 * Therefore, setting TextChangedListener.
	 */
	private void setSearchQueryChangedListener () {

		txtGoogleSearch.addTextChangedListener (this);
	}

	/**
	 * Called when a search should be initialized. However, the search may not
	 * be started depending upon the search term.
	 */
	private void handleSearch () {

		String searchTerm = txtGoogleSearch.getText ().toString ();

		if (StringUtility.isEmptyOrNull (searchTerm))
			return;

		if (searchTerm != null) {
			searchGooglePlaces (searchTerm);
		}
	}

	/**
	 * Converts the places list returned from API into a linear string array and
	 * displays the AutoCompleteTextView.
	 */
	private void showGooglePlacesSuggestions () {

		if (googlePlaces == null || googlePlaces.size () == 0)
			return;

		final String[] googlePlaceNames = getGooglePlaceNamesArray ();
		txtGoogleSearch.setAdapter (new ArrayAdapter<String> (getActivity (), R.layout.component_google_places_search_item, googlePlaceNames));
		txtGoogleSearch.showDropDown ();
	}

	/**
	 * Do the housekeeping when the search API returns success.
	 */
	private void handleGooglePlaceSearchAPISuccess () {

		hideProgressBar ();
		showGooglePlacesSuggestions ();
	}

	/**
	 * Handles API success for place details call and fires the listener method
	 * that which place was selected from the AutoCompleteTextView.
	 * 
	 * @param googlePlace The place object which was selected.
	 */
	private void handleGooglePlaceDetailAPISuccess (GooglePlace googlePlace) {

		hideProgressBar ();
		listener.onGooglePlaceSelected (googlePlace);
	}

	/**
	 * Handles the scenario when an error is returned from the search API.
	 */
	private void handleGoogleSearchAPIFailure () {

		hideProgressBar ();
	}

	/**
	 * Handles the case when an error is returned from the details API.
	 */
	private void handleGooglePlaceDetailAPIFailure () {

		hideProgressBar ();
	}

	/**
	 * Makes the progress bar visible to indicate that an API operation is in
	 * progress.
	 */
	private void showProgressBar () {

		progressBar.setVisibility (View.VISIBLE);
	}

	/**
	 * Hides the progress bar.
	 */
	private void hideProgressBar () {

		progressBar.setVisibility (View.GONE);
	}

	/**
	 * Dispatches the API call against the user typed search term. Also does
	 * some house-keeping like showing the progress bar, checking network state
	 * etc before initiating the network call.
	 * 
	 * @param searchTerm A non-empty search term typed by the user
	 */
	private void searchGooglePlaces (final String searchTerm) {

		if (!DeviceUtility.isInternetConnectionAvailable (getContext ()))
			return;

		showProgressBar ();

		String urlEncodedSearchTerm, url = null;

		try {
			urlEncodedSearchTerm = URLEncoder.encode (searchTerm, "utf-8");

			url = URL_GOOGLE_PLACE_SEARCH_API + "?";
			url += "input=" + urlEncodedSearchTerm;
			url += "&key=" + googleMapsKey;
			url += "&sensor=true";
			url += "&rankby=distance";

			if (didProvideLocation)
				url += "&location=" + String.format ("%f,%f", latitude, longitude);

			if (radius > 0)
				url += "&radius=" + String.valueOf (radius);
		}
		catch (Exception e) {
			handleGoogleSearchAPIFailure ();
			return;
		}

		JsonObjectRequest jsonRequest = new JsonObjectRequest (Method.GET, url, null, new Response.Listener<JSONObject> () {

			@Override
			public void onResponse (JSONObject jsonObject) {

				String response = jsonObject.toString ();
				JsonObject container = JsonUtility.parseToJsonObject (response);
				JsonArray jsonArray = container.getAsJsonArray ("predictions");

				googlePlaces = new ArrayList<GooglePlace> ();

				for (JsonElement jsonElement : jsonArray) {
					GooglePlace googlePlace = new GooglePlace ();
					googlePlace.deserializeFromJSON (jsonElement.toString ());
					googlePlaces.add (googlePlace);
				}

				handleGooglePlaceSearchAPISuccess ();
			}
		}, new Response.ErrorListener () {

			@Override
			public void onErrorResponse (VolleyError arg0) {

				handleGoogleSearchAPIFailure ();
			}
		});

		VolleyManager.getInstance ().getRequestQueue ().add (jsonRequest);
	}

	/**
	 * Dispatches the API call to get details of the place selected by the user
	 * from the AutoComplete.
	 * 
	 * @param googlePlace The place selected by the user
	 * @return the google place detail
	 */
	private void getGooglePlaceDetail (GooglePlace googlePlace) {

		showProgressBar ();

		String url = URL_GOOGLE_PLACE_DETAILS_API + "?";
		url += "reference=" + googlePlace.getReferenceId ();
		url += "&key=" + googleMapsKey;
		url += "&sensor=true";

		JsonObjectRequest jsonRequest = new JsonObjectRequest (Method.GET, url, null, new Response.Listener<JSONObject> () {

			@Override
			public void onResponse (JSONObject jsonObject) {

				try {
					String response = jsonObject.toString ();
					JsonObject container = JsonUtility.parseToJsonObject (response);
					JsonObject resultContainer = container.getAsJsonObject ("result");
					double latitude = Double.valueOf (resultContainer.getAsJsonObject ("geometry").getAsJsonObject ("location").get ("lat").toString ());
					double longitude = Double.valueOf (resultContainer.getAsJsonObject ("geometry").getAsJsonObject ("location").get ("lng").toString ());
					String name = JsonUtility.getString (resultContainer, "name");
					String formattedAddress = JsonUtility.getString (resultContainer, "formatted_address");

					GooglePlace googlePlace = new GooglePlace ();
					googlePlace.setDescription (name);
					googlePlace.setAddress (formattedAddress);
					googlePlace.setLatitude (latitude);
					googlePlace.setLongitude (longitude);

					handleGooglePlaceDetailAPISuccess (googlePlace);
				}
				catch (Exception e) {
					handleGooglePlaceDetailAPIFailure ();
				}
			}
		}, new Response.ErrorListener () {

			@Override
			public void onErrorResponse (VolleyError arg0) {

				handleGooglePlaceDetailAPIFailure ();

			}
		});

		VolleyManager.getInstance ().getRequestQueue ().add (jsonRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
	 * int, int)
	 */
	@Override
	public void onTextChanged (CharSequence text, int start, int lengthBefore, int lengthAfter) {

	}

	/**
	 * Handles AutoCompleteTextView's item click. It's also responsible for
	 * dispatching place details call.
	 * 
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param position the position
	 * @param arg3 the arg3
	 */
	@Override
	public void onItemClick (AdapterView<?> arg0, View arg1, int position, long arg3) {

		cancelTimerToHitGooglePlaceSearchAPI ();
		UIUtility.hideSoftKeyboard (txtGoogleSearch, getActivity ());
		txtGoogleSearch.clearFocus ();
		txtGoogleSearch.dismissDropDown ();
		getGooglePlaceDetail (googlePlaces.get (position));
	}

	/**
	 * Prepares the data source for setting up AutoCompleteTextView's items.
	 * 
	 * @return String array data source for feeding to the AutoCompleteTextView
	 */
	private String[] getGooglePlaceNamesArray () {

		googlePlaceNames = new ArrayList<String> ();

		for (GooglePlace googlePlace : googlePlaces) {
			googlePlaceNames.add (googlePlace.getDescription ());
		}

		String[] googlePlaces = new String[googlePlaceNames.size ()];
		googlePlaces = googlePlaceNames.toArray (googlePlaces);

		return googlePlaces;
	}

	/**
	 * Does the canceling of the timer used to initiate the place search API
	 * call.
	 */
	private void cancelTimerToHitGooglePlaceSearchAPI () {

		if (searchQueryTimer != null) {
			searchQueryTimer.cancel ();
			searchQueryTimer = null;
		}
	}

	/**
	 * Returns Activity to be used as the context. For internal purpose only.
	 * 
	 * @return Activity A valid context
	 */
	private Activity getActivity () {

		return (Activity) getContext ();
	}

	/**
	 * Callback method, fired when user changes text in the AutoComplete.
	 * 
	 * @param s the s
	 */
	@Override
	public void afterTextChanged (Editable s) {

		String searchQuery = txtGoogleSearch.getText ().toString ();

		if (!StringUtility.isEmptyOrNull (searchQuery)) {

			if (searchQueryTimer == null)
				searchQueryTimer = new Timer ();

			searchQueryTimer.schedule (new TimerTask () {

				@Override
				public void run () {

					getActivity ().runOnUiThread (new Runnable () {
						@Override
						public void run () {

							handleSearch ();
						}
					});

				}
			}, 1500);
		}
		else {
			cancelTimerToHitGooglePlaceSearchAPI ();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
	 * int, int, int)
	 */
	@Override
	public void beforeTextChanged (CharSequence s, int start, int count, int after) {

	}

	/**
	 * Setter for the component's listener.
	 * 
	 * @param listener The callback that will run
	 */
	public void setListener (GooglePlaceAutoCompleteTextViewListener listener) {

		this.listener = listener;
	}

	/**
	 * Set lat/long to get results tailored to specified location.
	 * 
	 * @param latitude Latitude of the location
	 * @param longitude Longitude of the location
	 */
	public void setLocation (double latitude, double longitude) {

		didProvideLocation = true;

		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Setter for providing Google Maps Key.
	 * 
	 * @param googleMapsKey The key generated from Google API console
	 */
	public void setGoogleMapsKey (String googleMapsKey) {

		this.googleMapsKey = googleMapsKey;
	}

	/**
	 * Setter for providing radius to filter the places.
	 * 
	 * @param radius Radius
	 */
	public void setRadius (int radius) {

		this.radius = radius;
	}

	/**
	 * Setter for providing placeholder text to show in the
	 * AutoCompleteTextView.
	 * 
	 * @param hint Placeholder text
	 */
	public void setHint (String hint) {

		this.hint = hint;
	}
}
