package com.mboconnect.entities;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mboconnect.utils.Utils;
import com.tenpearls.android.utilities.CollectionUtility;

/**
 * Created by tahir on 13/07/15.
 */
public class Search extends BaseEntity {

	private static String KEY_OR	  = "$or";
	private static String KEY_AND	  = "$and";
	private static String KEY_FILTERS = "$filter";

	private static String KEY_REGEX	 = "$regex";
	private static String KEY_OPTION = "$options";

	private static String KEY_I = "i";

	private static String KEY_TITLE	   = "title";
	private static String KEY_SKILLS   = "skills";
	private static String KEY_LOCATION = "location";
	private static String KEY_HOURLY   = "hourly";
	private static String KEY_PROJECT  = "project";

	private static String KEY_GTE = "$gte";
	private static String KEY_EQ  = "$eq";

	private static String KEY_LOCATION_STATE   = "location.address.state";
	private static String KEY_LOCATION_CITY	   = "location.address.city";
	private static String KEY_LOCATION_STREET1 = "location.address.street1";
	private static String KEY_LOCATION_STREET2 = "location.address.street2";

	private static String KEY_RATE_RANGE_MAX_AMOUNT	= "rate_range.max.amount";
	private static String KEY_RATE_RANGE_MAX_TYPE	= "rate_range.max.type";
	private static String KEY_NEGOTIABLE			= "negotiable";

	private String title;
	private String skill;
	private String location;

	private int		 hour;
	private int		 project;
	private String[] data;

	@Override
	public void set (String jsonString) {

	}

	public String getSearchPayload (int pageSize, int skipSize, String query, ArrayList<Preference> preferences) {

		if (query != null) {

			this.data = query.split (" ");
		}

		if (CollectionUtility.isEmptyOrNull (preferences)) {

			return getBasicSearchPayload (pageSize, skipSize);
		}
		else {

			return getAdvanceSearchPayload (pageSize, skipSize, preferences);
		}
	}

	private String getAdvanceSearchPayload (int pageSize, int skipSize, ArrayList<Preference> preferences) {

		JSONObject jsonPayload = new JSONObject ();
		JSONObject jsonFilterObj = new JSONObject ();
		JSONArray jsonArrayOR = new JSONArray ();
		JSONArray jsonArrayAND = new JSONArray ();
		JSONObject jsonObjectAND = new JSONObject ();

		if (getTitleORArray (preferences) != null) {

			jsonArrayOR.put (getTitleORArray (preferences));
		}

		if (getLocationORArray (preferences) != null) {

			jsonArrayOR.put (getLocationORArray (preferences));
		}

		if (getRangeORArray (preferences) != null) {

			jsonArrayOR.put (getRangeORArray (preferences));
		}

		if (getSkillORArray (preferences) != null) {

			jsonArrayOR.put (getSkillORArray (preferences));
		}

		try {

			jsonFilterObj.put (KEY_AND, jsonArrayOR);

			jsonArrayAND.put (jsonFilterObj);
			if (data != null) {

				jsonArrayAND.put (getBasicSearchFilterObject ());
			}
			jsonObjectAND.put (KEY_AND, jsonArrayAND);

			jsonPayload.put (KEY_FILTERS, jsonObjectAND);
			jsonPayload.put ("page_size", pageSize);
			jsonPayload.put ("skip", skipSize);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}
		return jsonPayload.toString ();
	}

	private String getBasicSearchPayload (int pageSize, int skipSize) {

		JSONObject jsonPayload = new JSONObject ();

		try {

			jsonPayload.put (KEY_FILTERS, getBasicSearchFilterObjectWithoutRange ());
			jsonPayload.put ("page_size", pageSize);
			jsonPayload.put ("skip", skipSize);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}
		return jsonPayload.toString ();
	}

	private JSONObject getBasicSearchFilterObject () {

		JSONObject jsonFilterObj = new JSONObject ();
		JSONArray jsonArrayOR = new JSONArray ();

		for (int i = 0; i < data.length; i++) {

			jsonArrayOR.put (getTitleObject (data[i]));
			jsonArrayOR.put (getLocationCity (data[i]));
			jsonArrayOR.put (getSkillObject (data[i]));

			if (Utils.isNumeric (data[i])) {

				jsonArrayOR.put (getRange (KEY_HOURLY, data[i]));
				jsonArrayOR.put (getRange (KEY_PROJECT, data[i]));
			}
		}

		try {

			jsonFilterObj.put (KEY_OR, jsonArrayOR);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}
		return jsonFilterObj;
	}

	private JSONObject getBasicSearchFilterObjectWithoutRange () {

		JSONObject jsonFilterObj = new JSONObject ();
		JSONArray jsonArrayOR = new JSONArray ();

		for (int i = 0; i < data.length; i++) {

			jsonArrayOR.put (getTitleObject (data[i]));
			jsonArrayOR.put (getLocationCity (data[i]));
			jsonArrayOR.put (getSkillObject (data[i]));
		}

		try {

			jsonFilterObj.put (KEY_OR, jsonArrayOR);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}
		return jsonFilterObj;
	}

