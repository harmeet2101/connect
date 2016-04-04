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
package com.tenpearls.android.utilities;

import java.util.ArrayList;

import com.tenpearls.android.entities.PhoneContact;
import com.tenpearls.android.interfaces.PhoneContactsRetrievalListener;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;

/**
 * Provides the functionality fetching contacts stored on phone. It fetches
 * contacts using a {@link ContentResolver}, encapsulated in an
 * {@link AsyncTask}. Therefore, it is essential for the developer to implement
 * {@link PhoneContactsRetrievalListener} in their code to get the contacts list
 * fetched using this class.
 * 
 * @author 10Pearls
 * 
 */
public class PhoneContactsUtility {

	private final static String[] PROJECTION = { RawContacts._ID, Contacts.DISPLAY_NAME, Email.DATA };

	/**
	 * A private method to fetch the contacts with email
	 * 
	 * @param selection A filter declaring which rows to return, formatted as an
	 *            SQL WHERE clause (excluding the WHERE itself). Passing null
	 *            will return all rows for the given URI. Use the 'column name'
	 *            methods of this class to construct the clause.
	 * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
	 *            clause (excluding the ORDER BY itself). Passing null will use
	 *            the default sort order, which may be unordered. Use the
	 *            'column name' methods of this class to construct the clause.
	 * @param context A valid context object to get the Content Resolver.
	 * 
	 * @return An array list of PhoneContact objects.
	 */

	private static ArrayList<PhoneContact> getContactsList (String selection, String sortOrder, Context context) {

		ArrayList<PhoneContact> contactsList = new ArrayList<PhoneContact> ();

		Cursor cursor = context.getContentResolver ().query (Email.CONTENT_URI, PROJECTION, null, null, null);

		if (cursor != null && cursor.getCount () > 0) {
			while (cursor.moveToNext ()) {

				String id = cursor.getString (cursor.getColumnIndex (RawContacts._ID));
				String name = cursor.getString (cursor.getColumnIndex (Contacts.DISPLAY_NAME));
				String email = cursor.getString (cursor.getColumnIndex (Email.DATA));

				PhoneContact phoneContact = new PhoneContact ();
				phoneContact.setId (id);
				phoneContact.setName (name);
				phoneContact.setEmail (email);

				contactsList.add (phoneContact);
			}

			cursor.close ();
		}

		return contactsList;
	}

	/**
	 * Dispatches the query to fetch contacts using {@link ContentResolver}.
	 * Fires the listener method {@code onContactsRetrieved} of
	 * {@link PhoneContactsRetrievalListener}.<br />
	 * <br />
	 * 
	 * NOTE: You need to have the following permission set in the
	 * AndroidManifest.xml file:<br />
	 * <br />
	 * {@code 
	 * <uses-permission android:name="android.permission.READ_CONTACTS" />
	 * }
	 * 
	 * @param selection A filter declaring which rows to return, formatted as an
	 *            SQL WHERE clause (excluding the WHERE itself). Passing null
	 *            will return all rows for the given URI. Use the 'column name'
	 *            methods of this class to construct the clause.
	 * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
	 *            clause (excluding the ORDER BY itself). Passing null will use
	 *            the default sort order, which may be unordered. Use the
	 *            'column name' methods of this class to construct the clause.
	 * @param context A valid context
	 * @param contactsRetrievalListener Object which implements
	 *            {@link PhoneContactsRetrievalListener}.
	 */
	public static void fetchContacts (final String selection, final String sortOrder, final Context context, final PhoneContactsRetrievalListener contactsRetrievalListener) {

		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object> () {

			@Override
			protected Object doInBackground (Object... params) {

				try {
					return getContactsList (selection, sortOrder, context);
				}
				catch (Exception exception) {
					return exception;
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute (Object result) {

				if (contactsRetrievalListener == null)
					return;

				if (result instanceof Exception) {
					Exception exception = (Exception) result;
					contactsRetrievalListener.onPhoneContactsRetrievalFailure (exception);
				}
				else {
					contactsRetrievalListener.onPhoneContactsRetrieved ((ArrayList<PhoneContact>) result);
				}
			}
		};

		asyncTask.execute ();
	}

	/**
	 * This method returns the Photo URI of a contact
	 * 
	 * @param id The ID of the Contact. This is the same value as returned by
	 *            PhoneContact 'getId' method.
	 * @param context A valid Context object to get the Content Resolver
	 * 
	 * @return The Photo URI
	 */

	public static Uri getPhotoUriFromID (String id, Context context) {

		try {
			Cursor cur = context.getContentResolver ().query (ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=" + id + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null, null);
			if (cur != null) {
				if (!cur.moveToFirst ()) {
					return null;
				}
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace ();
			return null;
		}
		Uri person = ContentUris.withAppendedId (Contacts.CONTENT_URI, Long.parseLong (id));
		return Uri.withAppendedPath (person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}

	/**
	 * This method should be used to construct the Selection and Sort Order
	 * clauses for fetchContactsList method.
	 * 
	 * @return The Column Name of the Contacts ID
	 */

	public static String getContactsIDColumnName () {

		return RawContacts._ID;
	}

	/**
	 * This method should be used to construct the Selection and Sort Order
	 * clauses for fetchContactsList method.
	 * 
	 * @return The Column Name of the Contact Display Name
	 */

	public static String getContactsDisplayNameColumnName () {

		return Contacts.DISPLAY_NAME;
	}

	/**
	 * This method should be used to construct the Selection and Sort Order
	 * clauses for fetchContactsList method.
	 * 
	 * @return The Column Name of the Email Address
	 */

	public static String getContactsEmailColumnName () {

		return Email.DATA;
	}

}