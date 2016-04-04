package com.mboconnect.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.mboconnect.R;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.adapters.NumbersAdapter;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.model.DataModel;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.Button;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.HashMap;

/**
 * Created by ali.mehmood on 6/12/2015.
 */
public class OpportunityDetailsHeaderView extends RelativeLayout {

    private NetworkImageView imgProfilePic;
    private Context context;
    private ImageButton btnMessage,
            btnEmail, btnCall;
    private TextView txtName,
            txtDesignation;
    Boolean isMobileNumberValid = true;
    Boolean isHomeNumberValid = true;
    private Button btnRespond;
    private Opportunity opportunity;
    private RelativeLayout opportunitydetailRLayout;

    public OpportunityDetailsHeaderView(Context context) {

        super(context);
        initUI();
    }

    public OpportunityDetailsHeaderView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initUI();
    }

    public OpportunityDetailsHeaderView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_opportunity_detais_header, null);

        imgProfilePic = (NetworkImageView) view.findViewById(R.id.imgProfilePic);
        btnMessage = (ImageButton) view.findViewById(R.id.btnMessage);
        btnEmail = (ImageButton) view.findViewById(R.id.btnEmail);
        btnCall = (ImageButton) view.findViewById(R.id.btnCall);
        btnRespond = (Button) view.findViewById(R.id.btnRespond);
        opportunitydetailRLayout = (RelativeLayout) view.findViewById(R.id.opportunityRelativeLayout);
        if (btnRespond.getText() == "Respond") {

            // opportunitydetailRLayout

        }
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtDesignation = (TextView) view.findViewById(R.id.txtDesignation);
        btnMessage.setEnabled(((OpportunityDetailsActivity) getContext()).isSelectedOpportunityIsResponded() ? true : false);
        btnMessage.setAlpha(((OpportunityDetailsActivity) getContext()).isSelectedOpportunityIsResponded() ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
        // txtTimestamp = (TextView) view.findViewById (R.id.txtJobDate);

        btnMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((OpportunityDetailsActivity) getContext()).isSelectedOpportunityIsResponded()) {

                    ((OpportunityDetailsActivity) getContext()).startConversationMessageActivity();
                } else {

                    UIUtility.showShortToast(getContext().getString(R.string.opportunity_not_responded), getContext());
                }

            }
        });

        btnEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onEmailPressed();
            }
        });

        btnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer size = opportunity.getAuthor().getPhone().size();

                if (opportunity.getAuthor().getPhone().size() == 1) {

                    Utils.onCallPressed(getContext(), opportunity.getAuthor().getPhone().get(opportunity.getAuthor().getPhone().entrySet().iterator().next().getKey()).replaceAll(" ", "").trim());
                } else {
                    areNumbersValid();
                    if (isMobileNumberValid  && isHomeNumberValid) {
                        showSelectNumberDialog(opportunity);
                    } else {
                        if (isHomeNumberValid) {
                            String homeno=opportunity.getAuthor().getPhone().get("Home");
                            Utils.onCallPressed(getContext(), opportunity.getAuthor().getPhone().get(("Home")).replaceAll(" ", "").trim());
                        } else if (isMobileNumberValid) {
                            Utils.onCallPressed(getContext(), opportunity.getAuthor().getPhone().get(("Mobile")).replaceAll(" ", "").trim());
                        }

                    }
                }
            }
        });

        btnRespond.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onRespondPressed();
            }
        });

        this.addView(view);
    }

    private void areNumbersValid() {

        HashMap<String, String> phones = new HashMap<String, String>();
        phones = opportunity.getAuthor().getPhone();
        //String no= (String) phones.keySet().toArray()[1];
        //String number=phones.get(no);

        if (phones.get("Mobile").equals("")) {

            isMobileNumberValid = false;
        }

        if (phones.get("Home").equals("")) {

            isHomeNumberValid = false;
        }

    }


    private void onEmailPressed() {

        if (StringUtility.isEmptyOrNull(opportunity.getAuthor().getEmail())) {

            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: " + DataModel.getActiveOpportunities().get(((OpportunityDetailsActivity) getContext()).getSelectedIndex()).getTitle());
        emailIntent.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.enter_your_message));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{opportunity.getAuthor().getEmail().trim()});
        getContext().startActivity(Intent.createChooser(emailIntent, getContext().getString(R.string.send_email)));
    }

    private void showSelectNumberDialog(Opportunity opportunity) {

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_numbers, null);
        builderSingle.setView(view);
        builderSingle.setTitle(getContext().getString(R.string.select_phone_number));

        builderSingle.setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builderSingle.create();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.numbersListRecyclerView);
        LinearLayoutManager numbersListLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(numbersListLayoutManager);
        recyclerView.setAdapter(new NumbersAdapter(getContext(), opportunity.getAuthor().getPhone(), dialog));

        dialog.show();
        dialog.getWindow().setLayout(900, 730);
    }

    public void populateData(Opportunity opportunity) {

        this.opportunity = opportunity;

        txtName.setText(opportunity.getAuthor() == null ? "" : opportunity.getAuthor().getName());
        txtDesignation.setText(opportunity.getAuthor() == null ? "" : opportunity.getAuthor().getDesignation());
        if (opportunity.getAuthor() == null) {

            txtName.setVisibility(View.GONE);
        }
        btnRespond.setSelected(opportunity.isResponded());

        if (opportunity.isResponded()) {

            btnRespond.setText(getContext().getString(R.string.responded));
        } else {
            btnRespond.setText(getContext().getString(R.string.respond));
        }

        btnCall.setEnabled(opportunity.getAuthor() == null ? false : opportunity.getAuthor().getPhone().size() != 0);
        btnEmail.setEnabled(opportunity.getAuthor() == null ? false : !StringUtility.isEmptyOrNull(opportunity.getAuthor().getEmail()));
        btnEmail.setAlpha(btnEmail.isEnabled() ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
        btnCall.setAlpha(btnCall.isEnabled() ? AppConstants.VIEW_ENABLE_ALPHA : AppConstants.VIEW_DISABLE_ALPHA);
        imgProfilePic.setDefaultImageResId(R.drawable.thumbnail);
        imgProfilePic.setErrorImageResId(R.drawable.thumbnail);
        imgProfilePic.setImageUrl(opportunity.getAuthor() == null ? "" : opportunity.getAuthor().getImageUrl(), VolleyManager.getInstance().getImageLoader());
    }

    private void onRespondPressed() {

        if (opportunity.isResponded()) {

            return;
        }

        Utils.showAlert(getContext().getString(R.string.are_you_sure), getContext().getString(R.string.by_responding_opportunity), getContext().getString(R.string.continue_caps), getContext().getString(R.string.cancel_caps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((OpportunityDetailsActivity) getContext()).respondToOpportunity(opportunity);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }, getContext());
    }
}
