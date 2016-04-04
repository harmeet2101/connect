package com.mboconnect.adapters;

import com.mboconnect.R;
import com.mboconnect.activities.OpportunityMessageActivity;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.entities.ConversationMessage;
import com.mboconnect.entities.Message;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.utilities.UIUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context           context;
	public ArrayList<Message> messagesList = new ArrayList<> ();

	public MessageListAdapter (Context context) {

		// TODO Auto-generated constructor stub

		super ();
		this.context = context;

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

		return new ViewHolderMessage (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_message_list, parent, false), viewType);
	}

	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

		Message message = messagesList.get (position);
		((ViewHolderMessage) holder).populateData (message.getAuthorName (), message.getMessage (), message.getSent (), message.getIsRead (), message.getUnreadMessages ());

	}

	@Override
	public long getItemId (int position) {

		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemCount () {

		return messagesList.size ();
	}

	public class ViewHolderMessage extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final TextView       txtMessageSubject;
		private final TextView       txtMessageDate;
		private final TextView       txtMessageBody;
		private final RelativeLayout layoutItemMessageList;
		private final TextView       txtMessageCount;
		private final RelativeLayout circleLayoutMessageNumber;

		public ViewHolderMessage (View itemView, int position) {

			super (itemView);

			itemView.setOnClickListener (this);

			itemView.setTag (position);
			layoutItemMessageList = (RelativeLayout) itemView.findViewById (R.id.layoutItemMessageList);
			circleLayoutMessageNumber=(RelativeLayout) itemView.findViewById (R.id.circleLayoutMessageNumber);
			txtMessageSubject = (TextView) itemView.findViewById (R.id.txtMessageSubject);
			txtMessageBody = (TextView) itemView.findViewById (R.id.txtMessageBody);
			txtMessageDate = (TextView) itemView.findViewById (R.id.txtMessageDate);
			txtMessageCount = (TextView) itemView.findViewById (R.id.txtMessageCount);

		}

		public void populateData (String subject, String body, String date, Boolean isRead, int unreadCount) {

			layoutItemMessageList.setBackgroundColor(isRead ? context.getResources().getColor(R.color.grey_light) : context.getResources().getColor(R.color.whiteBackground));
			txtMessageSubject.setText (subject);
			txtMessageBody.setText (body);
			txtMessageDate.setText (date);

            if (unreadCount > 0) {

                circleLayoutMessageNumber.setVisibility(View.VISIBLE);
                txtMessageCount.setText(unreadCount + "");
            } else {

                circleLayoutMessageNumber.setVisibility(View.GONE);
            }

        }

		@Override
		public void onClick (View v) {

			Intent intent = new Intent (context, OpportunityMessageActivity.class);
			intent.putExtra (context.getString (R.string.conversation_id), getMessagesList ().get (getLayoutPosition ()).getId ());
			intent.putExtra (context.getString (R.string.author_name), getMessagesList ().get (getLayoutPosition ()).getAuthorName ());
			intent.putExtra (context.getString (R.string.is_message_list), true);

			if (getMessagesList ().get (getLayoutPosition ()).getStatus ().getValue () == EnumConstants.ConversationTypes.CLOSED.getValue ()) {

				intent.putExtra (context.getString (R.string.is_opportunity_closed), true);
			}
			context.startActivity (intent);
		}
	}

	public ArrayList<Message> getMessagesList () {

		return messagesList;
	}

	public void setMessagesList (ArrayList<Message> messagesList) {

		this.messagesList = messagesList;
	}

}
