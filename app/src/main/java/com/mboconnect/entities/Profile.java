package com.mboconnect.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.R;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ali.mehmood on 6/25/2015.
 */
public class Profile extends BaseEntity implements Parcelable {

	private String				  userId;
	private String				  firstName;
	private String				  lastName;
	private String				  designation;
	private String				  imageURL;
	private String				  email;
	private String				  phone;
	private String				  mobile;
	private String				  summary;
	private String				  skillsJson;
	private String				  resume;
	private ArrayList<String>	  skills;
	private ArrayList<Academics>  academics;
	private String				  academicsJson;
	private byte[]				  imageData;
	private ArrayList<Preference> preferences;
	private String				  preferenceJson;
	private String				  preferredLocation;
	public String 				  userRole;

	public Profile () {

	}

	@Override
	public void set (String jsonString) {

		JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject();
		JsonObject nameObject = jsonObject.getAsJsonObject (KeyConstants.KEY_NAME);
		JsonObject contactObject = jsonObject.getAsJsonObject (KeyConstants.KEY_CONTACT);
		JsonObject profile = jsonObject.getAsJsonObject (KeyConstants.KEY_PROFESSIONAL_PROFILE);
		JsonObject preferredLocationObj = jsonObject.getAsJsonObject (KeyConstants.KEY_PREFERRED_LOCATION);

		JsonArray academics = null;
		if (jsonObject.has (KeyConstants.KEY_ACADEMIC_PROFILE)) {

			academics = jsonObject.getAsJsonArray (KeyConstants.KEY_ACADEMIC_PROFILE);
		}

		userId = JsonUtility.getString (jsonObject, KeyConstants.KEY_ID);
		firstName = JsonUtility.getString (nameObject, KeyConstants.KEY_FIRST);
		lastName = JsonUtility.getString(nameObject, KeyConstants.KEY_LAST);
		JsonArray userRoleArray =JsonUtility.getJsonArray(jsonObject,KeyConstants.KEY_ROLE);
		userRole=userRoleArray.get(0).getAsString();
		imageURL = ServiceConstants.SERVICE_IMAGE + JsonUtility.getString (jsonObject, KeyConstants.KEY_IMAGE_URL);
		imageURL=imageURL.replace("/v1","");
		designation = JsonUtility.getString (contactObject, KeyConstants.KEY_TITLE);
		email = JsonUtility.getString (JsonUtility.getJsonObject (contactObject, KeyConstants.KEY_EMAIL), KeyConstants.KEY_ADDRESS);
		JsonArray phoneArray = JsonUtility.getJsonArray (contactObject, KeyConstants.KEY_PHONE);
		if (phoneArray != null && phoneArray.size () > 0) {

			phone = JsonUtility.getString (phoneArray.get (0).getAsJsonObject (), KeyConstants.KEY_NUMBER);

			if (phoneArray.size () > 1) {

				mobile = JsonUtility.getString (phoneArray.get (1).getAsJsonObject (), KeyConstants.KEY_NUMBER);
			}
		}


		summary = JsonUtility.getString (jsonObject, KeyConstants.KEY_SUMMARY);
		resume = JsonUtility.getString (jsonObject, KeyConstants.KEY_RESUME);

		JsonArray skillArray=null;
		if(profile != null){

			skillArray = profile.getAsJsonArray (KeyConstants.KEY_SKILLS);
		}

		if (skillArray != null && skillArray.size () != 0) {

			skillsJson = skillArray.toString ();
		}

		if (academics != null && academics.size () > 0) {

			academicsJson = academics.toString ();
		}

		if (preferredLocationObj != null) {

			JsonObject addressObj = JsonUtility.getJsonObject (preferredLocationObj, KeyConstants.KEY_ADDRESS);

			if (addressObj != null) {

				preferredLocation = JsonUtility.getString (addressObj, KeyConstants.KEY_CITY);
			}
		}

		JsonObject prefObj = jsonObject.getAsJsonObject (KeyConstants.KEY_PREFERENCES);
		if (prefObj != null) {

			preferenceJson = prefObj.toString ();
		}
	}

	public String getGreetings () {

		Calendar cal = Calendar.getInstance ();
		int hours = cal.get (Calendar.HOUR_OF_DAY);
		String msg = "";

		if (hours > 6 && hours < 12) {

			msg = Utils.getString (R.string.good_morning);
		}
		if (hours >= 12 && hours < 17) {

			msg = Utils.getString (R.string.good_afternoon);

		}
		if (hours >= 17 && hours < 22) {

			msg = Utils.getString (R.string.good_evening);
		}
		if (hours >= 22) {

			msg = Utils.getString (R.string.good_night);
		}

		return msg;
	}

	public void setUserId (String userId) {

		this.userId = userId;
	}

	public void setFirstName (String firstName) {

		this.firstName = firstName;
	}

	public void setLastName (String lastName) {

		this.lastName = lastName;
	}

	public void setDesignation (String designation) {

		this.designation = designation;
	}

	public void setImageURL (String imageURL) {

		this.imageURL = imageURL;
	}

	public String getFirstName () {

		return firstName;
	}

	public String getLastName () {

		return lastName;
	}

	public String getDisplayName () {

		return String.format ("%s %s", firstName, lastName);
	}

	public String getDesignation () {

		return designation;
	}

	public String getImageURL () {

		return imageURL;
	}

	public String getUserId () {

		return userId;
	}

