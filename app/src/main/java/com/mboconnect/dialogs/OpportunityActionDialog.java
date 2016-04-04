package com.mboconnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.components.ImageButton;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.model.DataModel;
import com.tenpearls.android.utilities.UIUtility;

/**
 * Created by ali.mehmood on 6/17/2015.
 */
public class OpportunityActionDialog extends Dialog implements View.OnClickListener {

	ImageButton imgBtnEye,
			imgBtnMessage,
			imgBtnFavourite,
			imgBtnTrash;
	Opportunity opportunity;
	int         index;
	Context     context;

	public OpportunityActionDialog (Context context, Opportunity opportunity, int index) {

		super (context);
		this.opportunity = opportunity;
		this.index = index;
		this.context = context;
	}

	public OpportunityActionDialog (Context context, int theme) {

		super (context, theme);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

		requestWindowFeature (Window.FEATURE_NO_TITLE);
		setContentView (R.layout.dialog_opportunity_action);
		getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
		getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		initUI ();
	}

	private void initUI () {

		imgBtnEye = (ImageButton) findViewById (R.id.imgBtnEye);
		imgBtnMessage = (ImageButton) findViewById (R.id.imgBtnMessage);
		imgBtnFavourite = (ImageButton) findViewById (R.id.imgBtnFavourite);
		imgBtnTrash = (ImageButton) findViewById (R.id.imgBtnTrash);

		imgBtnEye.setOnClickListener (this);
		imgBtnMessage.setOnClickListener (this);
		imgBtnFavourite.setOnClickListener (this);
		imgBtnTrash.setOnClickListener (this);

		imgBtnMessage.setEnabled (opportunity.isResponded ());
		imgBtnMessage.setAlpha (opportunity.isResponded () ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
	}

	@Override
	public void onClick (View v) {

		int id = v.getId ();

		if (id == R.id.imgBtnEye) {

			navigateToDetails ();
			dismiss ();
		}
		else if (id == R.id.imgBtnMessage) {

		}
		else if (id == R.id.imgBtnFavourite) {

			toggleFavorite (opportunity);
			dismiss ();
		}
		else if (id == R.id.imgBtnTrash) {

			onDeletePressed ();
			dismiss ();
		}
	}

	private void onDeletePressed () {

		UIUtility.showAlert (null, getContext ().getString (R.string.you_want_to_delete_opportunity), new DialogInterface.OnClickListener () {
			@Override
			public void onClick (DialogInterface dialog, int which) {

				((OpportunityListActivity) context).deleteOpportunity (opportunity);

			}
		}, new DialogInterface.OnClickListener () {
			@Override
			public void onClick (DialogInterface dialog, int which) {

				dialog.dismiss ();
			}
		}, getContext ());
	}

	private void navigateToDetails () {

		Intent intent = new Intent (getContext (), OpportunityDetailsActivity.class);
		intent.putExtra ("selected_position", index);
		((OpportunityListActivity) context).startActivityForResult (intent, OpportunityListActivity.RECORD_UPDATE_REQUEST);
	}

	private void toggleFavorite (Opportunity opportunity) {

		if (!opportunity.isFavorite ()) {

			opportunity.setIsFavorite (true);
			DataModel.updateOpportunity (opportunity);
			((OpportunityListActivity) context).markFavorite (opportunity.getOpportunityId());
		}
		else {

			opportunity.setIsFavorite (false);
			DataModel.updateOpportunity (opportunity);
			((OpportunityListActivity) context).markUnFavorite (opportunity.getOpportunityId());
		}
	}
}
