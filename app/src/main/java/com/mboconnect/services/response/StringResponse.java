package com.mboconnect.services.response;

import com.mboconnect.constants.KeyConstants;
import com.google.gson.JsonObject;
import com.tenpearls.android.utilities.JsonUtility;

public class StringResponse<T> extends BaseResponse<T> {

	public StringResponse (String key) {

		this.key = key;
	}

	@Override
	public void set (String response) {

		if (this.key == null)
			return;

		JsonObject jsonObject = JsonUtility.parseToJsonObject (response);
		JsonObject results = JsonUtility.getJsonObject (jsonObject, KeyConstants.KEY_RESULT);

		if (this.key.equals (KeyConstants.KEY_RESULT))
			this.value = results.toString ();
		else
			this.value = JsonUtility.getString (results, this.key);
	}

}
