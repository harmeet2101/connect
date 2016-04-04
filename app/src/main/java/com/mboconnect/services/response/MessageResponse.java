package com.mboconnect.services.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.entities.Message;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class MessageResponse extends BaseResponse<Message> {

	private MessageMeta  meta;
	private final String KEY_PAGE_SIZE    = "page_size";
	private final String KEY_TOTAL        = "total";
	private final String KEY_NEW_MESSAGES = "new";

	@Override
	public void set (String input) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (input);
			JsonObject metaObject = jsonObject.getAsJsonObject (KEY_META);
			JsonArray jsonArray = jsonObject.getAsJsonArray (KEY_DATA);

			meta = new MessageMeta ();
			meta.setPageSize (JsonUtility.getInt (metaObject, KEY_PAGE_SIZE));
			meta.setTotal (JsonUtility.getInt (metaObject, KEY_TOTAL));
			meta.setNewMessages (JsonUtility.getInt (metaObject, KEY_NEW_MESSAGES));

			for (int i = 0; i < jsonArray.size (); i++) {

				Message message = new Message ();
				message.set (jsonArray.get (i).toString ());
				list.add (message);
			}
		}
		catch (Exception e) {

		}
	}

	public MessageMeta getMeta () {

		return meta;
	}

	public void setMeta (MessageMeta meta) {

		this.meta = meta;
	}

	public class MessageMeta {

		int total;
		int pageSize;
		int newMessages;

		public int getTotal () {

			return total;
		}

		public void setTotal (int total) {

			this.total = total;
		}

		public int getPageSize () {

			return pageSize;
		}

		public void setPageSize (int pageSize) {

			this.pageSize = pageSize;
		}

		public int getNewMessages () {

			return newMessages;
		}

		public void setNewMessages (int newMessages) {

			this.newMessages = newMessages;
		}
	}

}
