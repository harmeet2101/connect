package com.mboconnect.managers;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class LocationManager {

	static LocationManager locationManager;
	static Location        currentLocation;
	Context                context;

	public boolean isGPSEnabled (Context context) {

		android.location.LocationManager manager = (android.location.LocationManager) context.getSystemService (Context.LOCATION_SERVICE);

		if (manager == null)
			return false;

		return manager.isProviderEnabled (android.location.LocationManager.GPS_PROVIDER);
	}

	public boolean isWiFiLocationEnabled (Context context) {

		android.location.LocationManager manager = (android.location.LocationManager) context.getSystemService (Context.LOCATION_SERVICE);

		if (manager == null)
			return false;

		return manager.isProviderEnabled (android.location.LocationManager.NETWORK_PROVIDER);
	}

	public static LocationManager getInstance (Context context) {

		if (locationManager == null) {
			locationManager = new LocationManager ();
		}

		locationManager.context = context;
		return locationManager;
	}

	public Location getCurrentLocation () {

		android.location.LocationManager manager = (android.location.LocationManager) context.getSystemService (Context.LOCATION_SERVICE);

		Location location = null;

		List<String> providers = manager.getProviders (true);

		for (String provider : providers) {

			location = manager.getLastKnownLocation (provider);
			// maybe try adding some Criteria here

			if (location != null)
				return location;
		}

		return null;
	}

	public void getLocationAddressOnThread (final LatLng latLng, final TextView txtView, final String defaultValue) {

		final Handler handler = new Handler () {
			@Override
			public void handleMessage (Message message) {

				if (txtView == null)
					return;

				txtView.setText (message.obj.toString ());

			}
		};

		Thread thread = new Thread () {
			@Override
			public void run () {

				String address = "";

				Message message = handler.obtainMessage (1, address);
				handler.sendMessage (message);
			}
		};
		thread.start ();
	}

	public static double getDistanceInKm (LatLng firstPoint, LatLng secondPoint) {

		return distance (firstPoint.latitude, firstPoint.longitude, secondPoint.latitude, secondPoint.longitude, 'K');
	}

	public static double getDistanceInMiles (LatLng firstPoint, LatLng secondPoint) {

		return distance (firstPoint.latitude, firstPoint.longitude, secondPoint.latitude, secondPoint.longitude, 'M');
	}

	public static double distance (double lat1, double lon1, double lat2, double lon2, char unit) {

		double theta = lon1 - lon2;

		double dist = Math.sin (deg2rad (lat1)) * Math.sin (deg2rad (lat2)) + Math.cos (deg2rad (lat1)) * Math.cos (deg2rad (lat2)) * Math.cos (deg2rad (theta));
		dist = Math.acos (dist);
		dist = rad2deg (dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {

			dist = dist * 1.609344;
		}
		else if (unit == 'N') {

			dist = dist * 0.8684;
		}

		return (dist);
	}

	private static double rad2deg (double rad) {

		return (rad * 180 / Math.PI);
	}

	private static double deg2rad (double deg) {

		return (deg * Math.PI / 180.0);
	}

	public static float getDistance (LatLng firstPoint, LatLng secondPoint) {

		double earthRadius = 3958.75;
		int meterConversion = 1609;

		double lat1 = firstPoint.latitude;
		double lat2 = secondPoint.latitude;
		double lon1 = firstPoint.longitude;
		double lon2 = secondPoint.longitude;
		double dLat = Math.toRadians (lat2 - lat1);
		double dLon = Math.toRadians (lon2 - lon1);
		double a = Math.sin (dLat / 2) * Math.sin (dLat / 2) + Math.cos (Math.toRadians (lat1)) * Math.cos (Math.toRadians (lat2)) * Math.sin (dLon / 2) * Math.sin (dLon / 2);
		double c = 2 * Math.asin (Math.sqrt (a));
		double dist = earthRadius * c;

		return Float.valueOf ((float) (dist * meterConversion));
	}
}
