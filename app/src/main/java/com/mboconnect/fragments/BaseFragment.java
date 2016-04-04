package com.mboconnect.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mboconnect.R;
import com.mboconnect.services.ServiceFactory;
import com.mboconnect.views.BaseView;
import com.tenpearls.android.components.Loader;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.UIUtility;

public abstract class BaseFragment extends Fragment {
	BaseView       view;
	View           txt;
	ServiceFactory service;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.view = this.getView (getActivity ());
		View containerView = getActivity ().getLayoutInflater ().inflate (this.view.getViewLayout (), null, false);
		this.view.overideView (containerView);
		return containerView;
	}

	@Override
	public void onActivityCreated (Bundle savedInstanceState) {

		super.onActivityCreated (savedInstanceState);
		if (this.view == null) {
			Toast.makeText (getActivity (), "view null", Toast.LENGTH_LONG).show ();
			return;
		}
		this.view.onCreate ();
		this.view.setActionListeners ();
		this.view.controller = this;
		this.service = new ServiceFactory (getActivity ());
		onCreated ();
	}

	@Override
	public void onInflate (Activity activity, AttributeSet attrs, Bundle savedInstanceState) {

		super.onInflate (activity, attrs, savedInstanceState);
	}

	protected void onServiceFailure (CustomHttpResponse response) {

		hideLoader ();
		UIUtility.showShortToast (response.getErrorMessage (), getActivity ());
	}

	protected void showLoader () {

		showLoader (getString (R.string.loading));
	}

	protected void showLoader (String message) {

		try {
			Loader.showLoader (getActivity (), message, null);
		}
		catch (Exception e) {
		}
	}

	protected void hideLoader () {

		try {
			Loader.hideLoader ();

		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public abstract BaseView getView (Activity activity);

	public abstract void onCreated ();

	public void refresh () {

		// implement in child classes where required
	}
}
