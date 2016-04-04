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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class which contains method for manipulating and conversion of dates.
 * 
 * @author 10Pearls
 * 
 */
public class DateAndTimeUtility {

	/**
	 * Converts the provided date in local time zone into its equivalent UTC
	 * String representation.
	 * 
	 * @param dateString String representation of the date that needs to get
	 *            converted
	 * @param dateFormat Format of the provided date
	 * @return UTC date
	 * @throws Exception the exception
	 */

	public static String convertToUTC (String dateString, String dateFormat) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		formatter.setTimeZone (getUTCTimeZone ());
		Date value = formatter.parse (dateString);
		SimpleDateFormat dateFormatter = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		dateFormatter.setTimeZone (TimeZone.getDefault ());
		String dt = dateFormatter.format (value);

		return dt;
	}

	/**
	 * Converts a date in local time zone in string format into its appropriate
	 * UTC time in {@link Date} representation.
	 * 
	 * @param dateString String representation of the date that needs to get
	 *            converted
	 * @param dateFormat Format of the provided date
	 * @return UTC date
	 * @throws ParseException the parse exception
	 */
	public static Date getUTCDateFromString (String dateString, String dateFormat) throws java.text.ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		sdf.setTimeZone (getUTCTimeZone ());
		return sdf.parse (dateString);
	}

	/**
	 * Converts a UTC date in string format into its appropriate Local time zone
	 * in {@link Date} representation.
	 * 
	 * @param dateString String representation of the date that needs to get
	 *            converted
	 * @param dateFormat Format of the provided date
	 * @return Local time zone date
	 * @throws ParseException the parse exception
	 */
	public static Date getLocalDateFromString (String dateString, String dateFormat) throws java.text.ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		sdf.setTimeZone (TimeZone.getDefault ());
		return sdf.parse (dateString);
	}

	/**
	 * Converts UTC date in string format into its local time zone
	 * representation in string format.
	 * 
	 * @param dateString String representation of the date that needs to get
	 *            converted
	 * @param dateFormat Format of the provided date
	 * @return Local time zone date
	 * @throws Exception the exception
	 */
	public static String convertToLocal (String dateString, String dateFormat) throws Exception {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		simpleDateFormat.setTimeZone (getUTCTimeZone ());
		Date myDate = simpleDateFormat.parse (dateString);

		return myDate.toString ();
	}

	/**
	 * Converts time stamp into UTC representation.
	 * 
	 * @param milliSeconds UNIX time stamp
	 * @param dateFormat Required format for the converted date
	 * @return UTC date
	 */
	public static String convertMillisToUTCTime (long milliSeconds, String dateFormat) {

		SimpleDateFormat dateFormatter = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		dateFormatter.setTimeZone (getUTCTimeZone ());
		String dt = dateFormatter.format (new Date (milliSeconds));

		return dt;
	}

	/**
	 * Converts time stamp into Local time zone representation.
	 * 
	 * @param milliSeconds UNIX time stamp
	 * @param dateFormat Required format for the converted date
	 * @return Local time zone date
	 */
	public static String convertMillisToLocalTime (long milliSeconds, String dateFormat) {

		SimpleDateFormat dateFormatter = new SimpleDateFormat (dateFormat, Locale.getDefault ());
		dateFormatter.setTimeZone (TimeZone.getDefault ());
		String dt = dateFormatter.format (new Date (milliSeconds));

		return dt;
	}

	/**
	 * Returns the number of days between the provided date and today's date.
	 * 
	 * @param date The {@link Date} from which difference needs to be calculated
	 * @return Difference in days. If provided date has not come yet, this
	 *         method will return -ve value.
	 */
	public static int getDaysDifferenceFromToday (Date date) {

		Calendar calendar = Calendar.getInstance ();
		calendar.setTime (date);
		int dayOfYearForDate = calendar.get (Calendar.DAY_OF_YEAR);

		calendar.setTime (new Date (System.currentTimeMillis ()));
		int dayOfYearForToday = calendar.get (Calendar.DAY_OF_YEAR);

		return dayOfYearForToday - dayOfYearForDate;
	}

	/**
	 * Gets the uTC time zone.
	 * 
	 * @return the uTC time zone
	 */
	public static TimeZone getUTCTimeZone () {

		return TimeZone.getTimeZone ("UTC");
	}
}
