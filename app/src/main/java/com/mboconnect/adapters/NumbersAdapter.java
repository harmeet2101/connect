package com.mboconnect.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mboconnect.R;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ali.mehmood on 8/26/2015.
 */
public class NumbersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	HashMap<String, String>	numbers	= new HashMap<String, String> ();
	Context					context;
	AlertDialog				dialog;

	public NumbersAdapter (Context context, HashMap<String, String> numbers, AlertDialog dialog) {

		this.context = context;
		this.numbers = numbers;
		this.dialog = dialog;
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
			if (dialog != null) {

				dialog.dismiss ();
			}
		}
	}
}