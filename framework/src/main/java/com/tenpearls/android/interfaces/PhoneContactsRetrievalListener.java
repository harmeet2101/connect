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

import java.util.ArrayList;

import com.tenpearls.android.entities.PhoneContact;
import com.tenpearls.android.utilities.PhoneContactsUtility;

/**
 * Listener interface to be used in conjunction with
 * {@link PhoneContactsUtility}
 * 
 * @author 10Pearls
 * 
 */
public interface PhoneContactsRetrievalListener {

	/**
	 * Called when the contacts fetching process has been completed.
	 * 
	 * @param contactList The list of contacts.
	 */
	public void onPhoneContactsRetrieved (ArrayList<PhoneContact> contactList);

	/**
	 * Called when there was some problem while fetching the contacts.
	 * 
	 * @param exception The exception generated.
	 */
	public void onPhoneContactsRetrievalFailure (Exception exception);
}
