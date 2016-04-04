package com.mboconnect.services.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.entities.ConversationMessage;
import com.tenpearls.android.utilities.JsonUtility;

import java.util.Collections;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class ConversationMessageResponse extends BaseResponse<ConversationMessage> {

	private ConversationMessageMeta meta;
	private final String            KEY_PAGE_SIZE    = "page_size";
	private final String            KEY_TOTAL        = "total";
	private final String            KEY_TOTAL_ACTIVE = "total_active";

	@Override
	public void set (String input) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (input);
			JsonObject metaObject = jsonObject.getAsJsonObject (KEY_META);
			JsonArray jsonArray = jsonObject.getAsJsonArray (KEY_DATA);

			meta = new ConversationMessageMeta ();
			meta.setPageSize (JsonUtility.getInt (metaObject, KEY_PAGE_SIZE));
			meta.setTotal (JsonUtility.getInt (metaObject, KEY_TOTAL));
			meta.setTotalActive (JsonUtility.getInt (metaObject, KEY_TOTAL_ACTIVE));

			for (int i = 0; i < jsonArray.size (); i++) {

				ConversationMessage conversationMessage = new ConversationMessage ();
				conversationMessage.set (jsonArray.get (i).toString ());
				list.add (conversationMessage);
			}
			Collections.reverse(list);
		}
		catch (Exception e) {

		}
	}

	public ConversationMessageMeta getMeta () {

		return meta;
	}

	public class ConversationMessageMeta {

		int total;
		int pageSize;
		int totalActive;

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

		public int getTotalActive () {

			return totalActive;
		}

		public void setTotalActive (int totalActive) {

			this.totalActive = totalActive;
		}
	}
}
