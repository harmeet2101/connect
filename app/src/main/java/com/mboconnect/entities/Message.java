package com.mboconnect.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;

/**
 * Created by tahir on 29/06/15.
 */
public class Message extends BaseEntity {

	String							id;
	String							authorName;
	String							message;
	String							sent;
	int								unreadMessages;
	String							updated_at;
	EnumConstants.ConversationTypes	status;
	Boolean							isRead;

	@Override
	public void set (String jsonString) {

		JsonObject jsonObject = JsonUtility.parseToJsonObject (jsonString).getAsJsonObject ();
		JsonObject conversationObject = jsonObject.getAsJsonObject (KeyConstants.KEY_CONVERSATION);
		JsonObject lastMessageObject = conversationObject.getAsJsonObject (KeyConstants.KEY_LAST_MESSAGE);
		JsonObject countObject = conversationObject.getAsJsonObject (KeyConstants.KEY_COUNTS);
		JsonArray membersJsonArray = conversationObject.getAsJsonArray (KeyConstants.KEY_MEMBERS);

		if (lastMessageObject != null) {
			setMessageSent (lastMessageObject);
			this.message = JsonUtility.getString (lastMessageObject, KeyConstants.KEY_BODY);
		}

		setAuthorName (membersJsonArray);
		this.id = JsonUtility.getString (conversationObject, KeyConstants.KEY_ID);
		this.unreadMessages = JsonUtility.getInt (countObject, KeyConstants.KEY_UNREAD_MESSAGES);
		setStatus (conversationObject);

		if (this.unreadMessages > 0) {

			this.isRead = true;
		}
		else {

			this.isRead = false;
		}

	}

	private void setStatus (JsonObject conversationObject) {

		String status = JsonUtility.getString (conversationObject, KeyConstants.KEY_STATUS);

		if (status.equalsIgnoreCase (KeyConstants.CLOSED)) {

			this.status = EnumConstants.ConversationTypes.CLOSED;
		}
		else if (status.equalsIgnoreCase (KeyConstants.KEY_ACTIVE)) {

			this.status = EnumConstants.ConversationTypes.ACTIVE;
		}
	}

	private void setMessageSent (JsonObject jsonObject) {

		long receivedAt = JsonUtility.getLong (jsonObject, KeyConstants.KEY_RECEIVED);
		String sentDate = Utils.getFormattedDateFromMilis (receivedAt, AppConstants.OPPORTUNITY_LIST_DATE_FORMAT);
		String currentDate = Utils.getFormattedDateFromMilis (System.currentTimeMillis (), AppConstants.OPPORTUNITY_LIST_DATE_FORMAT);

		if (sentDate.equalsIgnoreCase (currentDate)) {

			this.sent = Utils.getFormattedDateFromMilis (receivedAt, AppConstants.CONVERSATION_MESSAGE_TIME_FORMAT);
		}
		else {

			this.sent = Utils.getFormattedDateFromMilis (receivedAt, AppConstants.CONVERSATION_MESSAGE_DATE_FORMAT);
		}
	}

	private void setAuthorName (JsonArray membersJsonArray) {

		for (int i = 0; i < membersJsonArray.size (); i++) {

			JsonObject memberObject = membersJsonArray.get (i).getAsJsonObject ();
			if (JsonUtility.getString (memberObject, KeyConstants.KEY_ROLE_IN_CONVERSATION).equalsIgnoreCase (KeyConstants.KEY_AUTHOR)) {

				this.authorName = JsonUtility.getString (memberObject, KeyConstants.KEY_NAME);

			}
		}

		if (StringUtility.isEmptyOrNull (authorName)) {

			for (int i = 0; i < membersJsonArray.size (); i++) {

				JsonObject memberObject = membersJsonArray.get (i).getAsJsonObject ();
				if (JsonUtility.getString (memberObject, KeyConstants.KEY_ROLE_IN_CONVERSATION).equalsIgnoreCase (KeyConstants.KEY_MANAGER)) {

					this.authorName = JsonUtility.getString (memberObject, KeyConstants.KEY_NAME);

				}
			}
		}
	}

	@Override
	public boolean equals (Object obj) {

		if (obj instanceof Message) {

			return this.getId ().equals (((Message) obj).getId ());
		}
		return false;
	}

	@Override
	public int hashCode () {

		return id.length ();
	}

	public String getUpdated_at () {

		return updated_at;
	}

	public void setUpdated_at (String updated_at) {

		this.updated_at = updated_at;
	}

	public String getSent () {

		return sent;
	}

	public void setSent (String sent) {

		this.sent = sent;
	}

	public String getMessage () {

		return message;
	}

	public void setMessage (String message) {

		this.message = message;
	}

	public String getAuthorName () {

		return authorName;
	}

	public void setAuthorName (String authorName) {

		this.authorName = authorName;
	}

	public String getMessageId () {

		return id;
	}

	public void setId (String id) {

		this.id = id;
	}

	public int getUnreadMessages () {

		return unreadMessages;
	}

	public void setUnreadMessages (int unreadMessages) {

		this.unreadMessages = unreadMessages;
	}

	public String getId () {

		return id;
	}

	public Boolean getIsRead () {

		return isRead;
	}

	public void setIsRead (Boolean isRead) {

		this.isRead = isRead;
	}

	public EnumConstants.ConversationTypes getStatus () {

		return status;
	}

	public void setStatus (EnumConstants.ConversationTypes status) {

		this.status = status;
	}

}
