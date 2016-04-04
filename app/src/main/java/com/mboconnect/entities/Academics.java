package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ali.mehmood on 8/11/2015.
 */
public class Academics implements Parcelable {

	private String field;
	private String institute;
	private String year;
	private String degreeType;

	public Academics () {

	}

	protected Academics (Parcel in) {
		field = in.readString ();
		institute = in.readString ();
		year = in.readString ();
		degreeType = in.readString ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	public String getField () {

		return field;
	}

	public void setField (String field) {

		this.field = field;
	}

	public String getInstitute () {

		return institute;
	}

	public void setInstitute (String institute) {

		this.institute = institute;
	}

	public String getYear () {

		return year;
	}

	public void setYear (String year) {

		this.year = year;
	}

	public String getDegreeType () {

		return degreeType;
	}

	public void setDegreeType (String degreeType) {

		this.degreeType = degreeType;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (field);
		dest.writeString (institute);
		dest.writeString (year);
		dest.writeString (degreeType);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Academics> CREATOR = new Parcelable.Creator<Academics> () {
		@Override
		public Academics createFromParcel (Parcel in) {

			return new Academics (in);
		}

		@Override
		public Academics[] newArray (int size) {

			return new Academics[size];
		}
	};
}