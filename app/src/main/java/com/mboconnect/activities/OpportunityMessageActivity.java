package com.mboconnect.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.mboconnect.R;
import com.mboconnect.activities.base.BaseActivity;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.Conversation;
import com.mboconnect.entities.ConversationMessage;
import com.mboconnect.entities.MessageDate;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.services.response.ConversationMessageResponse;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.BaseView;
import com.mboconnect.views.OpportunityMessageView;
import com.tenpearls.android.activities.base.BaseActionBarActivity;
import com.tenpearls.android.network.CustomHttpResponse;
import com.tenpearls.android.utilities.DeviceUtility;
import com.tenpearls.android.utilities.StringUtility;
import com.tenpearls.android.utilities.UIUtility;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by tahir on 23/07/15.
 */
public class OpportunityMessageActivity extends BaseActivity {

	APIResponseListner conversationResponseListener;
	private APIResponseListner conversationMessageResponseListener;

	int totalMsges,
			pageSkip, pageSize;
	private APIResponseListner postConversationMessageResponseListener;
	private int selectedIndex;
	private String conversationId;
	Boolean refreshCall=false;

	ArrayList<ConversationMessage> convMsgList = new ArrayList<>();
	ArrayList<String> unReadMessages = new ArrayList<>();
	HashSet<ConversationMessage> conversationMessageHashSet = new HashSet<>();
	Boolean isOpportunityClosed = false;
	private APIResponseListner markReadResponseListener;
	public static boolean sendingMessage = false;
	ArrayList<ConversationMessage> getMessageList = new ArrayList<>();
	ConversationMessage checkLastMessage;
	Handler handler;
	Runnable refreshMessagesRunnable;
	float date;
	int sizeOfMessageList;
	ConversationMessageResponse conversationMessageResponseForAddingLatestMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		initResponseListener();
		initializePagination();

		if (DeviceUtility.isInternetConnectionAvailable(this)) {

			showLoader(getString(R.string.loading));

			if (getIntent().getExtras().getBoolean(getString(R.string.is_message_list))) {

				isOpportunityClosed = getIntent().getExtras().getBoolean(getString(R.string.is_opportunity_closed));
				conversationId = getIntent().getExtras().getString(getString(R.string.conversation_id));
				((OpportunityMessageView) view).getTxtManagerName().setText(getIntent().getExtras().getString(getString(R.string.author_name)));
				getConversationMessages();

			} else {

				selectedIndex = getIntent().getExtras().getInt(getString(R.string.selected_position));
				getConversation();
			}
		} else {

			Utils.showInternetConnectionNotFoundMessage();
		}

