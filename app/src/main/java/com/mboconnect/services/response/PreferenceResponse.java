package com.mboconnect.services.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.entities.Preference;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class PreferenceResponse extends BaseResponse<Preference> {

	@Override
	public void set (String input) {

		try {
			JsonObject jsonObject = JsonUtility.parseToJsonObject (input);
			JsonObject jsonSearchObject = JsonUtility.getJsonObject (jsonObject, KeyConstants.KEY_SEARCH);
			JsonArray jsonArray = JsonUtility.getJsonArray (jsonSearchObject, KeyConstants.KEY_ATTRIBUTES);

			for (int i = 0; i < jsonArray.size (); i++) {

				Preference preference = new Preference ();
				preference.set (jsonArray.get (i).toString ());
				list.add (preference);
			}
		}
		catch (Exception e) {

		}
	}

}
