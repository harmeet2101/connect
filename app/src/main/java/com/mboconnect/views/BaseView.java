package com.mboconnect.views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public abstract class BaseView extends View {
	Context       context;
	View          view;
	public Object controller;

	public BaseView (Context context) {

		super (context);
		this.context = context;
	}

	public View getView () {

		view = ((Activity) context).getLayoutInflater ().inflate (getViewLayout (), null, false);

		return view;
	}

	public void overideView (View view) {

		this.view = view;
	}

	public abstract int getViewLayout ();

	public abstract void onCreate ();

	public abstract void setActionListeners ();

	public void showToast (String string) {

		Toast.makeText (this.context, string, Toast.LENGTH_SHORT);
	}

	public void showLongToast (String string) {

		Toast.makeText (this.context, string, Toast.LENGTH_LONG);

	}

}
