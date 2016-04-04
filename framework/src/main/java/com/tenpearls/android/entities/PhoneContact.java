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

import com.tenpearls.android.interfaces.PhoneContactsRetrievalListener;
import com.tenpearls.android.utilities.PhoneContactsUtility;

/**
 * Entity to store information of a phone contact. Used in
 * {@link PhoneContactsUtility} and {@link PhoneContactsRetrievalListener}.
 * 
 * @author 10Pearls
 * 
 */
public class PhoneContact {

	/** The id. */
	String id;
	
	/** The name. */
	String name;
	
	/** The email. */
	String email;
	
	/** The photo uri. */
	String photoUri;

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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName () {

		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName (String name) {

		this.name = name;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail () {

		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail (String email) {

		this.email = email;
	}
	
	/**
	 * Gets the photo uri.
	 *
	 * @return the photo uri
	 */
	public String getPhotoUri () {

		return photoUri;
	}

	/**
	 * Sets the photo uri.
	 *
	 * @param photoUri the new photo uri
	 */
	public void setPhotoUri (String photoUri) {

		this.photoUri = photoUri;
	}
}
