/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.entities;

import com.google.gson.JsonObject;
import com.tenpearls.android.components.GooglePlacesAutoCompleteTextView;
import com.tenpearls.android.entities.base.BaseWebServiceEntity;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * This class is used to store the objects returned from the Google Places
 * Search API response. Usage in {@link GooglePlacesAutoCompleteTextView}
 * 
 * @author 10Pearls
 * 
 */
public class GooglePlace extends BaseWebServiceEntity {

	/** The id. */
	private String id;

	/** The reference id. */
	private String referenceId;

	/** The description. */
	private String description;

	/** The address. */
	private String address;

	/** The latitude. */
	private double latitude;

	/** The longitude. */
	private double longitude;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId () {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId (String id) {

		this.id = id;
	}

	/**
	 * Gets the reference id.
	 * 
	 * @return the reference id
	 */
	public String getReferenceId () {

		return referenceId;
	}

	/**
	 * Sets the reference id.
	 * 
	 * @param referenceId the new reference id
	 */
	public void setReferenceId (String referenceId) {

		this.referenceId = referenceId;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription () {

		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription (String description) {

		this.description = description;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress () {

		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address the new address
	 */
	public void setAddress (String address) {

		this.address = address;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public double getLatitude () {

		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude the new latitude
	 */
	public void setLatitude (double latitude) {

		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public double getLongitude () {

		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude the new longitude
	 */
	public void setLongitude (double longitude) {

		this.longitude = longitude;
	}

	/**
	 * Overriden method from {@link BaseWebServiceEntity}, uses.
	 * 
	 * @param json the json to deserialize.
	 */
	@Override
	public void deserializeFromJSON (String jsonString) {
		JsonObject jsonObject = JsonUtility.parseToJsonObject(jsonString); 
		this.id = JsonUtility.getString (jsonObject, "id");
		this.referenceId = JsonUtility.getString (jsonObject, "reference");
		this.description = JsonUtility.getString (jsonObject, "description");
	}
	
}
