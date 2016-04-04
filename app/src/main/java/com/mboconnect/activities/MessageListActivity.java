package com.mboconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.entities.Message;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.MessageResponse;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.MessageListView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.HashSet;

/**
 * Created by tahir on 21/07/15.
 */
public class MessageListActivity extends BaseActivity {

	private int				   messageListPageSize;
	private int				   messageListSkipSize;
	private int				   messageListTotalSize;
	private APIResponseListner messageResponseListener;
	private boolean			   isRefreshing	  = false;
	HashSet<Message>		   messageHashSet = new HashSet<> ();

	@Override
	public BaseView getView (BaseActionBarActivity activity) {

		return new MessageListView (activity);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

		if (DeviceUtility.isInternetConnectionAvailable (this)) {

			showLoader ();
		}
		initResponseListener ();

	}

	@Override
	protected void onResume () {

		super.onResume ();
		this.getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		refreshList ();

	}

	private void refreshList () {

		initPagination ();

		if (DeviceUtility.isInternetConnectionAvailable (this)) {

			if (((MessageListView) view).getMessageListAdapter ().getMessagesList ().size () > AppConstants.LIST_SIZE_TEN) {

				messageListPageSize = ((MessageListView) view).getMessageListAdapter ().getMessagesList ().size ();
			}
			else {

				messageListPageSize = AppConstants.LIST_SIZE_TEN;
			}

			isRefreshing = true;
			getMessagesAPICall ();
			messageListPageSize = AppConstants.LIST_SIZE_TEN;

		}
		else {

			Utils.showInternetConnectionNotFoundMessage ();
		}
	}

	public void onRefresh () {

		if (DeviceUtility.isInternetConnectionAvailable (this)) {

			isRefreshing = true;
			initPagination ();
			getMessagesAPICall ();
		}
		else {

			((MessageListView) view).setRefreshComplete ();
			Utils.showInternetConnectionNotFoundMessage ();
		}

	}

	private void initPagination () {

		messageListPageSize = AppConstants.LIST_SIZE_TEN;
		messageListSkipSize = 0;
		messageListTotalSize = 0;

	}

	public void getMessagesAPICall () {

		service.profileService.getMessages (messageListPageSize, messageListSkipSize, messageResponseListener);
	}

	private void initResponseListener () {

		messageResponseListener = new APIResponseListner () {

			@Override
			public void onSuccess (CustomHttpResponse response) {

				handleOnSuccessResponse (response);
			}

			@Override
			public void onFailure (CustomHttpResponse response) {

				MessageListView.isLoadMore = true;
				handleFailureResponse (response);
			}
		};
	}

	private void handleOnSuccessResponse (CustomHttpResponse response) {

		if (isRefreshing) {

			((MessageListView) view).getMessageListAdapter ().getMessagesList ().clear ();
			isRefreshing = false;
		}
		((MessageListView) view).getProgressBarHeader ().setVisibility (View.GONE);
		((MessageListView) view).setRefreshComplete ();
		MessageResponse messageResponse = (MessageResponse) response.getResponse ();
		((MessageListView) view).setToolBarScreenName (messageResponse.getMeta ().getNewMessages ());
		updateMessagesCount (messageResponse.getMeta ().getNewMessages ());
		addMessageByRemovingDuplicates (messageResponse);
		((MessageListView) view).getMessageListAdapter ().notifyDataSetChanged ();
		setMessageMeta (messageResponse);
		MessageListView.isLoadMore = true;
		hideLoader ();
	}

	@Override
	public void onBackPressed () {

		Intent intent = new Intent ();
		intent.putExtra ("deleted_object", new Opportunity ());
		setResult (RESULT_OK, intent);
		finish ();
	}

	private void updateMessagesCount (int count) {

		DataModel.setMessagesCount (count);
	}

	private void addMessageByRemovingDuplicates (MessageResponse messageResponse) {

		messageHashSet.clear ();
		messageHashSet.addAll (((MessageListView) view).getMessageListAdapter ().getMessagesList ());

		for (Message message : messageResponse.getList ()) {

			if (messageHashSet.add (message)) {

				((MessageListView) view).getMessageListAdapter ().getMessagesList ().add (message);
			}
		}
	}

	private void handleFailureResponse (CustomHttpResponse response) {

		((MessageListView) view).getProgressBarHeader ().setVisibility (View.GONE);
		UIUtility.showShortToast (response.getErrorMessage (), this);
		hideLoader ();
	}

	private void setMessageMeta (MessageResponse messageResponse) {

		messageListTotalSize = messageResponse.getMeta ().getTotal ();
		messageListSkipSize = ((MessageListView) view).getMessageListAdapter ().getMessagesList ().size ();
	}

	public void loadMoreMessages () {

		if (shouldMakeMessageAPICall ()) {

			((MessageListView) view).getProgressBarHeader ().setVisibility (View.VISIBLE);
			getMessagesAPICall ();
			MessageListView.isLoadMore = false;
		}
		else {

			MessageListView.isLoadMore = true;
		}
	}

	private boolean shouldMakeMessageAPICall () {

		return (((MessageListView) view).getMessageListAdapter ().getMessagesList ().size ()) < messageListTotalSize;
	}
}
