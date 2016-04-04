package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by ali.mehmood on 7/21/2015.
 */
public class GoogleDirection extends BaseEntity implements Parcelable {

	String duration;

	public GoogleDirection () {

	}

	public GoogleDirection (Parcel in) {

		duration = in.readString ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void set (String jsonString) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();
			JsonArray routesArray = jsonObject.getAsJsonArray (KeyConstants.KEY_ROUTES);

			if (routesArray.size () > 0) {

				JsonArray legs = JsonUtility.getJsonArray (routesArray.get (0).getAsJsonObject (), KeyConstants.KEY_LEGS);

				if (legs.size () > 0) {

					JsonObject durationObj = JsonUtility.getJsonObject (legs.get (0).getAsJsonObject (), KeyConstants.KEY_DURATION);
					duration = JsonUtility.getString (durationObj, "text");
				}
			}
		}
		catch (Exception e) {

			e.printStackTrace ();
		}
	}

	public String getDuration () {

		return duration;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (duration);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<GoogleDirection> CREATOR = new Parcelable.Creator<GoogleDirection> () {
		                                                                @Override
		                                                                public GoogleDirection createFromParcel (Parcel in) {

			                                                                return new GoogleDirection (in);
		                                                                }

		                                                                @Override
		                                                                public GoogleDirection[] newArray (int size) {

			                                                                return new GoogleDirection[size];
		                                                                }
	                                                                };

}
