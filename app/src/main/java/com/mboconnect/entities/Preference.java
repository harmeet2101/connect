package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by tahir on 09/07/15.
 */
public class Preference extends BaseEntity implements Parcelable {

	String type;

	String value;

	public Preference () {

	}

	public Preference (Preference preference) {

		this.value = preference.value;
		this.type = preference.type;

	}

	public Preference (String value, String type) {

		this.value = value;
		this.type = type;

	}

	@Override
	public void set (String jsonString) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();

			this.type = JsonUtility.getString (jsonObject, KeyConstants.KEY_TYPE);
			this.value = JsonUtility.getString (jsonObject, KeyConstants.KEY_VALUE);

		}
		catch (Exception e) {

		}
	}

	public String getValue () {

		return value;
	}

	public void setValue (String value) {

		this.value = value;
	}

	public String getType () {

		return type;
	}

	public void setType (String type) {

		this.type = type;
	}

	protected Preference (Parcel in) {
		type = in.readString ();
		value = in.readString ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (type);
		dest.writeString (value);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Preference> CREATOR = new Parcelable.Creator<Preference> () {
		@Override
		public Preference createFromParcel (Parcel in) {

			return new Preference (in);
		}

		@Override
		public Preference[] newArray (int size) {

			return new Preference[size];
		}
	};
}