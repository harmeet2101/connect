package com.mboconnect.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.mboconnect.R;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.entities.ConversationMessage;
import com.mboconnect.entities.MessageDate;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;

import java.util.ArrayList;

/**
 * Created by tahir on 23/07/15.
 */
public class OpportunityMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final Context	 context;
	public ArrayList<Object> items = new ArrayList<> ();

	public OpportunityMessageAdapter (Context context) {

		super ();
		this.context = context;

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

		if (viewType == EnumConstants.MessageType.MANAGER.getValue ())
			return new ViewHolderManager (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_message_manager, parent, false));

		else if (viewType == EnumConstants.MessageType.ASSOCIATE.getValue ())
			return new ViewHolderAssociate (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_message_associate, parent, false));

		return new ViewHolderSectionHeader (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_message_date, parent, false));
	}

	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

		int type = getItemViewType (position);

		if (type == EnumConstants.MessageType.MANAGER.getValue ()) {

			ConversationMessage conversationMessage = (ConversationMessage) items.get (position);
			((ViewHolderManager) holder).populateData (conversationMessage.getBody (), conversationMessage.getDateCreated (), conversationMessage.getImgUrl ());

		}
		else if (type == EnumConstants.MessageType.ASSOCIATE.getValue ()) {

			ConversationMessage conversationMessage = (ConversationMessage) items.get (position);
			((ViewHolderAssociate) holder).populateData (conversationMessage.getBody (), conversationMessage.getDateCreated (), conversationMessage.getImgUrl ());

		}
		else {

			MessageDate messageDate = (MessageDate) items.get (position);
			((ViewHolderSectionHeader) holder).populateData (messageDate.getDate ());
		}
	}

	@Override
	public int getItemCount () {

		return items.size ();
	}

	@Override
	public int getItemViewType (int position) {

		if (items.get (position) instanceof ConversationMessage) {
			ConversationMessage message = (ConversationMessage) items.get (position);
			if (message.getIsMine ()) {

				return EnumConstants.MessageType.ASSOCIATE.getValue ();
			}

			return EnumConstants.MessageType.MANAGER.getValue ();
		}

		return EnumConstants.MessageType.SECTION_HEADER.getValue ();
	}

	public class ViewHolderManager extends RecyclerView.ViewHolder {

		private final NetworkImageView imgManager;
		private final TextView		   txtMessageDate;
		private final TextView		   txtMessage;

		public ViewHolderManager (View itemView) {

			super (itemView);

			txtMessage = (TextView) itemView.findViewById (R.id.txtMessage);
			txtMessageDate = (TextView) itemView.findViewById (R.id.txtMessageDate);
			imgManager = (NetworkImageView) itemView.findViewById (R.id.imgManager);

		}

		public void populateData (String message, String messageDate, String imageUrl) {

			message += "          ";
			this.txtMessage.setText (message);
			String date = Utils.getFormattedDateFromMilis (Long.parseLong (messageDate), AppConstants.OPPORTUNITY_MESSAGE_TIME_FORMAT).replace ("AM", "am").replace ("PM", "pm");
			txtMessageDate.setText (date);
			this.imgManager.setImageUrl (imageUrl, VolleyManager.getInstance ().getImageLoader ());
			this.imgManager.setErrorImageResId (R.drawable.thumbnail);
		}
	}

	public class ViewHolderAssociate extends RecyclerView.ViewHolder {

		private final TextView		   txtMessage;
		private final TextView		   txtMessageDate;
		private final NetworkImageView imgAssociate;

		public ViewHolderAssociate (View itemView) {

			super (itemView);

			txtMessage = (TextView) itemView.findViewById (R.id.txtMessage);
			txtMessageDate = (TextView) itemView.findViewById (R.id.txtMessageDate);
			imgAssociate = (NetworkImageView) itemView.findViewById (R.id.imgAssociate);
		}

		public void populateData (String message, String messageDate, String imageUrl) {

			this.txtMessage.setText (message);
			String date = Utils.getFormattedDateFromMilis (Long.parseLong (messageDate), AppConstants.OPPORTUNITY_MESSAGE_TIME_FORMAT).replace ("AM", "am").replace ("PM", "pm");
			txtMessageDate.setText (date);
			this.imgAssociate.setImageUrl (imageUrl, VolleyManager.getInstance ().getImageLoader ());
			this.imgAssociate.setErrorImageResId (R.drawable.thumbnail);

		}
	}

	public class ViewHolderSectionHeader extends RecyclerView.ViewHolder {

		private final TextView txtMessageSectionDate;

		public ViewHolderSectionHeader (View itemView) {

			super (itemView);

			txtMessageSectionDate = (TextView) itemView.findViewById (R.id.txtMessageSectionDate);

		}

		public void populateData (String messageDate) {

			this.txtMessageSectionDate.setText (messageDate);

		}
	}

	public ArrayList<Object> getItems () {

		return items;
	}

	public void setItems (ArrayList<Object> items) {

		this.items = items;
	}
}
