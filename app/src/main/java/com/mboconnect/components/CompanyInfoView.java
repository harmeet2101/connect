package com.mboconnect.components;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.mboconnect.R;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.entities.Opportunity;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by ali.mehmood on 6/12/2015.
 */
public class CompanyInfoView extends RelativeLayout {

	private Context			 context;
	private NetworkImageView imgCompanyLogo;
	private TextView		 txtCompanyName,
	        txtCompanyIndustry,
	        txtCompanyAddress,
	        txtCompanyPhone, txtCompanyMobile;
	private TextView		 txtZip;
	Opportunity				 opportunity;
	private String			 phone1, phone2;
	private FrameLayout phonebar;

	public CompanyInfoView (Context context) {

		super (context);
		this.context = context;
		initUI ();
	}

	public CompanyInfoView (Context context, AttributeSet attrs) {

		super (context, attrs);
		this.context = context;
		initUI();
	}

	public CompanyInfoView (Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		this.context = context;
		initUI();
	}

	private void onCallPressed (String phone) {

		if (StringUtility.isEmptyOrNull (phone)) {

			return;
		}

		Intent callIntent = new Intent (Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:" + phone.replace(" ", "").trim()));
		getContext ().startActivity(callIntent);
	}

	private void initUI () {

		LayoutInflater layoutInflater = (LayoutInflater) getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate (R.layout.view_company_info, null);

		imgCompanyLogo = (NetworkImageView) view.findViewById (R.id.imgCompanyLogo);
		txtCompanyName = (TextView) view.findViewById (R.id.txtCompanyName);
		txtCompanyIndustry = (TextView) view.findViewById (R.id.txtCompanyIndustry);
		txtCompanyAddress = (TextView) view.findViewById (R.id.txtCompanyAddress);
		txtCompanyPhone = (TextView) view.findViewById (R.id.txtCompanyPhone);
		txtCompanyMobile= (TextView)view.findViewById(R.id.txtCompanyMobile);
		phonebar=(FrameLayout)view.findViewById(R.id.phoneBar);
		txtZip = (TextView) view.findViewById (R.id.txtZip);

		txtCompanyAddress.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View view) {

				((OpportunityDetailsActivity) context).expandMap ();
			}
		});

		txtCompanyPhone.setOnClickListener (new OnClickListener () {
			@Override

			public void onClick(View v) {

				onCallPressed(phone1);

			}

		});

		txtCompanyMobile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onCallPressed(phone2);
			}
		});

		this.addView (view);
	}

	public void populateData (String companyLogoUrl, String companyName, String companyIndustry, String companyAddress, String zip, String companyPhone, String companyMobile) {

		imgCompanyLogo.setImageUrl (companyLogoUrl, VolleyManager.getInstance ().getImageLoader ());
		txtCompanyName.setText(companyName);
		txtCompanyIndustry.setText(companyIndustry);
		txtCompanyAddress.setText (companyAddress);
		txtCompanyPhone.setVisibility(StringUtility.isEmptyOrNull(companyPhone) ? View.GONE : View.VISIBLE);
		txtCompanyMobile.setVisibility(StringUtility.isEmptyOrNull(companyMobile) ? View.GONE : View.VISIBLE);
		if(StringUtility.isEmptyOrNull(companyPhone) || StringUtility.isEmptyOrNull(companyMobile)){

			phonebar.setVisibility(View.GONE);
		}
		txtCompanyPhone.setText (Html.fromHtml("<b>m: </b>"+companyPhone));
		txtCompanyMobile.setText(Html.fromHtml("<b>h: </b>"+companyMobile));
		txtZip.setText (zip);
		phone1 = companyPhone;
		phone2 = companyMobile;
	}
}
