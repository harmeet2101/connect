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
package com.tenpearls.android.interfaces;

import com.tenpearls.android.components.GooglePlacesAutoCompleteTextView;
import com.tenpearls.android.entities.GooglePlace;

/**
 * Listener interface for {@link GooglePlacesAutoCompleteTextView} component.
 * 
 * @author 10Pearls
 * 
 */
public interface GooglePlaceAutoCompleteTextViewListener {

	/**
	 * This method will be invoked when an item is selected from the places list
	 * inside the autocomplete.
	 * 
	 * @param googlePlace The associated {@link GooglePlace} object
	 */
	public void onGooglePlaceSelected (GooglePlace googlePlace);
}