	public Profile (Parcel in) {

		userId = in.readString ();
		firstName = in.readString ();
		lastName = in.readString ();
		designation = in.readString ();
		imageURL = in.readString ();
		email = in.readString ();
		phone = in.readString ();
		summary = in.readString ();
		resume = in.readString ();
		if (in.readByte () == 0x01) {

			skills = new ArrayList<String> ();
			in.readList (skills, String.class.getClassLoader ());
		}
		else {

			skills = null;
		}

		if (in.readByte () == 0x01) {
			academics = new ArrayList<Academics> ();
			in.readList (academics, Academics.class.getClassLoader ());
		}
		else {
			academics = null;
		}
		in.readByteArray (imageData);
		mobile = in.readString ();

		if (in.readByte () == 0x01) {

			preferences = new ArrayList<Preference> ();
			in.readList (skills, Preference.class.getClassLoader ());
		}
		else {

			preferences = null;
		}

		preferenceJson = in.readString ();
		preferredLocation = in.readString ();
	}

	@Override
	public int describeContents () {

		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeString (userId);
		dest.writeString (firstName);
		dest.writeString (lastName);
		dest.writeString (designation);
		dest.writeString (email);
		dest.writeString (phone);
		dest.writeString (summary);
		dest.writeString (resume);
		dest.writeString (imageURL);

		if (skills == null) {

			dest.writeByte ((byte) (0x00));
		}
		else {

			dest.writeByte ((byte) (0x01));
			dest.writeList (skills);
		}

		if (academics == null) {
			dest.writeByte ((byte) (0x00));
		}
		else {
			dest.writeByte ((byte) (0x01));
			dest.writeList (academics);
		}

		dest.writeByteArray (imageData);
		dest.writeString (mobile);
		if (preferences == null) {

			dest.writeByte ((byte) (0x00));
		}
		else {

			dest.writeByte ((byte) (0x01));
			dest.writeList (preferences);
		}
		dest.writeString (preferenceJson);
		dest.writeString (preferredLocation);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile> () {
		@Override
		public Profile createFromParcel (Parcel in) {

			return new Profile (in);
		}

		@Override
		public Profile[] newArray (int size) {

			return new Profile[size];
		}
	};

	public String getEmail () {

		return email;
	}

	public void setEmail (String email) {

		this.email = email;
	}

	public String getPhone () {

		return phone;
	}

	public String getMobile () {

		return mobile;
	}

	public void setPhone (String phone) {

		this.phone = phone;
	}

	public void setMobile (String mobile) {

		this.mobile = mobile;
	}

	public String getSummary () {

		return summary;
	}

	public void setSummary (String summary) {

		this.summary = summary;
	}

	public ArrayList<String> getSkills () {

		if (CollectionUtility.isEmptyOrNull (skills) && !StringUtility.isEmptyOrNull (getSkillsJson ())) {

			JsonArray skillJsonArray = JsonUtility.parseToJsonArray (skillsJson);
			skills = new ArrayList<String> ();

			for (int i = 0; i < skillJsonArray.size (); i++) {

				skills.add (skillJsonArray.get (i).getAsString ());
			}
		}
		return skills;
	}

	public void setSkills (ArrayList<String> skills) {

		this.skills = skills;
	}

	public String getSkillsJson () {

		return skillsJson;
	}

	public void setSkillsJson (String skillsJson) {

		this.skillsJson = skillsJson;
	}

	public String getResume () {

		return resume;
	}

	public void setResume (String resume) {

		this.resume = resume;
	}

	public String getAcademicsJson () {

		return academicsJson;
	}

	public void setAcademicsJson (String academicsJson) {

		this.academicsJson = academicsJson;
	}

	public ArrayList<Academics> getAcademics () {

		if (CollectionUtility.isEmptyOrNull (academics) && !StringUtility.isEmptyOrNull (getAcademicsJson ())) {

			JsonArray academicsJsonArray = JsonUtility.parseToJsonArray (academicsJson);
			academics = new ArrayList<Academics> ();

			for (int i = 0; i < academicsJsonArray.size (); i++) {

				Academics academics = new Academics ();
				JsonObject acdObj = academicsJsonArray.get (i).getAsJsonObject ();
				academics.setField (JsonUtility.getString (acdObj, KeyConstants.KEY_FIELD));
				academics.setInstitute (JsonUtility.getString (acdObj, KeyConstants.KEY_INSTITUTE));
				academics.setYear (JsonUtility.getString (acdObj, KeyConstants.KEY_YEAR_ATTENDED));
				academics.setDegreeType (JsonUtility.getString (acdObj, KeyConstants.KEY_DEGREE_TYPE));

				this.academics.add (academics);
			}
		}
		return academics;
	}

	public void setImageData (byte[] imageData) {

		this.imageData = imageData;
	}

	public byte[] getImageData () {

		return imageData;
	}

	public Bitmap getProfileImage () {

		return Utils.getImageFromBytes (imageData);
	}

	public String getPreferenceJson () {

		return preferenceJson;
	}

	public void setPreferenceJson (String preferenceJson) {

		this.preferenceJson = preferenceJson;
	}

	public String getPreferredLocation () {

		return preferredLocation;
	}

	public void setPreferredLocation (String preferredLocation) {

		this.preferredLocation = preferredLocation;
	}

	public ArrayList<Preference> getPreferences () {

		if (CollectionUtility.isEmptyOrNull (preferences) && !StringUtility.isEmptyOrNull (getPreferenceJson ())) {

			preferences = new ArrayList<Preference> ();
			JsonObject jsonObject = JsonUtility.parseToJsonObject (getPreferenceJson ());
			JsonObject jsonSearchObject = JsonUtility.getJsonObject (jsonObject, KeyConstants.KEY_SEARCH);
			JsonArray jsonArray = JsonUtility.getJsonArray (jsonSearchObject, KeyConstants.KEY_ATTRIBUTES);

			for (int i = 0; i < jsonArray.size (); i++) {

				Preference preference = new Preference ();
				preference.set (jsonArray.get (i).toString ());
				preferences.add (preference);
			}
		}
		return preferences;
	}
}