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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tenpearls.android.R;

/**
 * Encapsulates methods for UI widgets like {@link Toast}, {@link AlertDialog}
 * etc.
 * 
 * @author 10Pearls
 * 
 */
public class UIUtility {

	/**
	 * Displays a standard Toast for a small duration, contains just a text
	 * view.
	 * 
	 * @param message The message to show
	 * @param context A valid context
	 */
	public static void showShortToast (String message, Context context) {

		try {
			Toast.makeText (context, message, Toast.LENGTH_SHORT).show ();
		}
		catch (Exception e) {

			e.printStackTrace ();
		}
	}

	/**
	 * Displays a standard Toast for a long duration, contains just a text view.
	 * 
	 * @param message The message to show
	 * @param context A valid context
	 */
	public static void showLongToast (String message, Context context) {

		Toast.makeText (context, message, Toast.LENGTH_LONG).show ();
	}

	/**
	 * Displays an Alert dialog with a primary button.
	 * 
	 * @param title Title of the dialog
	 * @param message Descriptive message for the dialog
	 * @param positiveButtonText Button title
	 * @param context A valid context
	 */
	public static void showAlert (String title, String message, String positiveButtonText, Context context) {

		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (context);
			alertDialogBuilder.setTitle (title);
			alertDialogBuilder.setMessage (message);
			alertDialogBuilder.setPositiveButton (positiveButtonText, null);

			alertDialogBuilder.show ();
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

	public static void showAlert (String title, String message, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener, Context context) {

		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (context);
			if (!StringUtility.isEmptyOrNull (title)) {

				alertDialogBuilder.setTitle (title);
			}
			alertDialogBuilder.setMessage (message);
			alertDialogBuilder.setPositiveButton (context.getString (R.string.ok), positiveButtonListener);
			alertDialogBuilder.setNegativeButton (context.getString (R.string.cancel_caps), negativeButtonListener);
			alertDialogBuilder.show ();
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

	/**
	 * Hides the soft keyboard from the phone's screen.
	 * 
	 * @param editText A valid reference to any EditText, currently in the view
	 *            hierarchy
	 * @param context A valid context
	 */
	public static void hideSoftKeyboard (EditText editText, Context context) {

		InputMethodManager imm = (InputMethodManager) context.getSystemService (Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow (editText.getWindowToken (), 0);
	}
}
