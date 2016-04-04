package com.mboconnect.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityMessageActivity;
import com.mboconnect.adapters.OpportunityMessageAdapter;
import com.mboconnect.constants.AppConstants;
import com.tenpearls.android.components.EditText;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by tahir on 23/07/15.
 */
public class OpportunityMessageView extends BaseView {

	public static boolean			  isLoadMore;
	public static RecyclerView			  recyclerViewMessage;
	private LinearLayoutManager		  layoutManagerMessage;
	private OpportunityMessageAdapter messageAdapter;
	private Toolbar					  mToolbar;
	private android.widget.EditText	  etxtMessageBox;
	private TextView				  txtScreenName;
	private TextView				  txtManagerName;
	private ProgressBar				  progressBarHeader;
	private TextView				  txtSend;

	public OpportunityMessageView (Context context) {

		super (context);

	}

	@Override
	public int getViewLayout () {

		return R.layout.view_opportunity_message;
	}

	@Override
	public void onCreate () {

		initUi ();

	}

	@Override
	public void setActionListeners () {

	}

	private void initUi () {

		mToolbar = (Toolbar) view.findViewById (R.id.toolBarMessage);
		((OpportunityMessageActivity) controller).setSupportActionBar (mToolbar);
		ImageView btnBackToolBar = (ImageView) view.findViewById (R.id.btnBackToolBar);
		txtScreenName = (TextView) view.findViewById (R.id.txtScreenName);
		etxtMessageBox = (android.widget.EditText) view.findViewById (R.id.etxtMessageBox);
		txtManagerName = (TextView) view.findViewById (R.id.txtManagerName);
		progressBarHeader = (ProgressBar) view.findViewById (R.id.progressBarHeader);
		setupRecyclerMessageList ();
		btnBackToolBar.setOnClickListener (btnBackToolBarListener);
		txtScreenName.setText (getContext ().getString (R.string.messages));
		recyclerViewMessage.setOnScrollListener (conversationMessageScrollListener);
		isLoadMore = true;
		txtSend = (TextView) view.findViewById (R.id.txtSend);
		txtSend.setOnClickListener (new OnClickListener () {
			@Override
			public void onClick (View v) {

				if (StringUtility.isEmptyOrNull (etxtMessageBox.getText ().toString ())) {

					return;
				}
				((OpportunityMessageActivity) controller).postConversationMessage (etxtMessageBox.getText ().toString ().trim ());
			}
		});
	}

	public void disableSendButton () {

		txtSend.setEnabled (false);
		txtSend.setAlpha (AppConstants.VIEW_DISABLE_ALPHA);
	}

	public void enableSendButton () {

		txtSend.setEnabled (true);
		txtSend.setAlpha (AppConstants.VIEW_ENABLE_ALPHA);
	}

	private void setupRecyclerMessageList () {

		recyclerViewMessage = (RecyclerView) view.findViewById (R.id.recyclerViewMessage);
		layoutManagerMessage = new LinearLayoutManager (context);
		layoutManagerMessage.setReverseLayout (true);
		recyclerViewMessage.setLayoutManager (layoutManagerMessage);
		messageAdapter = new OpportunityMessageAdapter (context);
		recyclerViewMessage.setAdapter (messageAdapter);
	}

	OnClickListener btnBackToolBarListener = new OnClickListener () {
		@Override
		public void onClick (View v) {

			((OpportunityMessageActivity) controller).onBackPressed ();
		}

	};

	TextView.OnEditorActionListener etxtMessageBoxActionListener = new TextView.OnEditorActionListener () {
		@Override
		public boolean onEditorAction (android.widget.TextView v, int actionId, KeyEvent event) {

			if (event != null && event.getAction () != KeyEvent.ACTION_DOWN) {
				return false;
			}
			else if (actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event != null && event.getKeyCode () == KeyEvent.KEYCODE_ENTER) {

				((OpportunityMessageActivity) controller).postConversationMessage (etxtMessageBox.getText ().toString ().trim ());
			}

			return true;
		}

	};

	RecyclerView.OnScrollListener conversationMessageScrollListener = new RecyclerView.OnScrollListener () {
		@Override
		public void onScrolled (RecyclerView recyclerView, int dx, int dy) {

			super.onScrolled (recyclerView, dx, dy);

			int pastVisiblesItems = layoutManagerMessage.findFirstVisibleItemPosition ();
			int visibleItemCount = layoutManagerMessage.getChildCount ();
			int totalItemCount = layoutManagerMessage.getItemCount ();
			checkLoadMore (pastVisiblesItems, visibleItemCount, totalItemCount);

		}

	};

	private void checkLoadMore (int pastVisiblesItems, int visibleItemCount, int totalItemCount) {

		if (isLoadMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {

			((OpportunityMessageActivity) context).loadMoreMessages ();

		}
	}

	public RecyclerView getRecyclerViewMessage () {

		return recyclerViewMessage;
	}

	public void setRecyclerViewMessage (RecyclerView recyclerViewMessage) {

		this.recyclerViewMessage = recyclerViewMessage;
	}

	public LinearLayoutManager getLayoutManagerMessage () {

		return layoutManagerMessage;
	}

	public void setLayoutManagerMessage (LinearLayoutManager layoutManagerMessage) {

		this.layoutManagerMessage = layoutManagerMessage;
	}

	public OpportunityMessageAdapter getMessageAdapter () {

		return messageAdapter;
	}

	public void setMessageAdapter (OpportunityMessageAdapter messageAdapter) {

		this.messageAdapter = messageAdapter;
	}

	public TextView getTxtScreenName () {

		return txtScreenName;
	}

	public void setTxtScreenName (TextView txtScreenName) {

		this.txtScreenName = txtScreenName;
	}

	public TextView getTxtManagerName () {

		return txtManagerName;
	}

	public void setTxtManagerName (TextView txtManagerName) {

		this.txtManagerName = txtManagerName;
	}

	public ProgressBar getProgressBarHeader () {

		return progressBarHeader;
	}

	public void setProgressBarHeader (ProgressBar progressBarHeader) {

		this.progressBarHeader = progressBarHeader;
	}

	public android.widget.EditText getEtxtMessageBox () {

		return etxtMessageBox;
	}

	public void setEtxtMessageBox (EditText etxtMessageBox) {

		this.etxtMessageBox = etxtMessageBox;
	}

}