	private JSONObject getTitleORArray (ArrayList<Preference> preferences) {

		JSONObject jsonORObject = new JSONObject ();
		JSONArray jsonArray = new JSONArray ();

		for (int i = 0; i < preferences.size (); i++) {

			if (preferences.get (i).getType ().equalsIgnoreCase (KEY_TITLE)) {

				jsonArray.put (getTitleObject (preferences.get (i).getValue ()));

			}
		}

		if (CollectionUtility.isEmptyOrNull (preferences)) {

			for (int i = 0; i < data.length; i++) {

				jsonArray.put (getTitleObject (data[i]));
			}
		}

		try {

			if (jsonArray.length () <= 0) {

				return null;
			}
			jsonORObject.put (KEY_OR, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return jsonORObject;
	}

	private JSONObject getLocationORArray (ArrayList<Preference> preferences) {

		JSONObject jsonORObject = new JSONObject ();
		JSONArray jsonArray = new JSONArray ();
		for (int i = 0; i < preferences.size (); i++) {

			if (preferences.get (i).getType ().equalsIgnoreCase (KEY_LOCATION)) {

				jsonArray.put (getLocationCity (preferences.get (i).getValue ()));
			}
		}
		if (CollectionUtility.isEmptyOrNull (preferences)) {

			for (int i = 0; i < data.length; i++) {

				jsonArray.put (getLocationCity (data[i]));
			}
		}
		try {

			if (jsonArray.length () <= 0) {

				return null;
			}

			jsonORObject.put (KEY_OR, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return jsonORObject;
	}

	private JSONObject getRangeORArray (ArrayList<Preference> preferences) {

		JSONObject jsonORObject = new JSONObject ();

		JSONArray jsonArray = new JSONArray ();

		for (int i = 0; i < preferences.size (); i++) {

			if (preferences.get (i).getType ().equalsIgnoreCase (KEY_HOURLY) || preferences.get (i).getType ().equalsIgnoreCase (KEY_PROJECT)) {

				jsonArray.put (getRange (preferences.get (i).getType (), preferences.get (i).getValue ()));
			}
		}

		if (jsonArray.length () > 0) {

			jsonArray.put (getNegotiable ());
		}
		if (CollectionUtility.isEmptyOrNull (preferences)) {

			for (int i = 0; i < data.length; i++) {

				if (Utils.isNumeric (data[i])) {

					jsonArray.put (getRange (KEY_HOURLY, data[i]));
					jsonArray.put (getRange (KEY_PROJECT, data[i]));
				}
			}
		}

		try {
			if (jsonArray.length () <= 0) {

				return null;
			}
			jsonORObject.put (KEY_OR, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return jsonORObject;
	}

	private JSONObject getSkillORArray (ArrayList<Preference> preferences) {

		JSONObject jsonORObject = new JSONObject ();
		JSONArray jsonArray = new JSONArray ();

		for (int i = 0; i < preferences.size (); i++) {

			if (preferences.get (i).getType ().equalsIgnoreCase (KEY_SKILLS)) {

				jsonArray.put (getSkillObject (preferences.get (i).getValue ()));
			}
		}

		if (CollectionUtility.isEmptyOrNull (preferences)) {

			for (int i = 0; i < data.length; i++) {

				jsonArray.put (getSkillObject (data[i]));
			}
		}

		try {

			if (jsonArray.length () <= 0) {

				return null;
			}

			jsonORObject.put (KEY_OR, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return jsonORObject;
	}

	private JSONObject getTitleObject (String titleValue) {

		JSONObject title = new JSONObject ();
		JSONObject titleObject = new JSONObject ();
		try {
			titleObject.put (KEY_REGEX, escapeSpecialCharacters (titleValue));
			titleObject.put (KEY_OPTION, KEY_I);

			title.put (KEY_TITLE, titleObject);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return title;
	}

	private JSONObject getLocationState (String locationValue) {

		JSONObject object = new JSONObject ();
		JSONObject objectDetail = new JSONObject ();
		try {
			objectDetail.put (KEY_REGEX, locationValue);
			objectDetail.put (KEY_OPTION, KEY_I);

			object.put (KEY_LOCATION_STATE, objectDetail);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return object;

	}

	private JSONObject getLocationCity (String locationValue) {

		JSONObject object = new JSONObject ();
		JSONObject objectDetail = new JSONObject ();
		try {
			objectDetail.put (KEY_REGEX, escapeSpecialCharacters (locationValue));
			objectDetail.put (KEY_OPTION, KEY_I);

			object.put (KEY_LOCATION_CITY, objectDetail);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return object;
	}

	private JSONObject getLocationStreet1 (String locationValue) {

		JSONObject object = new JSONObject ();
		JSONObject objectDetail = new JSONObject ();
		try {
			objectDetail.put (KEY_REGEX, locationValue);
			objectDetail.put (KEY_OPTION, KEY_I);

			object.put (KEY_LOCATION_STREET1, objectDetail);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return object;
	}

	private JSONObject getLocationStreet2 (String locationValue) {

		JSONObject object = new JSONObject ();
		JSONObject objectDetail = new JSONObject ();
		try {
			objectDetail.put (KEY_REGEX, locationValue);
			objectDetail.put (KEY_OPTION, KEY_I);

			object.put (KEY_LOCATION_STREET2, objectDetail);

		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return object;
	}

	private JSONObject getRange (String type, String Value) {

		JSONArray jsonArray = new JSONArray ();
		JSONObject jsonAndObject = new JSONObject ();

		JSONObject objectMinAmount = new JSONObject ();
		JSONObject objectMinType = new JSONObject ();
		JSONObject objectType = new JSONObject ();
		JSONObject objectValue = new JSONObject ();
		try {
			objectValue.put (KEY_GTE, Long.parseLong (Value));
			objectMinAmount.put (KEY_RATE_RANGE_MAX_AMOUNT, objectValue);

			objectType.put (KEY_EQ, type);
			objectMinType.put (KEY_RATE_RANGE_MAX_TYPE, objectType);

			jsonArray.put (objectMinAmount);
			jsonArray.put (objectMinType);
			jsonAndObject.put (KEY_AND, jsonArray);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return jsonAndObject;
	}

	private JSONObject getNegotiable () {

		JSONObject objectMinType = new JSONObject ();
		JSONObject objectType = new JSONObject ();
		try {
			objectType.put (KEY_EQ, KEY_NEGOTIABLE);
			objectMinType.put (KEY_RATE_RANGE_MAX_TYPE, objectType);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return objectMinType;
	}

	private JSONObject getSkillObject (String skillValue) {

		JSONObject title = new JSONObject ();
		JSONObject titleObject = new JSONObject ();
		try {

			titleObject.put (KEY_REGEX, escapeSpecialCharacters (skillValue));
			titleObject.put (KEY_OPTION, KEY_I);
			title.put (KEY_SKILLS, titleObject);
		}
		catch (JSONException e) {
			e.printStackTrace ();
		}

		return title;
	}

	private String escapeSpecialCharacters (String input) {

		final StringBuilder result = new StringBuilder ();
		final StringCharacterIterator iterator = new StringCharacterIterator (input);
		char character = iterator.current ();
		while (character != CharacterIterator.DONE) {

			if (character == '&') {
				result.append ("\\&");
			}
			else if (character == '!') {
				result.append ("\\!");
			}
			else if (character == '#') {
				result.append ("\\#");
			}
			else if (character == '$') {
				result.append ("\\$");
			}
			else if (character == '%') {
				result.append ("\\%");
			}
			else if (character == '(') {
				result.append ("\\(");
			}
			else if (character == ')') {
				result.append ("\\)");
			}
			else if (character == '*') {
				result.append ("\\*");
			}
			else if (character == '+') {
				result.append ("\\+");
			}
			else if (character == '-') {
				result.append ("\\-");
			}
			else if (character == '.') {
				result.append ("\\.");
			}
			else if (character == '=') {
				result.append ("\\=");
			}
			else if (character == '@') {
				result.append ("\\@");
			}
			else if (character == '[') {
				result.append ("\\[");
			}
			else if (character == ']') {
				result.append ("\\]");
			}
			else if (character == '^') {
				result.append ("\\^");
			}
			else if (character == '_') {
				result.append ("\\_");
			}
			else if (character == '{') {
				result.append ("\\{");
			}
			else if (character == '}') {
				result.append ("\\}");
			}
			else if (character == '~') {
				result.append ("\\~");
			}
			else {

				result.append (character);
			}
			character = iterator.next ();
		}
		return result.toString ();
	}

	// private String escapeSpecialCharacters (String input) {
	//
	// char[] specialChars = "~!@#$%^&*()-_=+[]{}.".toCharArray ();
	// final StringBuilder result = new StringBuilder ();
	// final StringCharacterIterator iterator = new StringCharacterIterator
	// (input);
	// char character = iterator.current ();
	//
	// while (character != CharacterIterator.DONE) {
	// for (int i = 0; i < specialChars.length; i++) {
	//
	// if (character == specialChars[i]) {
	//
	// result.append ("\\" + character);
	// character = iterator.next ();
	// break;
	// }
	// }
	//
	// result.append (character);
	// character = iterator.next ();
	// }
	// Log.e ("result.toString ()", result.toString ());
	// return result.toString ();
	// }
}
