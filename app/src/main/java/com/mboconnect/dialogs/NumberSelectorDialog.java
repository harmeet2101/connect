package com.mboconnect.dialogs;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mboconnect.R;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;

/**
 * Created by ali.mehmood on 6/17/2015.
 */
public class NumberSelectorDialog extends Dialog {

	RecyclerView numbersListRecyclerView;
	Context		 context;
	Opportunity	 opportunity;

	public NumberSelectorDialog (Context context, Opportunity opportunity) {

		super (context);
		this.opportunity = opportunity;
		this.context = context;
	}

	public NumberSelectorDialog (Context context, int theme) {

		super (context, theme);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

		requestWindowFeature (Window.FEATURE_NO_TITLE);
		setContentView (R.layout.view_numbers);
		initUI ();
	}

	private void initUI () {

		numbersListRecyclerView = (RecyclerView) findViewById (R.id.numbersListRecyclerView);
		LinearLayoutManager numbersListLayoutManager = new LinearLayoutManager (context);
		numbersListRecyclerView.setLayoutManager (numbersListLayoutManager);
		numbersListRecyclerView.setAdapter (new NumbersAdapter (getContext (), opportunity.getAuthor ().getPhone ()));
	}

	public class NumbersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		HashMap<String, String>	numbers	= new HashMap<String, String> ();
		Context					context;

		public NumbersAdapter (Context context, HashMap<String, String> numbers) {

			this.context = context;
			this.numbers = numbers;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

			return new ViewHolderItem (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_phone_number, parent, false), viewType);
		}

		@Override
		public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

			Map.Entry<String, String> entry = (Map.Entry<String, String>) numbers.entrySet ().toArray ()[position];
			((ViewHolderItem) holder).setData (String.format ("%s: %s", entry.getKey (), entry.getValue ()));
		}

		@Override
		public int getItemViewType (int position) {

			return super.getItemViewType (position);
		}

		@Override
		public int getItemCount () {

			return numbers.size ();
		}

		class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

			private TextView txtNumber;

			public ViewHolderItem (View convertView, final int position) {

				super (convertView);

				txtNumber = (TextView) convertView.findViewById (R.id.txtNumber);
				convertView.setOnClickListener (this);
			}

			public void setData (String number) {

				txtNumber.setText (number);
			}

			@Override
			public void onClick (View v) {

				Map.Entry<String, String> entry = (Map.Entry<String, String>) numbers.entrySet ().toArray ()[getLayoutPosition ()];
				Utils.onCallPressed (context, entry.getValue ().replaceAll (" ", "").trim ());
				dismiss ();
			}
		}
	}
}
