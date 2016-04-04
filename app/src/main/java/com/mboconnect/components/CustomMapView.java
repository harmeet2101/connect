package com.mboconnect.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mboconnect.R;


/**
 * Created by ali.mehmood on 6/3/2015.
 */
public class CustomMapView extends LinearLayout {

	GoogleMap googleMap;

	public CustomMapView(Context context) {

		super (context);
		initUI ();
	}

	public CustomMapView(Context context, AttributeSet attrs) {

		super (context, attrs);
		initUI ();
	}

	public CustomMapView(Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
		initUI ();
	}

	private void initUI () {

		LayoutInflater layoutInflator = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View rootView = layoutInflator.inflate (R.layout.view_map, null);
		createMapView (rootView);

		this.addView (rootView);
	}

	private void createMapView (View rootView) {

		/**
		 * Catch the null pointer exception that may be thrown when initialising
		 * the map
		 */
		try {
			if (null == googleMap) {

				googleMap = ((MapFragment) ((Activity) getContext ()).getFragmentManager ().findFragmentById (R.id.mapFragment)).getMap ();

			}
		}
		catch (NullPointerException exception) {
		}
	}
}