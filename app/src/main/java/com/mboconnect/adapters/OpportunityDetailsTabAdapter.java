package com.mboconnect.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.mboconnect.R;
import com.mboconnect.components.CompanyInfoView;
import com.mboconnect.components.OpportunityDetailsHeaderView;
import com.mboconnect.entities.Opportunity;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by ali.mehmood on 8/10/2015.
 */
public class OpportunityDetailsTabAdapter extends PagerAdapter {

	Opportunity	opportunity;
	Context		context;

	public OpportunityDetailsTabAdapter (Context context, Opportunity opportunity) {

		this.context = context;
		this.opportunity = opportunity;
	}

	public Object getItem (int position) {

		return null;
	}

	@Override
	public int getCount () {

		return 4;
	}

	@Override
	public int getItemPosition (Object object) {

		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem (ViewGroup container, int position) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate (R.layout.item_tab_layout, null);
		ScrollView scrlContainer = (ScrollView) view.findViewById (R.id.scrlContainer);

		View v = null;

		if (position == 0 && !StringUtility.isEmptyOrNull (opportunity.getDescription ())) {

			ScrollView.LayoutParams lp = new FrameLayout.LayoutParams (ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT);

			v = new TextView (context);
			v.setLayoutParams (lp);
			v.setPadding(DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context));
			((TextView) v).setTextSize(13);
			((TextView) v).setText(Html.fromHtml(opportunity.getDescription()));

		}
		else if (position == 1 && !CollectionUtility.isEmptyOrNull (opportunity.getSkills ())) {

			String skills = "";
			for (String skill : opportunity.getSkills ()) {
				skills += skill + "\n";
			}

			ScrollView.LayoutParams lp = new FrameLayout.LayoutParams (ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT);

			v = new TextView (context);
			v.setLayoutParams (lp);
			v.setPadding(DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context), DeviceUtility.getPixelsFromDps(10, context));
			((TextView) v).setTextSize(13);
			((TextView) v).setText(skills);

		}
		else if (position == 2) {

			if (opportunity.isCompanyInfoVisible ()) {
				v = new CompanyInfoView (context);
				String imgUrl=opportunity.getCompanyImgUrl();
				((CompanyInfoView) v).populateData (opportunity.getCompanyImgUrl (), opportunity.getCompanyName (), opportunity.getCompanyIndustry (), opportunity.getAddress ().getFormattedAddress (), opportunity.getAddress ().getZip (), opportunity.getCompanyPhone (), opportunity.getCompanyMobile ());

			}
			else {

				ScrollView.LayoutParams lp = new ScrollView.LayoutParams (ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER;
				v = new TextView (context);
				v.setPadding (DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context), DeviceUtility.getPixelsFromDps (10, context));
				v.setLayoutParams(lp);
				((TextView) v).setTextSize(13);
				((TextView) v).setText("Employer is confidential and will be revealed upon acceptance.");

			}
		}
		else if (position == 3) {

			v = new OpportunityDetailsHeaderView (context);
			((OpportunityDetailsHeaderView) v).populateData (opportunity);
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

	@Override
	public boolean isViewFromObject (View arg0, Object arg1) {

		return arg0 == arg1;
	}
}