package com.mboconnect.components;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.activities.ProfileActivity;
import com.tenpearls.android.components.TextView;

/**
 * Created by ali.mehmood on 6/15/2015.
 */
public class TabbedViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {

	LinearLayout		linLayoutTabs;
	CustomViewPager		viewPager;
	OnTabChangeListener	onTabChangeListener;
	ArrayList<String>	tabsTitles				 = new ArrayList<String> ();
	boolean				shouldUpdateLastTabColor = true;

	public TabbedViewPager (Context context) {

		super (context);
		initUI ();
	}

	public TabbedViewPager (Context context, AttributeSet attrs) {

		super (context, attrs);
		initUI ();
	}

	public void setShouldUpdateLastTabColor (boolean shouldUpdateLastTabColor) {

		this.shouldUpdateLastTabColor = shouldUpdateLastTabColor;
	}

	private void initUI () {

		LayoutInflater layoutInflater = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate (R.layout.view_tabbed_viewpager, null);

		linLayoutTabs = (LinearLayout) view.findViewById (R.id.linLayoutTabs);
		viewPager = (CustomViewPager) view.findViewById (R.id.viewPager);
		viewPager.addOnPageChangeListener (this);

		this.addView (view);
	}

	private void updateIndicators (int selectedPosition) {

		linLayoutTabs.removeAllViews ();
		LayoutInflater layoutInflater = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < tabsTitles.size (); i++) {

			View tabView = layoutInflater.inflate (R.layout.item_tab, null);

			TextView txtTab = (TextView) tabView.findViewById (R.id.txtTitle);

			if (i == 3 && shouldUpdateLastTabColor) {

				txtTab.setTextColor (getResources ().getColor (R.color.red_button_selected));
			}

			View viewDivider = tabView.findViewById (R.id.viewDivider);
			tabView.setTag (i);

			if (i == selectedPosition) {

				tabView.setBackground (getResources ().getDrawable (R.drawable.tab_selected_bg));
			}

			if (i == (tabsTitles.size () - 1)) {

				viewDivider.setVisibility (View.GONE);
			}

			txtTab.setText (tabsTitles.get (i));
			tabView.setOnClickListener (new OnClickListener () {
				@Override
				public void onClick (View v) {

					viewPager.setCurrentItem ((int) v.getTag ());
				}
			});

			LinearLayout.LayoutParams lp = null;
			if (i == 0 && getContext () instanceof OpportunityDetailsActivity) {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f);
			}
			else if (i == 1 && getContext () instanceof ProfileActivity) {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f);
			}
			else if (i == 1 && getContext () instanceof OpportunityDetailsActivity) {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
			}
			else if (i == 2 && getContext () instanceof ProfileActivity) {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
			}
			else if (i == 2 && getContext () instanceof ProfileActivity) {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.3f);
			}
			else {

				lp = new LinearLayout.LayoutParams (0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f);
			}

			linLayoutTabs.addView (tabView, lp);
		}
	}

	public void setOnTabChangeListener (OnTabChangeListener onTabChangeListener) {

		this.onTabChangeListener = onTabChangeListener;
	}

	@Override
	public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected (int position) {

		updateIndicators (position);

		if (onTabChangeListener != null) {

			onTabChangeListener.onPageSelected (position);
		}
	}

	@Override
	public void onPageScrollStateChanged (int state) {

	}

	public interface OnTabChangeListener {

		public void onPageSelected (int position);
	}

	public void setAdapter (PagerAdapter adapter) {

		viewPager.setAdapter (adapter);
		viewPager.setCurrentItem (0);
		updateIndicators (0);
	}

	public void setTabsTitles (ArrayList<String> tabsTitles) {

		this.tabsTitles = tabsTitles;
	}
}
