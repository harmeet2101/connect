package com.mboconnect.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.mboconnect.R;
import com.mboconnect.components.AcademicProfileView;
import com.mboconnect.components.TabbedViewPager;
import com.mboconnect.entities.Academics;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Profile;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;

import java.util.ArrayList;

/**
 * Created by vishal.chhatwani on 8/6/2015.
 */
public class ProfileAdapter extends PagerAdapter {

	Context			  context;
	Profile			  profile;
	ArrayList<String> tabstitles = new ArrayList<> ();

	public ProfileAdapter (Context context, Profile profile, ArrayList<String> tabsTitles) {

		this.context = context;
		this.profile = profile;
		this.tabstitles = tabsTitles;

	}

	public Object getItem (int position) {

		return tabstitles.get (position);
	}

	@Override
	public int getItemPosition (Object object) {

		return 0;
	}

	@Override
	public int getCount () {

		return 4;
	}

	@Override
	public boolean isViewFromObject (View arg0, Object arg1) {

		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem (ViewGroup container, int position) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate (R.layout.item_tab_layout, null);
		ScrollView scrlContainer = (ScrollView) view.findViewById (R.id.scrlContainer);

		View v = null;
		if (position == 0) {

			v = new TextView (context);
			v.setPadding (DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context));
			((TextView) v).setText(profile.getSummary());
		}
		else if (position == 1 && !CollectionUtility.isEmptyOrNull (profile.getAcademics ())) {

			v = new LinearLayout (context);
			for (Academics acd : profile.getAcademics ()) {

				((LinearLayout) v).setOrientation (LinearLayout.VERTICAL);
				AcademicProfileView academicProfileView = new AcademicProfileView (context);
				academicProfileView.populateData (acd.getInstitute (), acd.getField () + ", " + acd.getDegreeType (), acd.getYear ());
				((LinearLayout) v).addView (academicProfileView);
			}
		}
		else if (position == 2 && !CollectionUtility.isEmptyOrNull (profile.getSkills ())) {
			String skills = "";
			for (String skill : profile.getSkills ()) {
				skills += skill + "\n";
			}

			v = new TextView (context);
			v.setPadding (DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context));
			((TextView) v).setText (skills);
		}
		else if (position == 3) {

			v = new TextView (context);
			v.setPadding (DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context));
			((TextView) v).setText (profile.getResume ());
		}

		if (v != null) {

			scrlContainer.addView (v);
		}

		container.addView (scrlContainer);

		return scrlContainer;
	}

	@Override
	public void destroyItem (ViewGroup container, int position, Object object) {

		container.removeView ((View) object);
	}

}
