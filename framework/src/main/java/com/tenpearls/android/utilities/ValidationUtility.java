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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Any reusable validation methods like email, phone number, name, postal code
 * validation should be contained here.
 * 
 * @author 10Pearls
 * 
 */
public class ValidationUtility {

	/**
	 * Checks if the supplied string meets the criteria of a valid email format.
	 * 
	 * @param emailAddress The input email address string
	 * @return {@code true} if email address is valid, {@code false} otherwise
	 */
	public static boolean isValidEmailAddress (String emailAddress) {

		Pattern pattern;
		Matcher matcher;
		String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile (emailPattern);
		matcher = pattern.matcher (emailAddress);
		return matcher.matches ();
	}

	public static boolean isValidPhoneNumber (String phoneNumber) {

		Pattern pattern1 = Pattern.compile ("\\d{3}-\\d{7}");
		Pattern pattern2 = Pattern.compile ("\\d{10}");
		Matcher matcher1 = pattern1.matcher (phoneNumber);
		Matcher matcher2 = pattern2.matcher (phoneNumber);
		
		return (matcher1.matches () || matcher2.matches ());
	}

	/**
	 * Checks if the provided string meets the criteria of a valid name.
	 * 
	 * @param name The input name string
	 * @return {@code true} if name is valid, {@code false} otherwise
	 */
	public static boolean isValidName (String name) {

		return !(StringUtility.isEmptyOrNull (name));
	}
}
