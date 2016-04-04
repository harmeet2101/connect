package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.mboconnect.constants.ServiceConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ali.mehmood on 6/29/2015.
 */
public class Author implements Parcelable {

	String					name;
	String					designation;
	HashMap<String, String>	phones = new HashMap<String, String> ();
	String					email;
	String					industry;
	Boolean					isVerified;
	Address					address;
	String					authorId;
	String					imageUrl;

	public Author () {

		address = new Address ();
	}

	public Address getAddress () {

		return address;
	}

	public void setAddress (Address address) {

		this.address = address;
	}

	public String getName () {

		return name;
	}

	public void setName (String name) {

		this.name = name;
	}

	public String getAuthorId () {

		return authorId;
	}

	public void setAuthorId (String id) {

		this.authorId = id;
	}

	public String getDesignation () {

		return designation;
	}

	public void setDesignation (String designation) {

		this.designation = designation;
	}

	public HashMap<String, String> getPhone () {

		return phones;
	}

	public String getEmail () {

		return email;
	}

	public void setEmail (String email) {

		this.email = email;
	}

	public String getIndustry () {

		return industry;
	}

	public void setIndustry (String industry) {

		this.industry = industry;
	}

	public Boolean getIsVerified () {

		return isVerified;
	}

	public void setIsVerified (Boolean isVerified) {

		this.isVerified = isVerified;
	}

	public Author (Parcel in) {

		name = in.readString ();
		designation = in.readString ();
		email = in.readString ();
		industry = in.readString ();
		byte isVerifiedVal = in.readByte ();
		isVerified = isVerifiedVal == 0x02 ? null : isVerifiedVal != 0x00;
		address = (Address) in.readValue (Address.class.getClassLoader ());
		authorId = in.readString ();
		imageUrl = in.readString ();
		int size = in.readInt ();
		for (int i = 0; i < size; i++) {
			String key = in.readString ();
			String value = in.readString ();
			phones.put (key, value);
		}
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (name);
		dest.writeString (designation);
		dest.writeString (email);
		dest.writeString (industry);
		if (isVerified == null) {
			dest.writeByte ((byte) (0x02));
		}
		else {
			dest.writeByte ((byte) (isVerified ? 0x01 : 0x00));
		}
		dest.writeValue (address);
		dest.writeString (authorId);
		dest.writeString (imageUrl);
		dest.writeInt (phones.size ());
		for (Map.Entry<String, String> entry : phones.entrySet ()) {
			dest.writeString (entry.getKey ());
			dest.writeString (entry.getValue ());
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author> () {
		@Override
		public Author createFromParcel (Parcel in) {

			return new Author (in);
		}

		@Override
		public Author[] newArray (int size) {

			return new Author[size];
		}
	};

	public String getImageUrl () {

		String completeImageUrl= ServiceConstants.SERVICE_IMAGE + imageUrl;
		completeImageUrl=completeImageUrl.replace("/v1","");
		return completeImageUrl;
	}

	public void setImageUrl (String imageUrl) {

		this.imageUrl = imageUrl;
	}
}