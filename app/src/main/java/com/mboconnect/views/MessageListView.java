package com.mboconnect.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mboconnect.R;
import com.mboconnect.activities.MessageListActivity;
import com.mboconnect.adapters.MessageListAdapter;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.DeviceUtility;

/**
 * Created by tahir on 21/07/15.
 */
public class MessageListView extends BaseView {

	public static boolean       isLoadMore;
	private SwipeRefreshLayout  swipeRefreshLayout;
	private LinearLayoutManager messageListLayoutManager;
	private RecyclerView        recyclerViewMessageList;
	private MessageListAdapter  messageListAdapter;
	private TextView            txtScreenName;
	private ProgressBar         progressBarHeader;

	public MessageListView (Context context) {

		super (context);
	}

	@Override
	public int getViewLayout () {

		return R.layout.view_message_list;
	}

	@Override
	public void onCreate () {

		initUI ();
	}

	@Override
	public void setActionListeners () {

	}

	private void initUI () {

		setupToolBar ();
		initializeVariable ();
		setupSwipeRefreshLayout ();
		setupRecyclerViewMessageList ();

	}

	private void initializeVariable () {

		isLoadMore = true;
	}

	private void setupRecyclerViewMessageList () {

		recyclerViewMessageList = (RecyclerView) view.findViewById (R.id.recyclerViewMessageList);
		messageListLayoutManager = new LinearLayoutManager (context);
		recyclerViewMessageList.setLayoutManager (messageListLayoutManager);
		recyclerViewMessageList.setOnScrollListener (recyclerViewMessageListOnScrollListener);
		messageListAdapter = new MessageListAdapter (context);
		recyclerViewMessageList.setAdapter (messageListAdapter);

	}

	private void setupSwipeRefreshLayout () {

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById (R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources (R.color.grey_light, R.color.blue, R.color.orange);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				if (DeviceUtility.isInternetConnectionAvailable(getContext())) {

					((MessageListActivity) controller).onRefresh();
				} else {

					Utils.showInternetConnectionNotFoundMessage();

				}
			}
		});
	}

	private void setupToolBar () {

		Toolbar toolBarMessageList = (Toolbar) view.findViewById (R.id.toolBarMessageList);
		((MessageListActivity) controller).setSupportActionBar (toolBarMessageList);
		((MessageListActivity) controller).getSupportActionBar ().setDisplayShowTitleEnabled (false);
		ImageView btnBackToolBar = (ImageView) view.findViewById (R.id.btnBackToolBar);
		progressBarHeader = (ProgressBar) view.findViewById (R.id.progressBarHeader);
		txtScreenName = (TextView) view.findViewById (R.id.txtScreenName);
		txtScreenName.setText(context.getResources().getString(R.string.messages));
		btnBackToolBar.setOnClickListener(btnBackToolBarOnClickListener);
	}


	OnClickListener               btnBackToolBarOnClickListener           = new OnClickListener () {
		                                                                      @Override
		                                                                      public void onClick (View v) {

			                                                                      ((MessageListActivity) controller).onBackPressed ();

		                                                                      }
	                                                                      };

	RecyclerView.OnScrollListener recyclerViewMessageListOnScrollListener = new RecyclerView.OnScrollListener () {
		                                                                      @Override
		                                                                      public void onScrollStateChanged (RecyclerView recyclerView, int newState) {

			                                                                      super.onScrollStateChanged (recyclerView, newState);
		                                                                      }

		                                                                      @Override
		                                                                      public void onScrolled (RecyclerView recyclerView, int dx, int dy) {

			                                                                      super.onScrolled (recyclerView, dx, dy);

			                                                                      int pastVisiblesItems = getMessageListLayoutManager ().findFirstVisibleItemPosition ();
			                                                                      int visibleItemCount = getMessageListLayoutManager ().getChildCount ();
			                                                                      int totalItemCount = getMessageListLayoutManager ().getItemCount ();

			                                                                      checkLoadMore (pastVisiblesItems, visibleItemCount, totalItemCount);
		                                                                      }
	                                                                      };

	private void checkLoadMore (int pastVisiblesItems, int visibleItemCount, int totalItemCount) {

		if (isLoadMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {

			if (!DeviceUtility.isInternetConnectionAvailable (context)) {

				Utils.showInternetConnectionNotFoundMessage ();
				return;
			}

			((MessageListActivity) context).loadMoreMessages ();

		}
	}

	public void setToolBarScreenName (int unreadMessages) {

		if (unreadMessages > 0) {

			this.txtScreenName.setText (context.getResources ().getString (R.string.messages) + " (" + unreadMessages + ")");
		}
		else {

			this.txtScreenName.setText (context.getResources ().getString (R.string.messages));
		}

	}

	// Methods

	public void setRefreshComplete () {

		swipeRefreshLayout.setRefreshing (false);
	}

	public void enableSwipeLayout () {

		swipeRefreshLayout.setEnabled (true);
	}

	public void disableSwipeLayout () {

		swipeRefreshLayout.setEnabled (false);
	}

	// Getter Setter
	public MessageListAdapter getMessageListAdapter () {

		return messageListAdapter;
	}

	public void setMessageListAdapter (MessageListAdapter messageListAdapter) {

		this.messageListAdapter = messageListAdapter;
	}

	public RecyclerView getRecyclerViewMessageList () {

		return recyclerViewMessageList;
	}

	public void setRecyclerViewMessageList (RecyclerView recyclerViewMessageList) {

		this.recyclerViewMessageList = recyclerViewMessageList;
	}

	public LinearLayoutManager getMessageListLayoutManager () {

		return messageListLayoutManager;
	}

	public void setMessageListLayoutManager (LinearLayoutManager messageListLayoutManager) {

		this.messageListLayoutManager = messageListLayoutManager;
	}

	public SwipeRefreshLayout getSwipeRefreshLayout () {

		return swipeRefreshLayout;
	}

	public void setSwipeRefreshLayout (SwipeRefreshLayout swipeRefreshLayout) {

		this.swipeRefreshLayout = swipeRefreshLayout;
	}

	public ProgressBar getProgressBarHeader () {

		return progressBarHeader;
	}

	public void setProgressBarHeader (ProgressBar progressBarHeader) {

		this.progressBarHeader = progressBarHeader;
	}

}