		// refreshConversation();
		sendRefreshCall();
	}

	private void sendRefreshCall(){
		handler = new Handler();

		refreshMessagesRunnable = new Runnable() {
			@Override
			public void run() {
				pageSkip = 0;
				refreshCall=true;
				service.oppotunityService.getConversationMessages(conversationId, pageSkip, pageSize, conversationMessageResponseListener);

				handler.postDelayed(this, 20000);
			}
		};
		handler.postDelayed(refreshMessagesRunnable, 20000);

	}

	private void initializePagination() {

		totalMsges = 0;
		pageSize = 15;
		pageSkip = 0;
	}

	@Override
	public BaseView getView(BaseActionBarActivity activity) {

		return new OpportunityMessageView(activity);
	}

	private void getConversation() {

		service.oppotunityService.getConversation(DataModel.getActiveOpportunities().get(selectedIndex).getOpportunityId(), conversationResponseListener);

	}

	private void getConversationMessages() {

		if (conversationId == null) {

			return;
		}
		service.oppotunityService.getConversationMessages(conversationId, convMsgList.size(), pageSize, conversationMessageResponseListener);
	}

	public void postConversationMessage(String message) {

		if (conversationId == null) {

			return;
		}

		((OpportunityMessageView) view).disableSendButton();
		service.oppotunityService.postConversationMessages(conversationId, message, postConversationMessageResponseListener);

	}

	private void markReadMessage() {

		service.oppotunityService.markReadMessages(unReadMessages, conversationId, markReadResponseListener);

	}

	private void initResponseListener() {

		conversationResponseListener = new APIResponseListner() {
			@Override
			public void onSuccess(CustomHttpResponse response) {

				handleConversationResponse(response);
				//  Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(CustomHttpResponse response) {
				//  Toast.makeText(getApplicationContext(), "Failure!", Toast.LENGTH_SHORT).show();
			}
		};

		conversationMessageResponseListener = new APIResponseListner() {
			@Override
			public void onSuccess(CustomHttpResponse response) {

				handleMessagesOnSuccess(response);
				// Toast.makeText(getApplicationContext(), "Success! Conversation updated..!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(CustomHttpResponse response) {

				OpportunityMessageView.isLoadMore = true;
				((OpportunityMessageView) view).getProgressBarHeader().setVisibility(View.GONE);
				hideLoader();
			}
		};

		postConversationMessageResponseListener = new APIResponseListner() {
			@Override
			public void onSuccess(CustomHttpResponse response) {


				ConversationMessageResponse conversationMessageResponse = (ConversationMessageResponse) response.getResponse();
				ConversationMessage message = ((ArrayList<ConversationMessage>) conversationMessageResponse.getList()).get(0);

				addMessage(message);
				updateMeta(conversationMessageResponse);
				((OpportunityMessageView) view).enableSendButton();
			}

			@Override
			public void onFailure(CustomHttpResponse response) {

				((OpportunityMessageView) view).enableSendButton();
				if (DeviceUtility.isInternetConnectionAvailable(OpportunityMessageActivity.this)) {

					UIUtility.showLongToast(response.getErrorMessage(), OpportunityMessageActivity.this);

				} else {

					Utils.showInternetConnectionNotFoundMessage();
				}
			}
		};

		markReadResponseListener = new APIResponseListner() {
			@Override
			public void onSuccess(CustomHttpResponse response) {

			}

			@Override
			public void onFailure(CustomHttpResponse response) {

				handleFailureResponse(response);
			}
		};

	}

	private void handleFailureResponse(CustomHttpResponse response) {

		UIUtility.showShortToast(response.getErrorMessage(), this);
		hideLoader();
	}

	private void handleConversationResponse(CustomHttpResponse response) {

		String authorName = null;
		Conversation conversation = (Conversation) response.getResponse();
		conversationId = conversation.getConversationId();
		// ((OpportunityMessageView) view).getTxtManagerName().setText(getIntent().getExtras().getString(getString(R.string.author_name)));
		setAuthorName(authorName, conversation);
		getConversationMessages();
	}

	private void setAuthorName(String authorName, Conversation conversation) {

		for (int i = 0; i < conversation.getMemberList().size(); i++) {

			if (conversation.getMemberList().get(i).getMemberType() == EnumConstants.MemberType.MANAGER) {

				authorName = conversation.getMemberList().get(i).getName();
			}
		}


		if (StringUtility.isEmptyOrNull(authorName)) {

			for (int i = 0; i < conversation.getMemberList().size(); i++) {

				if (conversation.getMemberList().get(i).getMemberType() == EnumConstants.MemberType.AUTHOR) {

					authorName = conversation.getMemberList().get(i).getName();
				}
			}
		}
		((OpportunityMessageView) view).getTxtManagerName().setText(authorName);
	}

	private void addMessage(ConversationMessage message) {

		if (((OpportunityMessageView) view).getEtxtMessageBox().getText().toString().trim().length() == 0) {

			return;
		}

		if (isOpportunityClosed) {

			UIUtility.showShortToast(getString(R.string.opportunity_closed_message), this);
			return;
		}

//        ConversationMessage message = new ConversationMessage();
//        message.setBody(((OpportunityMessageView) view).getEtxtMessageBox().getText().toString().trim());
//        message.setDateCreated(System.currentTimeMillis() + "");
//        message.setImgUrl(DataModel.getProfile().getImageURL());
//        message.setIsRead(true);
//        message.setIsMine(true);
//        message.setId(id);
		((OpportunityMessageView) view).getEtxtMessageBox().setText("");
		convMsgList.add(0, message);
		createGroupedDataByDate();
	}

	private void  handleMessagesOnSuccess(CustomHttpResponse response) {

		ConversationMessageResponse conversationMessageResponse = (ConversationMessageResponse) response.getResponse();
		updateMeta(conversationMessageResponse);
		if(refreshCall==true){
			autoRefreshMessages(conversationMessageResponse);
			refreshCall=false;
		}
		else{

			removeDuplicateMessages(conversationMessageResponse);
		}
		markReadMessages();
		createGroupedDataByDate();
		OpportunityMessageView.isLoadMore = true;
		((OpportunityMessageView) view).getProgressBarHeader().setVisibility(View.GONE);
		hideLoader();
	}

	private void markReadMessages() {

		unReadMessages.clear();
		for (ConversationMessage message : convMsgList) {

			if (!message.getIsRead()) {

				unReadMessages.add(message.getConversationMessageId().toString());
				message.setIsRead(true);
			}

		}
		if (unReadMessages.size() > 0) {

			markReadMessage();
		}
	}


	private void autoRefreshMessages(ConversationMessageResponse conversationMessageResponse){

		//  Toast.makeText(getApplicationContext(),"AutoRefresh Called",Toast.LENGTH_SHORT).show();

		String lastMessage;
		lastMessage=convMsgList.get(0).getDateCreated();
		ArrayList<ConversationMessage> myNewMessages=new ArrayList<>();
		myNewMessages.addAll(conversationMessageResponse.getList());

		if(!lastMessage.equals(myNewMessages.get(0).getDateCreated())){

			convMsgList.clear();
			convMsgList.addAll(conversationMessageResponse.getList());
			OpportunityMessageView.recyclerViewMessage.post(new Runnable() {

				@Override
				public void run() {
					OpportunityMessageView.recyclerViewMessage.scrollToPosition(0);
				}
			});

		}

       /* convMsgList.clear();
        convMsgList.addAll(conversationMessageResponse.getList());
        OpportunityMessageView.recyclerViewMessage.post(new Runnable() {

            @Override
            public void run() {
                OpportunityMessageView.recyclerViewMessage.scrollToPosition(0);
            }
        });
*/
	}

	private void removeDuplicateMessages(ConversationMessageResponse conversationMessageResponse) {

		//int size,size2,difference;
		if (convMsgList.size() == 0) {
			convMsgList.addAll(conversationMessageResponse.getList());
			
			return;
		}

		conversationMessageHashSet.clear();
		conversationMessageHashSet.addAll(convMsgList);

		ArrayList<ConversationMessage> myNewMessages=new ArrayList<>();

		for (ConversationMessage message : conversationMessageResponse.getList()) {

			int size = conversationMessageHashSet.size();

			conversationMessageHashSet.add(message);

			if (conversationMessageHashSet.size() > size) {

				convMsgList.add(message);

				checkLastMessage=convMsgList.get(convMsgList.size()-1);
			}
			date= Float.parseFloat(message.getDateCreated());
		}

	}

	private void createGroupedDataByDate() {

		((OpportunityMessageView) view).getMessageAdapter().getItems().clear();
		MessageDate messageDate = null;

		int size=convMsgList.size();

		for (int i = 0; i < convMsgList.size(); i++) {

			String date = Utils.getFormattedDateFromMilis(Long.parseLong(convMsgList.get(i).getDateCreated()), AppConstants.OPPORTUNITY_MESSAGE_DATE_FORMAT);

			if (messageDate == null || messageDate.getDate() == null) {

				messageDate = setMessageDate(date);

			}
			if (messageDate.getDate() != null && !messageDate.getDate().equalsIgnoreCase(date)) {

				((OpportunityMessageView) view).getMessageAdapter().getItems().add(messageDate);
				messageDate = setMessageDate(date);

			}

			((OpportunityMessageView) view).getMessageAdapter().getItems().add(convMsgList.get(i));

			if (i == convMsgList.size() - 1) {
				messageDate.setDate(date);
				((OpportunityMessageView) view).getMessageAdapter().getItems().add(messageDate);
			}

		}

		for(int i=0;i<convMsgList.size(); i++){
			String currentDate = convMsgList.get(i).getDateCreated();
			Log.d("Date","Current Date: "+currentDate);
		}

		((OpportunityMessageView) view).getMessageAdapter().notifyDataSetChanged();
	}

	private MessageDate setMessageDate(String date) {

		MessageDate messageDate;
		messageDate = new MessageDate();
		messageDate.setDate(date);
		return messageDate;
	}

	private void updateMeta(ConversationMessageResponse conversationMessageResponse) {

		totalMsges = conversationMessageResponse.getMeta().getTotal();
		pageSize = conversationMessageResponse.getMeta().getPageSize();
		//addNewMessage(conversationMessageResponse);
	}

	public void loadMoreMessages() {

		if (shouldMakeMessageAPICall()) {

			((OpportunityMessageView) view).getProgressBarHeader().setVisibility(View.VISIBLE);
			getConversationMessages();
			OpportunityMessageView.isLoadMore = false;

		} else {

			OpportunityMessageView.isLoadMore = true;

		}
	}


	@Override
	protected void onPause() {
		super.onPause();

		handler.removeCallbacks(refreshMessagesRunnable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//  Toast.makeText(getApplicationContext(),"OnResume",Toast.LENGTH_SHORT).show();

		// refreshConversation();
		// handler.post(refreshMessagesRunnable);
		//handler.postDelayed(refreshMessagesRunnable,5000);
	}

	private boolean shouldMakeMessageAPICall() {

		return (convMsgList.size()) < totalMsges;
	}
}
