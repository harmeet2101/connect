package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ali.mehmood on 6/29/2015.
 */
public class Address implements Parcelable {

	private String streetOne;
	private String streetTwo;
	private String zip;
	private String state;
	private String city;
	private String name;
	private double latitude;
	private double longitude;

	public Address () {

	}

	public String getStreetOne () {

		return streetOne;
	}

	public void setStreetOne (String streetOne) {

		this.streetOne = streetOne;
	}

	public String getStreetTwo () {

		return streetTwo;
	}

	public void setStreetTwo (String streetTwo) {

		this.streetTwo = streetTwo;
	}

	public String getZip () {

		return zip;
	}

	public void setZip (String zip) {

		this.zip = zip;
	}

	public String getState () {

		return state;
	}

	public void setState (String state) {

		this.state = state;
	}

	public String getCity () {

		return city;
	}

	public void setCity (String city) {

		this.city = city;
	}

	public String getName () {

		return name;
	}

	public void setName (String name) {

		this.name = name;
	}

	public String getFormattedAddress () {

		return String.format ("%s, %s, %s, %s", getStreetOne (), getStreetTwo (), getCity (), getState ());
	}

	public String getShortFormattedAddress () {

		return String.format ("%s, %s", getCity (), getState ());
	}

	public void setCoordinates (LatLng coordinates) {

		this.latitude = coordinates.latitude;
		this.longitude = coordinates.longitude;
	}

	public LatLng getCoordinates () {

		return new LatLng (latitude, longitude);
	}

	protected Address (Parcel in) {

		streetOne = in.readString ();
		streetTwo = in.readString ();
		zip = in.readString ();
		state = in.readString ();
		city = in.readString ();
		name = in.readString ();
		latitude = in.readDouble ();
		longitude = in.readDouble ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (streetOne);
		dest.writeString (streetTwo);
		dest.writeString (zip);
		dest.writeString (state);
		dest.writeString (city);
		dest.writeString (name);
		dest.writeDouble (latitude);
		dest.writeDouble (longitude);
	}

	public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address> () {
		                                                        @Override
		                                                        public Address createFromParcel (Parcel in) {

			                                                        return new Address (in);
		                                                        }

		                                                        @Override
		                                                        public Address[] newArray (int size) {

			                                                        return new Address[size];
		                                                        }
	                                                        };
}