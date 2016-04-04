package com.mboconnect.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.mboconnect.R;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;

/**
 * Created by ali.mehmood on 6/12/2015.
 */
public class AcademicProfileView extends RelativeLayout {

	private TextView txtInstitue,
	        txtDegree, txtYearAttended;

	public AcademicProfileView (Context context) {

		super (context);
		initUI ();
	}

	public AcademicProfileView (Context context, AttributeSet attrs) {

		super (context, attrs);
		initUI ();
	}

	public AcademicProfileView (Context context, AttributeSet attrs, int defStyle) {

		super (context, attrs, defStyle);
		initUI ();
	}

	private void initUI () {

		LayoutInflater layoutInflater = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate (R.layout.view_academic_profile, null);

		txtInstitue = (TextView) view.findViewById (R.id.txtInstitue);
		txtDegree = (TextView) view.findViewById (R.id.txtDegree);
		txtYearAttended = (TextView) view.findViewById (R.id.txtYearAttended);

		this.addView (view);
	}

	public void populateData (String institute, String degree, String year) {

		txtInstitue.setText (institute);
		txtDegree.setText (degree);
		txtYearAttended.setText (year);
	}
}
