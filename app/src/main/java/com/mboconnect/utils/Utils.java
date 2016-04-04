package com.mboconnect.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.artisan.powerhooks.PowerHookManager;
import com.google.android.gms.maps.model.LatLng;
import com.mboconnect.Application;
import com.mboconnect.R;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.receivers.LoginAlarmReceiver;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static Toast popup;



    public static boolean isAndroidEmulator() {

        String product = Build.PRODUCT;
        boolean isEmulator = false;
        if (product != null) {
            isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_") || product.contains("vbox");
        }
        return isEmulator;
    }

    public static Date getFormattedDate(String input, String inputDateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(inputDateFormat, Locale.getDefault());

        TimeZone timezone = TimeZone.getTimeZone("Etc/UTC");
        formatter.setTimeZone(timezone);

        Date date = null;
        try {
            date = formatter.parse(input);
        } catch (Exception e) {

        }
        return date;

    }

    public static Date getFormattedDate(String input) {

        return Utils.getFormattedDate(input, AppConstants.DATE_Z_FORMAT);
    }

    public static String getStringFromDate(Date date) {

        return getStringFromDate(date, AppConstants.DATE_FORMAT, false);
    }

    public static String getStringFromDate(Date date, String inputDateFormat, boolean isUTC) {

        String output = "--";
        try {
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(inputDateFormat, Locale.getDefault());

                if (isUTC) {
                    TimeZone timezone = TimeZone.getTimeZone("Etc/UTC");
                    formatter.setTimeZone(timezone);
                }

                output = formatter.format(date);
            }
        } catch (Exception e) {
        }
        return output.toUpperCase(Locale.getDefault());
    }

    public static String getFormattedDateStringForLog(String input) {

        SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_Z_FORMAT, Locale.getDefault());

        TimeZone timezone = TimeZone.getTimeZone("Etc/UTC");
        formatter.setTimeZone(timezone);

        Date date = null;
        String output = "--";

        try {
            date = formatter.parse(input);
            if (date != null) {
                formatter = new SimpleDateFormat(AppConstants.LOG_DATE_FORMAT, Locale.getDefault());
                output = formatter.format(date);

            }
        } catch (Exception e) {
            e.getMessage();
        }

        return output.toUpperCase(Locale.getDefault());
    }

    public static void writeBytesToFile(byte[] data) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/telmate/");
        dir.mkdirs();
        File file = new File(dir, UUID.randomUUID().toString() + ".txt");
        FileOutputStream stream = null;
        try {

            stream = new FileOutputStream(file);
            stream.write(data);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("resource")
    public static byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);

        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        byte[] bytes_data = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes_data.length && (numRead = is.read(bytes_data, offset, bytes_data.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes_data.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes_data;
    }

    public static boolean isEmailValid(String email) {

        String expression = "^[\\w\\.+-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static String getTimezoneOffset() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        SimpleDateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        return localTime;

    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }

    public static void showSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);

    }

    public static String getFormattedDateFromMilis(long milliSeconds, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getString(int id) {

        return Application.getInstance().getString(id);
    }

    public static long daysAgo(long milisecs) {

        DateTime startDateTime = new DateTime(milisecs, DateTimeZone.UTC);
        DateTime endDateTime = new DateTime();

        Duration duration = new Duration(startDateTime, endDateTime);
        return duration.getStandardDays();
    }

    public static long[] hoursAgo(long milisecs) {

        long[] hoursAndMins = new long[3];

        DateTime startDateTime = new DateTime(milisecs, DateTimeZone.UTC);
        DateTime endDateTime = new DateTime();

        Duration duration = new Duration(startDateTime, endDateTime);
        hoursAndMins[0] = duration.getStandardHours(); // Hours
        hoursAndMins[1] = duration.getStandardMinutes(); // Minutes
        hoursAndMins[2] = duration.getStandardSeconds(); // Seconds

        return hoursAndMins;
    }

    public static long getHoursDifference(long milisecs) {

        DateTime startDateTime = new DateTime(milisecs, DateTimeZone.UTC);
        DateTime endDateTime = new DateTime();

        Duration duration = new Duration(startDateTime, endDateTime);
        return duration.getStandardHours();
    }

    public static String[] getStringParsedBySpace(String value) {

        String[] splited = value.split("\\s+");
        return splited;

    }

    public static String getName(String value) {

        String[] arrayName = getStringParsedBySpace(value);

        String name = "";
        for (int i = 0; i < arrayName.length; i++) {

            if (i == arrayName.length - 1) {

                String s = arrayName[i];
                name = name + s.charAt(0) + ".";
            } else
                name = name + arrayName[i] + " ";

        }
        return name;

    }

    public static String getLocationAddress(LatLng latLng, Context context) {

        Geocoder geocoder;
        List<Address> addresses;
        String completeAddress = "";

        if (!Utils.isLatLngValid(latLng)) {
            return completeAddress;
        }

        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            completeAddress = address + ", " + city;

        } catch (Exception ex) {

        }

        return filterAddress(completeAddress);
    }

    public static void showInternetConnectionNotFoundMessage() {

        LayoutInflater layoutInflator = (LayoutInflater) Application.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflator.inflate(R.layout.internet_not_found, null);

        if (popup == null) {

            popup = new Toast(Application.getInstance().getApplicationContext());
            popup.setView(view);
            popup.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, DeviceUtility.getPixelsFromDps(55, Application.getInstance()));
            popup.setDuration(Toast.LENGTH_SHORT);
            popup.show();
        }
        if (popup != null && popup.getView().getWindowVisibility() != View.VISIBLE) {

            popup.show();
        }
    }

    public static boolean isLatLngValid(LatLng latLng) {

        if ((latLng == null) || (latLng.latitude == 0.0 && latLng.longitude == 0.0)) {
            return false;
        }

        return true;
    }

    public static String filterAddress(String address) {

        if (!address.contains(",")) {
            return address;
        }

        String[] addressParts = address.split(",");
        String completeAddress = addressParts[0].trim();
        if (!StringUtility.isEmptyOrNull(addressParts[1])) {
            completeAddress = completeAddress + ", " + addressParts[1].trim();
        }
        return completeAddress;
    }

    public static boolean isNumeric(String str) {

        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getVersionName(Activity activity) {

        String versionName = null;

        try {
            versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (Exception ex) {
        }

        return versionName;
    }

    public static int getVersionCode(Activity activity) {

        int versionCode = -1;

        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (Exception ex) {
        }

        return versionCode;
    }

    public static void showAlert(String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener, Context context) {

        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            if (!StringUtility.isEmptyOrNull(title)) {

                alertDialogBuilder.setTitle(title);
            }
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton(positiveButtonText, positiveButtonListener);
            alertDialogBuilder.setNegativeButton(negativeButtonText, negativeButtonListener);
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBrowserIntent(String url, Context context) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }

    public static String decodeFromBase64(String base64) {

        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String text = null;
        try {

            text = new String(data, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return text;
    }

    public void updateConfigurationValuesUsingPowerHooks(){

      //  forgotPassLink=PowerHookManager.getVariableValue("forgotpass");

    }

    public static String updateAuthURL(int i){

        if(i==0){

            return getProdAuthURL();
        }
        return getPreProdAuthURL();
    }


    public static String getForgotPassLink(){

        return PowerHookManager.getVariableValue("forgotpass");
    }

    public static String getProdAPIHostName(){

        return PowerHookManager.getVariableValue("HOOK_PROD_API_HOSTNAME");
    }

    public static String getProProdLogoutURL(){

        return PowerHookManager.getVariableValue("HOOK_PRE_PROD_LOGOUT_URL");
    }

    public static String getProdLogoutURL(){
        return PowerHookManager.getVariableValue("HOOK_PROD_LOGOUT_URL");
    }

    public static String getProdClientID(){
        return PowerHookManager.getVariableValue("HOOK_PROD_CLIENT_ID");
    }

    public static String getPreProdClientID(){
        return PowerHookManager.getVariableValue("HOOK_PRE_PROD_CLIENT_ID");
    }

    public static String getProdAuthURL(){
        return PowerHookManager.getVariableValue("HOOK_PROD_AUTH_URL");
    }

    public static String getPreProdAuthURL(){
        return PowerHookManager.getVariableValue("HOOK_PRE_PROD_AUTH_URL");
    }

    public static String getPreProdAPIHostname(){
        return PowerHookManager.getVariableValue("HOOK_PRE_PROD_API_HOSTNAME");
    }

    public static String getProdAPIHostname(){
        return PowerHookManager.getVariableValue("HOOK_PROD_API_HOSTNAME");
    }

    public static void setLoginAlarm(Context context) {

        Intent intent = new Intent(context, LoginAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, AppConstants.LOGIN_ALARM_REQUEST_CODE, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR, 3);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    public static void cancelLoginAlarm(Context context) {

        Intent intent = new Intent(context, LoginAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, AppConstants.LOGIN_ALARM_REQUEST_CODE, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    public static String formatCurrency(int amount) {

        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) defaultFormat;
        df.applyPattern("###,###.##");
        return defaultFormat.format(amount);
    }

    public static String formatTimeDuration(float time) {

        DecimalFormat decimalformat = new DecimalFormat("#,##.#");
        String myTime = decimalformat.format(time);
        return myTime;
    }

    public static String stripNonDigits(String str) {

        str = str.replaceAll("[^\\d.]", "");
        return str;
    }

    public static void onCallPressed(Context context, String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber.replace(" ", "").trim()));
        context.startActivity(callIntent);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImageFromBytes(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String capitalizeFirstLetter(String input) {

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String getNumberInPlusNotation(int number, int allowedDigits) {

        int length = String.valueOf(number).length();
        String numberString = String.valueOf(number);

        if (length > allowedDigits) {

            numberString = numberString.substring(0, 5) + "+";
        }

        return numberString;
    }

    public static String removeWords(String word, String remove) {

        return word.replace(remove, "");
    }
}