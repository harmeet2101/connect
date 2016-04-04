package com.mboconnect.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.entities.Preference;

import java.util.ArrayList;


/**
 * Created by tahir on 07/07/15.
 */
public class SuggestionCursorAdapter extends CursorAdapter {

    private final Context context;

    public ArrayList<Preference> items=new ArrayList<Preference>();
    private TextView text;

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public SuggestionCursorAdapter(Context context, Cursor cursor, ArrayList<Preference> items) {

        super(context, cursor, false);
        this.context = context;
        this.items=items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        text = (TextView) view.findViewById(R.id.text);
        text.setText(items.get(cursor.getPosition()).getValue());

        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.btnCancel);
        btnCancel.setTag(cursor.getPosition());
        btnCancel.setOnClickListener(btnCancelOnClickListener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_search_suggestion, parent, false);

        return view;

    }

    View.OnClickListener btnCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            items.remove(tag);
            ((OpportunityListActivity) context).updateSuggestionList();
        }
    };
}
