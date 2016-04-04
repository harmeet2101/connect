package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.mboconnect.constants.KeyConstants;
import com.tenpearls.android.utilities.JsonUtility;

public class AccessToken extends BaseEntity implements Parcelable {

	private String accessToken;
	private String refreshToken;
	private long   expiryTime;
	private long   refreshTokenExpiryTime;

	public AccessToken () {

	}

	@Override
	public void set (String jsonString) {

		JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();

		accessToken = JsonUtility.getString (jsonObject, KeyConstants.KEY_ACCESS_TOKEN);
		refreshToken = JsonUtility.getString (jsonObject, KeyConstants.KEY_REFRESH_TOKEN);
		expiryTime = JsonUtility.getLong (jsonObject, KeyConstants.KEY_ACCESS_TOKEN_EXPIRY);
		refreshTokenExpiryTime = JsonUtility.getLong (jsonObject, KeyConstants.KEY_REFRESH_TOKEN_EXPIRY);
	}

	public String getAccessToken () {

		return accessToken;
	}

	public void setAccessToken (String accessToken) {

		this.accessToken = accessToken;
	}

	public String getRefreshToken () {

		return refreshToken;
	}

	public void setRefreshToken (String refreshToken) {

		this.refreshToken = refreshToken;
	}

	public long getExpiryTime () {

		return expiryTime;
	}

	public void setExpiryTime (long expiryTime) {

		this.expiryTime = expiryTime;
	}

	protected AccessToken (Parcel in) {
		accessToken = in.readString ();
		refreshToken = in.readString ();
		expiryTime = in.readLong ();
		refreshTokenExpiryTime = in.readLong ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (accessToken);
		dest.writeString (refreshToken);
		dest.writeLong (expiryTime);
		dest.writeLong (refreshTokenExpiryTime);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<AccessToken> CREATOR = new Parcelable.Creator<AccessToken> () {
		@Override
		public AccessToken createFromParcel (Parcel in) {

			return new AccessToken (in);
		}

		@Override
		public AccessToken[] newArray (int size) {

			return new AccessToken[size];
		}
	};
}